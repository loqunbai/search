package com.qunbai.lib;

import com.qunbai.lib.SynonymAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import com.ik.analyzer.lucene.IKAnalyzer;

/**
 * Created by aki on 9/18/15.
 */
public class Search {


    // 初始化Search所需实例

    private static int hitsPerPage = 1000;
    private static Directory index;
    private static SynonymAnalyzer synonymAnalyzer = new SynonymAnalyzer();

    private StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
    private IKAnalyzer chineseAnalyzer = new IKAnalyzer();

    public Search(){

    }

    public String parseQueryStr(String str, int begin){

        QueryParser queryParser = new QueryParser("global", chineseAnalyzer);

        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage + begin*hitsPerPage);
        IndexSearcher searcher = new IndexSearcher(SearchFactory.getReader());

//        SortField fieldSort = new SortField(textType, SortField.Type.SCORE);
        SortField dateSort  = new SortField("date", SortField.Type.INT, true);

        Sort sort = new Sort(dateSort);
        // Search...
        String ids = "";

        int allHits = 0;

        try {
            Query q = queryParser.parse(str);
            TopDocs topDocs = searcher.search(q, hitsPerPage, sort);
            ScoreDoc[] hits = topDocs.scoreDocs;

            int i;

            allHits = hits.length;


            for( i = begin * 10; i < begin * 10 + 10; ++i) {
                if(i > hits.length - 1) break;
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                ids += d.get("id").toString()+",";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return ids + "hits:" + allHits;
    }

    public String run(String str, int begin){

        String ids = "";

        String[] qs = str.split(":");

        String queryStr;
        String textType;

        if(qs.length > 1){

            textType = qs[0];
            queryStr = qs[1];
        }else{
            textType = "global";
            queryStr = qs[0];
        }

        System.out.println(queryStr + " search start...");

        SortField fieldSort = new SortField(textType, SortField.Type.SCORE);
        SortField dateSort  = new SortField("date", SortField.Type.INT, true);

        Sort sort = new Sort(fieldSort, dateSort);

        int allHits = 0;

        try{

            BooleanQuery q = synonymAnalyzer.getQuery(queryStr, textType);
            IndexSearcher searcher = new IndexSearcher(SearchFactory.getReader());

            // Search...
            TopDocs topDocs = searcher.search(q, hitsPerPage, sort);

            ScoreDoc[] hits = topDocs.scoreDocs;

            int i;

            allHits = hits.length;;

            for(i = begin * 10; i < begin * 10 + 10; ++i) {

                if(i > hits.length - 1) break;

                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                ids += d.get("id").toString()+",";
            }

            System.out.println(queryStr + " search success...and the result count is " + hits.length);

        }catch(Exception e){

            e.printStackTrace();

            System.out.println(queryStr + " search failed...");
        }

        return ids + "hits:" + allHits;
    }
}
