//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.lucene;

import java.io.BufferedReader;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import com.ik.analyzer.lucene.IKTokenizer;

public final class IKAnalyzer extends Analyzer {
    private boolean useSmart;

    public boolean useSmart() {
        return this.useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public IKAnalyzer() {
        this(false);
    }

    public IKAnalyzer(boolean useSmart) {
        this.useSmart = useSmart;
    }

    protected TokenStreamComponents createComponents(String text) {
        BufferedReader reader = new BufferedReader(new StringReader(text));
        IKTokenizer _IKTokenizer = new IKTokenizer(reader, this.useSmart());
        return new TokenStreamComponents(_IKTokenizer);
    }
}
