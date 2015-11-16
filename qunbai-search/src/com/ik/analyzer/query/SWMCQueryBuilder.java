//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.query;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import com.ik.analyzer.core.IKSegmenter;
import com.ik.analyzer.core.Lexeme;

public class SWMCQueryBuilder {
    public SWMCQueryBuilder() {
    }

    public static Query create(String fieldName, String keywords, boolean quickMode) {
        if(fieldName != null && keywords != null) {
            List lexemes = doAnalyze(keywords);
            Query _SWMCQuery = getSWMCQuery(fieldName, lexemes, quickMode);
            return _SWMCQuery;
        } else {
            throw new IllegalArgumentException("参数 fieldName 、 keywords 不能为null.");
        }
    }

    private static List<Lexeme> doAnalyze(String keywords) {
        ArrayList lexemes = new ArrayList();
        IKSegmenter ikSeg = new IKSegmenter(new StringReader(keywords), true);

        try {
            Lexeme e = null;

            while((e = ikSeg.next()) != null) {
                lexemes.add(e);
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return lexemes;
    }

    private static Query getSWMCQuery(String fieldName, List<Lexeme> lexemes, boolean quickMode) {
        StringBuffer keywordBuffer = new StringBuffer();
        StringBuffer keywordBuffer_Short = new StringBuffer();
        int lastLexemeLength = 0;
        int lastLexemeEnd = -1;
        int shortCount = 0;
        int totalCount = 0;

        Lexeme qp;
        for(Iterator e = lexemes.iterator(); e.hasNext(); lastLexemeEnd = qp.getEndPosition()) {
            qp = (Lexeme)e.next();
            totalCount += qp.getLength();
            if(qp.getLength() > 1) {
                keywordBuffer_Short.append(' ').append(qp.getLexemeText());
                shortCount += qp.getLength();
            }

            if(lastLexemeLength == 0) {
                keywordBuffer.append(qp.getLexemeText());
            } else if(lastLexemeLength == 1 && qp.getLength() == 1 && lastLexemeEnd == qp.getBeginPosition()) {
                keywordBuffer.append(qp.getLexemeText());
            } else {
                keywordBuffer.append(' ').append(qp.getLexemeText());
            }

            lastLexemeLength = qp.getLength();
        }

        QueryParser qp1 = new QueryParser(fieldName, new StandardAnalyzer());
        qp1.setDefaultOperator(QueryParser.AND_OPERATOR);
        qp1.setAutoGeneratePhraseQueries(true);
        Query e1;
        if(quickMode && (float)shortCount * 1.0F / (float)totalCount > 0.5F) {
            try {
                e1 = qp1.parse(keywordBuffer_Short.toString());
                return e1;
            } catch (ParseException var12) {
                var12.printStackTrace();
            }
        } else if(keywordBuffer.length() > 0) {
            try {
                e1 = qp1.parse(keywordBuffer.toString());
                return e1;
            } catch (ParseException var11) {
                var11.printStackTrace();
            }
        }

        return null;
    }
}
