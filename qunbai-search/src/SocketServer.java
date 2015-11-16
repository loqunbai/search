/**
 * Created by aki on 9/16/15.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.qunbai.lib.IndexManager;
import com.qunbai.lib.Search;
import net.sf.json.JSONObject;


public class SocketServer {

    public static int PORT = 12345;//监听的端口号

    public static String LimitIp = "";//监听的端口号


    private Search search = new Search();

    private IndexManager indexManager = new IndexManager();


    public static void main(String[] args) throws IOException {


        int portFormArgs = PORT;

        for(int i = 0; i < args.length; i++){

            if(args[i].equals("-p") || args[i].equals("--port")){

                if(i < args.length && args[1 + i] != null) {
                    portFormArgs = Integer.valueOf(args[i + 1]);
                }
            }

            if(args[i].equals("--limitip")){

                if(i < args.length && args[1 + i] != null) {
                    LimitIp = args[1 + i];
                }
            }

        }

        SocketServer server = new SocketServer();
        server.init(portFormArgs);
    }

    public void init(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("TCP Server running at 127.0.0.1:" + port);

            while (true) {

                Socket client = serverSocket.accept();
                new HandlerThread(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parse(JSONObject jsonObject) throws Exception{

        String functionName = jsonObject.getString("time");

        int page = 0;
        /*
            id     为索引id
            field  索引标示符
            text   文字内容
        */
        try {
            page = jsonObject.getInt("page");
        }catch (Exception e){

        }
        if(jsonObject.getString("type").equals("addIndex")){

            String id    = jsonObject.getString("_id");
            String text  = jsonObject.getString("text");
            String field = jsonObject.getString("field");
            int date = jsonObject.getInt("date");

            indexManager.addDoc(field, text.split("\u2222"), id, date);

            return functionName + ">add success";
        }

        if(jsonObject.getString("type").equals("addMultiIndex")){

            String id = jsonObject.getString("_id");
            String fieldType = jsonObject.getString("fieldType");
            String field = jsonObject.getString("field");
            int date = jsonObject.getInt("date");
            String[] kv = field.split("\u2222");
            System.out.println(date);
            Map<String, String> fields = new HashMap<>();

            for(String k : kv){

                String[] fv = k.split(":");

                if(fv.length == 2){
                    fields.put(fv[0], fv[1]);
                }
            }

            indexManager.addDoc(fields, id, fieldType, date);

            return functionName + ">add success";
        }

        // 检索索引
        // 语法格式为： field:查询字符串
        if(jsonObject.getString("type").equals("search")){

            String queryStr = jsonObject.getString("queryStr");

            return functionName + ">" + search.run(queryStr, page);
        }


        if(jsonObject.getString("type").equals("multiSearch")){

            String queryStr = jsonObject.getString("queryStr");

            return functionName + ">" + search.parseQueryStr(queryStr, page);
        }

        if(jsonObject.getString("type").equals("delete")){

            String id = jsonObject.getString("_id");
            indexManager.deleteIndex(id);
            return functionName + ">delete success";
        }

        return functionName + ">error action";
    }


    private class HandlerThread implements Runnable {

        private Socket socket;

        public HandlerThread(Socket client) {
            socket = client;

            if(!LimitIp.equals("") && socket.getRemoteSocketAddress().toString().indexOf(LimitIp) == -1){

                System.out.println(socket.getRemoteSocketAddress().toString() + " blocked");
                return;
            }

            System.out.println(socket.getRemoteSocketAddress().toString() + " connected");
            new Thread(this).start();
        }

        public void run() {

            try {

                // 读取客户端数据
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line = null;

                while ((line = br.readLine()) != null) {

                    System.out.println("Client Message:" + line);

                    JSONObject jo = JSONObject.fromObject(line);

                    out.writeUTF(parse(jo)+"\u2222");
                }

                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

