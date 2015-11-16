package com.qunbai.lib;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by aki on 9/23/15.
 */
public class SearchFactory {

    private static Path path = FileSystems.getDefault().getPath("index");

    public static IndexReader reader;

    public static void reopenReader(){

        try{
            IndexReader newReader = getReopenReader();

            if(newReader != reader && reader != null){
                reader.close();
            }else{
                reader = newReader;
                System.out.println("refresh reader");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static IndexReader getReopenReader(){

        try {

            Directory index = FSDirectory.open(path);

            reader = DirectoryReader.open(index);

            return reader;

        }catch(Exception e){

            e.printStackTrace();

            return null;
        }
    }

    public static IndexReader getReader(){

        if(reader != null){

            return reader;
        }

        return getReopenReader();
    }


}
