package com.ik.analyzer.core;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.ik.analyzer.core.AnalyzeContext;
import com.ik.analyzer.core.ISegmenter;
import com.ik.analyzer.core.Lexeme;
import com.ik.analyzer.dic.Dictionary;
import com.ik.analyzer.dic.Hit;

class CN_QuantifierSegmenter implements ISegmenter {
    static final String SEGMENTER_NAME = "QUAN_SEGMENTER";
    private static String Chn_Num = "一二两三四五六七八九十零壹贰叁肆伍陆柒捌玖拾百千万亿拾佰仟萬億兆卅廿";
    private static Set<Character> ChnNumberChars = new HashSet();
    private int nStart = -1;
    private int nEnd = -1;
    private List<Hit> countHits = new LinkedList();

    static {
        char[] ca = Chn_Num.toCharArray();
        char[] var4 = ca;
        int var3 = ca.length;

        for(int var2 = 0; var2 < var3; ++var2) {
            char nChar = var4[var2];
            ChnNumberChars.add(Character.valueOf(nChar));
        }

    }

    CN_QuantifierSegmenter() {
    }

    public void analyze(AnalyzeContext context) {
        this.processCNumber(context);
        this.processCount(context);
        if(this.nStart == -1 && this.nEnd == -1 && this.countHits.isEmpty()) {
            context.unlockBuffer("QUAN_SEGMENTER");
        } else {
            context.lockBuffer("QUAN_SEGMENTER");
        }

    }

    public void reset() {
        this.nStart = -1;
        this.nEnd = -1;
        this.countHits.clear();
    }

    private void processCNumber(AnalyzeContext context) {
        if(this.nStart == -1 && this.nEnd == -1) {
            if(4 == context.getCurrentCharType() && ChnNumberChars.contains(Character.valueOf(context.getCurrentChar()))) {
                this.nStart = context.getCursor();
                this.nEnd = context.getCursor();
            }
        } else if(4 == context.getCurrentCharType() && ChnNumberChars.contains(Character.valueOf(context.getCurrentChar()))) {
            this.nEnd = context.getCursor();
        } else {
            this.outputNumLexeme(context);
            this.nStart = -1;
            this.nEnd = -1;
        }

        if(context.isBufferConsumed() && this.nStart != -1 && this.nEnd != -1) {
            this.outputNumLexeme(context);
            this.nStart = -1;
            this.nEnd = -1;
        }

    }

    private void processCount(AnalyzeContext context) {
        if(this.needCountScan(context)) {
            if(4 == context.getCurrentCharType()) {
                if(!this.countHits.isEmpty()) {
                    Hit[] singleCharHit = (Hit[])this.countHits.toArray(new Hit[this.countHits.size()]);
                    Hit[] var6 = singleCharHit;
                    int var5 = singleCharHit.length;

                    for(int var4 = 0; var4 < var5; ++var4) {
                        Hit newLexeme = var6[var4];
                        newLexeme = Dictionary.getSingleton().matchWithHit(context.getSegmentBuff(), context.getCursor(), newLexeme);
                        if(newLexeme.isMatch()) {
                            Lexeme newLexeme1 = new Lexeme(context.getBufferOffset(), newLexeme.getBegin(), context.getCursor() - newLexeme.getBegin() + 1, 32);
                            context.addLexeme(newLexeme1);
                            if(!newLexeme.isPrefix()) {
                                this.countHits.remove(newLexeme);
                            }
                        } else if(newLexeme.isUnmatch()) {
                            this.countHits.remove(newLexeme);
                        }
                    }
                }

                Hit var8 = Dictionary.getSingleton().matchInQuantifierDict(context.getSegmentBuff(), context.getCursor(), 1);
                if(var8.isMatch()) {
                    Lexeme var9 = new Lexeme(context.getBufferOffset(), context.getCursor(), 1, 32);
                    context.addLexeme(var9);
                    if(var8.isPrefix()) {
                        this.countHits.add(var8);
                    }
                } else if(var8.isPrefix()) {
                    this.countHits.add(var8);
                }
            } else {
                this.countHits.clear();
            }

            if(context.isBufferConsumed()) {
                this.countHits.clear();
            }

        }
    }

    private boolean needCountScan(AnalyzeContext context) {
        if((this.nStart == -1 || this.nEnd == -1) && this.countHits.isEmpty()) {
            if(!context.getOrgLexemes().isEmpty()) {
                Lexeme l = context.getOrgLexemes().peekLast();
                if((16 == l.getLexemeType() || 2 == l.getLexemeType()) && l.getBegin() + l.getLength() == context.getCursor()) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    private void outputNumLexeme(AnalyzeContext context) {
        if(this.nStart > -1 && this.nEnd > -1) {
            Lexeme newLexeme = new Lexeme(context.getBufferOffset(), this.nStart, this.nEnd - this.nStart + 1, 16);
            context.addLexeme(newLexeme);
        }

    }
}