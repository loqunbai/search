//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.core;

import java.util.Arrays;
import com.ik.analyzer.core.AnalyzeContext;
import com.ik.analyzer.core.ISegmenter;
import com.ik.analyzer.core.Lexeme;

class LetterSegmenter implements ISegmenter {
    static final String SEGMENTER_NAME = "LETTER_SEGMENTER";
    private static final char[] Letter_Connector = new char[]{'#', '&', '+', '-', '.', '@', '_'};
    private static final char[] Num_Connector = new char[]{',', '.'};
    private int start;
    private int end;
    private int englishStart;
    private int englishEnd;
    private int arabicStart;
    private int arabicEnd;

    LetterSegmenter() {
        Arrays.sort(Letter_Connector);
        Arrays.sort(Num_Connector);
        this.start = -1;
        this.end = -1;
        this.englishStart = -1;
        this.englishEnd = -1;
        this.arabicStart = -1;
        this.arabicEnd = -1;
    }

    public void analyze(AnalyzeContext context) {
        boolean bufferLockFlag = false;
        bufferLockFlag = this.processEnglishLetter(context) || bufferLockFlag;
        bufferLockFlag = this.processArabicLetter(context) || bufferLockFlag;
        bufferLockFlag = this.processMixLetter(context) || bufferLockFlag;
        if(bufferLockFlag) {
            context.lockBuffer("LETTER_SEGMENTER");
        } else {
            context.unlockBuffer("LETTER_SEGMENTER");
        }

    }

    public void reset() {
        this.start = -1;
        this.end = -1;
        this.englishStart = -1;
        this.englishEnd = -1;
        this.arabicStart = -1;
        this.arabicEnd = -1;
    }

    private boolean processMixLetter(AnalyzeContext context) {
        boolean needLock = false;
        Lexeme newLexeme;
        if(this.start == -1) {
            if(1 == context.getCurrentCharType() || 2 == context.getCurrentCharType()) {
                this.start = context.getCursor();
                this.end = this.start;
            }
        } else if(1 != context.getCurrentCharType() && 2 != context.getCurrentCharType()) {
            if(context.getCurrentCharType() == 0 && this.isLetterConnector(context.getCurrentChar())) {
                this.end = context.getCursor();
            } else {
                newLexeme = new Lexeme(context.getBufferOffset(), this.start, this.end - this.start + 1, 3);
                context.addLexeme(newLexeme);
                this.start = -1;
                this.end = -1;
            }
        } else {
            this.end = context.getCursor();
        }

        if(context.isBufferConsumed() && this.start != -1 && this.end != -1) {
            newLexeme = new Lexeme(context.getBufferOffset(), this.start, this.end - this.start + 1, 3);
            context.addLexeme(newLexeme);
            this.start = -1;
            this.end = -1;
        }

        if(this.start == -1 && this.end == -1) {
            needLock = false;
        } else {
            needLock = true;
        }

        return needLock;
    }

    private boolean processEnglishLetter(AnalyzeContext context) {
        boolean needLock = false;
        Lexeme newLexeme;
        if(this.englishStart == -1) {
            if(2 == context.getCurrentCharType()) {
                this.englishStart = context.getCursor();
                this.englishEnd = this.englishStart;
            }
        } else if(2 == context.getCurrentCharType()) {
            this.englishEnd = context.getCursor();
        } else {
            newLexeme = new Lexeme(context.getBufferOffset(), this.englishStart, this.englishEnd - this.englishStart + 1, 1);
            context.addLexeme(newLexeme);
            this.englishStart = -1;
            this.englishEnd = -1;
        }

        if(context.isBufferConsumed() && this.englishStart != -1 && this.englishEnd != -1) {
            newLexeme = new Lexeme(context.getBufferOffset(), this.englishStart, this.englishEnd - this.englishStart + 1, 1);
            context.addLexeme(newLexeme);
            this.englishStart = -1;
            this.englishEnd = -1;
        }

        if(this.englishStart == -1 && this.englishEnd == -1) {
            needLock = false;
        } else {
            needLock = true;
        }

        return needLock;
    }

    private boolean processArabicLetter(AnalyzeContext context) {
        boolean needLock = false;
        Lexeme newLexeme;
        if(this.arabicStart == -1) {
            if(1 == context.getCurrentCharType()) {
                this.arabicStart = context.getCursor();
                this.arabicEnd = this.arabicStart;
            }
        } else if(1 == context.getCurrentCharType()) {
            this.arabicEnd = context.getCursor();
        } else if(context.getCurrentCharType() != 0 || !this.isNumConnector(context.getCurrentChar())) {
            newLexeme = new Lexeme(context.getBufferOffset(), this.arabicStart, this.arabicEnd - this.arabicStart + 1, 2);
            context.addLexeme(newLexeme);
            this.arabicStart = -1;
            this.arabicEnd = -1;
        }

        if(context.isBufferConsumed() && this.arabicStart != -1 && this.arabicEnd != -1) {
            newLexeme = new Lexeme(context.getBufferOffset(), this.arabicStart, this.arabicEnd - this.arabicStart + 1, 2);
            context.addLexeme(newLexeme);
            this.arabicStart = -1;
            this.arabicEnd = -1;
        }

        if(this.arabicStart == -1 && this.arabicEnd == -1) {
            needLock = false;
        } else {
            needLock = true;
        }

        return needLock;
    }

    private boolean isLetterConnector(char input) {
        int index = Arrays.binarySearch(Letter_Connector, input);
        return index >= 0;
    }

    private boolean isNumConnector(char input) {
        int index = Arrays.binarySearch(Num_Connector, input);
        return index >= 0;
    }
}
