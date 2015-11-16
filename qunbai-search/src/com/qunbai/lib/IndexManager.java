package com.qunbai.lib;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import com.ik.analyzer.lucene.IKAnalyzer;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

public class IndexManager {

    private Path path = FileSystems.getDefault().getPath("index");

    private StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
    private IKAnalyzer chineseAnalyzer = new IKAnalyzer();

    private Directory index;

    public IndexManager(){

        try{
            index = FSDirectory.open(path);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addIndex(String uid, Document doc){
        try{

            Term term = new Term("id", uid);
            IndexWriterConfig standardConfig = new IndexWriterConfig(standardAnalyzer);
            IndexWriter standardWriter = new IndexWriter(index, standardConfig);

            standardWriter.deleteDocuments(term);
            standardWriter.commit();
            standardWriter.addDocument(doc);
            standardWriter.close();

            IndexWriterConfig chineseConfig = new IndexWriterConfig(chineseAnalyzer);
            IndexWriter chineseWriter  = new IndexWriter(index, chineseConfig);


            chineseWriter.deleteDocuments(term);
            chineseWriter.commit();
            chineseWriter.addDocument(doc);
            chineseWriter.close();

            SearchFactory.reopenReader();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addDoc(Map fields, String id, String fieldType, int date){

        Document doc = new Document();

        Iterator it = fields.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry entry = (Map.Entry) it.next();

            String field = (String)entry.getKey();
            String value = (String)entry.getValue();

            doc.add(new TextField("global", value, Field.Store.YES));
            doc.add(new TextField(field,    value, Field.Store.YES));
        }

        String uid = fieldType + ":" + id;

        // 获取数据 字段
        doc.add(new NumericDocValuesField("date", date));
        doc.add(new StringField("id", uid, Field.Store.YES));

        addIndex(uid, doc);
    }



    public void addDoc(String textType,String textArray[], String id, int date){

        Document doc = new Document();

        String uid = textType + ":" + id;

        for(int i = 0; i < textArray.length; i++){
            doc.add(new TextField("global", textArray[i], Field.Store.YES));
            doc.add(new TextField(textType, textArray[i], Field.Store.YES));
        }
        doc.add(new NumericDocValuesField("date", date));
        // 获取数据
        doc.add(new StringField("id", uid, Field.Store.YES));

        addIndex(uid, doc);
    }

    public void deleteIndex(String id){

        IndexWriterConfig standardConfig = new IndexWriterConfig(standardAnalyzer);
        IndexWriterConfig chineseConfig = new IndexWriterConfig(chineseAnalyzer);

        Term term = new Term("id", id);
        try {

            System.out.print(id);
            IndexWriter standardWriter = new IndexWriter(index, standardConfig);
            standardWriter.deleteDocuments(term);
            standardWriter.commit();
            standardWriter.close();

            IndexWriter chineseWriter  = new IndexWriter(index, chineseConfig);

            chineseWriter.deleteDocuments(term);
            chineseWriter.commit();
            chineseWriter.close();

            SearchFactory.reopenReader();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}