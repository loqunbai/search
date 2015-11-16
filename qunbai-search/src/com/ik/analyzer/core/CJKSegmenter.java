
package com.ik.analyzer.core;



import java.util.LinkedList;
import java.util.List;
import com.ik.analyzer.core.AnalyzeContext;
import com.ik.analyzer.core.ISegmenter;
import com.ik.analyzer.core.Lexeme;
import com.ik.analyzer.dic.Dictionary;
import com.ik.analyzer.dic.Hit;

class CJKSegmenter implements ISegmenter {
    static final String SEGMENTER_NAME = "CJK_SEGMENTER";
    private List<Hit> tmpHits = new LinkedList();

    CJKSegmenter() {
    }

    public void analyze(AnalyzeContext context) {
        if(context.getCurrentCharType() != 0) {
            if(!this.tmpHits.isEmpty()) {
                Hit[] singleCharHit = (Hit[])this.tmpHits.toArray(new Hit[this.tmpHits.size()]);
                Hit[] var6 = singleCharHit;
                int var5 = singleCharHit.length;

                for(int var4 = 0; var4 < var5; ++var4) {
                    Hit newLexeme = var6[var4];
                    newLexeme = Dictionary.getSingleton().matchWithHit(context.getSegmentBuff(), context.getCursor(), newLexeme);
                    if(newLexeme.isMatch()) {
                        Lexeme newLexeme1 = new Lexeme(context.getBufferOffset(), newLexeme.getBegin(), context.getCursor() - newLexeme.getBegin() + 1, 4);
                        context.addLexeme(newLexeme1);
                        if(!newLexeme.isPrefix()) {
                            this.tmpHits.remove(newLexeme);
                        }
                    } else if(newLexeme.isUnmatch()) {
                        this.tmpHits.remove(newLexeme);
                    }
                }
            }

            Hit var8 = Dictionary.getSingleton().matchInMainDict(context.getSegmentBuff(), context.getCursor(), 1);
            if(var8.isMatch()) {
                Lexeme var9 = new Lexeme(context.getBufferOffset(), context.getCursor(), 1, 4);
                context.addLexeme(var9);
                if(var8.isPrefix()) {
                    this.tmpHits.add(var8);
                }
            } else if(var8.isPrefix()) {
                this.tmpHits.add(var8);
            }
        } else {
            this.tmpHits.clear();
        }

        if(context.isBufferConsumed()) {
            this.tmpHits.clear();
        }

        if(this.tmpHits.size() == 0) {
            context.unlockBuffer("CJK_SEGMENTER");
        } else {
            context.lockBuffer("CJK_SEGMENTER");
        }

    }

    public void reset() {
        this.tmpHits.clear();
    }
}
