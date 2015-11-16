//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.core;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.ik.analyzer.cfg.Configuration;
import com.ik.analyzer.cfg.DefaultConfig;
import com.ik.analyzer.core.AnalyzeContext;
import com.ik.analyzer.core.CJKSegmenter;
import com.ik.analyzer.core.CN_QuantifierSegmenter;
import com.ik.analyzer.core.IKArbitrator;
import com.ik.analyzer.core.ISegmenter;
import com.ik.analyzer.core.LetterSegmenter;
import com.ik.analyzer.core.Lexeme;
import com.ik.analyzer.dic.Dictionary;

public final class IKSegmenter {
    private Reader input;
    private Configuration cfg;
    private AnalyzeContext context;
    private List<ISegmenter> segmenters;
    private IKArbitrator arbitrator;

    public IKSegmenter(Reader input, boolean useSmart) {
        this.input = input;
        this.cfg = DefaultConfig.getInstance();
        this.cfg.setUseSmart(useSmart);
        this.init();
    }

    public IKSegmenter(Reader input, Configuration cfg) {
        this.input = input;
        this.cfg = cfg;
        this.init();
    }

    private void init() {
        Dictionary.initial(this.cfg);
        this.context = new AnalyzeContext(this.cfg);
        this.segmenters = this.loadSegmenters();
        this.arbitrator = new IKArbitrator();
    }

    private List<ISegmenter> loadSegmenters() {
        ArrayList segmenters = new ArrayList(4);
        segmenters.add(new LetterSegmenter());
        segmenters.add(new CN_QuantifierSegmenter());
        segmenters.add(new CJKSegmenter());
        return segmenters;
    }

    public synchronized Lexeme next() throws IOException {
        Lexeme l = null;

        while((l = this.context.getNextLexeme()) == null) {
            int available = this.context.fillBuffer(this.input);
            if(available <= 0) {
                this.context.reset();
                return null;
            }

            this.context.initCursor();

            ISegmenter segmenter;
            Iterator var4;
            do {
                var4 = this.segmenters.iterator();

                while(var4.hasNext()) {
                    segmenter = (ISegmenter)var4.next();
                    segmenter.analyze(this.context);
                }
            } while(!this.context.needRefillBuffer() && this.context.moveCursor());

            var4 = this.segmenters.iterator();

            while(var4.hasNext()) {
                segmenter = (ISegmenter)var4.next();
                segmenter.reset();
            }

            this.arbitrator.process(this.context, this.cfg.useSmart());
            this.context.outputToResult();
            this.context.markBufferOffset();
        }

        return l;
    }

    public synchronized void reset(Reader input) {
        this.input = input;
        this.context.reset();
        Iterator var3 = this.segmenters.iterator();

        while(var3.hasNext()) {
            ISegmenter segmenter = (ISegmenter)var3.next();
            segmenter.reset();
        }

    }
}
