//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.dic;

import com.ik.analyzer.dic.DictSegment;

public class Hit {
    private static final int UNMATCH = 0;
    private static final int MATCH = 1;
    private static final int PREFIX = 16;
    private int hitState = 0;
    private DictSegment matchedDictSegment;
    private int begin;
    private int end;

    public Hit() {
    }

    public boolean isMatch() {
        return (this.hitState & 1) > 0;
    }

    public void setMatch() {
        this.hitState |= 1;
    }

    public boolean isPrefix() {
        return (this.hitState & 16) > 0;
    }

    public void setPrefix() {
        this.hitState |= 16;
    }

    public boolean isUnmatch() {
        return this.hitState == 0;
    }

    public void setUnmatch() {
        this.hitState = 0;
    }

    public DictSegment getMatchedDictSegment() {
        return this.matchedDictSegment;
    }

    public void setMatchedDictSegment(DictSegment matchedDictSegment) {
        this.matchedDictSegment = matchedDictSegment;
    }

    public int getBegin() {
        return this.begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return this.end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
