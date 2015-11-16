package com.qunbai.lib;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.FilesystemResourceLoader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import com.ik.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aki on 9/24/15.
 */
public class SynonymAnalyzer {

    private static Path path = FileSystems.getDefault().getPath("index");
    private static IKAnalyzer smartChineseAnalyzer = new IKAnalyzer();
    private static SynonymFilterFactory factory;

    public SynonymAnalyzer() {

        Map<String, String> filterArgs = new HashMap<String, String>();
        filterArgs.put("synonyms", "./synonyms");
        filterArgs.put("expand", "true");
        factory = new SynonymFilterFactory(filterArgs);

        try {
            factory.inform(new FilesystemResourceLoader(path));
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public BooleanQuery getQuery(String queryStr, String field) throws IOException {


        TokenStream ts = factory.create(smartChineseAnalyzer.tokenStream(field, queryStr));

        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);

        ts.reset();

        while (ts.incrementToken()) {

            String t = term.toString();
            Query q = new TermQuery(new Term(field,t));

            builder.add(q, BooleanClause.Occur.SHOULD);
        }
        ts.close();
        BooleanQuery resultQuery =  builder.build();

        return resultQuery;
    }
}
