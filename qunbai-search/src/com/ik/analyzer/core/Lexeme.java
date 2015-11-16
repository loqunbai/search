//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.core;



public class Lexeme implements Comparable<Lexeme> {
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ENGLISH = 1;
    public static final int TYPE_ARABIC = 2;
    public static final int TYPE_LETTER = 3;
    public static final int TYPE_CNWORD = 4;
    public static final int TYPE_CNCHAR = 64;
    public static final int TYPE_OTHER_CJK = 8;
    public static final int TYPE_CNUM = 16;
    public static final int TYPE_COUNT = 32;
    public static final int TYPE_CQUAN = 48;
    private int offset;
    private int begin;
    private int length;
    private String lexemeText;
    private int lexemeType;

    public Lexeme(int offset, int begin, int length, int lexemeType) {
        this.offset = offset;
        this.begin = begin;
        if(length < 0) {
            throw new IllegalArgumentException("length < 0");
        } else {
            this.length = length;
            this.lexemeType = lexemeType;
        }
    }

    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if(this == o) {
            return true;
        } else if(o instanceof Lexeme) {
            Lexeme other = (Lexeme)o;
            return this.offset == other.getOffset() && this.begin == other.getBegin() && this.length == other.getLength();
        } else {
            return false;
        }
    }

    public int hashCode() {
        int absBegin = this.getBeginPosition();
        int absEnd = this.getEndPosition();
        return absBegin * 37 + absEnd * 31 + absBegin * absEnd % this.getLength() * 11;
    }

    public int compareTo(Lexeme other) {
        return this.begin < other.getBegin()?-1:(this.begin == other.getBegin()?(this.length > other.getLength()?-1:(this.length == other.getLength()?0:1)):1);
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getBegin() {
        return this.begin;
    }

    public int getBeginPosition() {
        return this.offset + this.begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEndPosition() {
        return this.offset + this.begin + this.length;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        if(this.length < 0) {
            throw new IllegalArgumentException("length < 0");
        } else {
            this.length = length;
        }
    }

    public String getLexemeText() {
        return this.lexemeText == null?"":this.lexemeText;
    }

    public void setLexemeText(String lexemeText) {
        if(lexemeText == null) {
            this.lexemeText = "";
            this.length = 0;
        } else {
            this.lexemeText = lexemeText;
            this.length = lexemeText.length();
        }

    }

    public int getLexemeType() {
        return this.lexemeType;
    }

    public String getLexemeTypeString() {
        switch(this.lexemeType) {
        case 1:
            return "ENGLISH";
        case 2:
            return "ARABIC";
        case 3:
            return "LETTER";
        case 4:
            return "CN_WORD";
        case 8:
            return "OTHER_CJK";
        case 16:
            return "TYPE_CNUM";
        case 32:
            return "COUNT";
        case 48:
            return "TYPE_CQUAN";
        case 64:
            return "CN_CHAR";
        default:
            return "UNKONW";
        }
    }

    public void setLexemeType(int lexemeType) {
        this.lexemeType = lexemeType;
    }

    public boolean append(Lexeme l, int lexemeType) {
        if(l != null && this.getEndPosition() == l.getBeginPosition()) {
            this.length += l.getLength();
            this.lexemeType = lexemeType;
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
        strbuf.append(" : ").append(this.lexemeText).append(" : \t");
        strbuf.append(this.getLexemeTypeString());
        return strbuf.toString();
    }
}
