//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.core;

import com.ik.analyzer.core.Lexeme;
import com.ik.analyzer.core.QuickSortSet;
import com.ik.analyzer.core.QuickSortSet.Cell;

class LexemePath extends QuickSortSet implements Comparable<LexemePath> {
    private int pathBegin = -1;
    private int pathEnd = -1;
    private int payloadLength = 0;

    LexemePath() {
    }

    boolean addCrossLexeme(Lexeme lexeme) {
        if(this.isEmpty()) {
            this.addLexeme(lexeme);
            this.pathBegin = lexeme.getBegin();
            this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            this.payloadLength += lexeme.getLength();
            return true;
        } else if(this.checkCross(lexeme)) {
            this.addLexeme(lexeme);
            if(lexeme.getBegin() + lexeme.getLength() > this.pathEnd) {
                this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            }

            this.payloadLength = this.pathEnd - this.pathBegin;
            return true;
        } else {
            return false;
        }
    }

    boolean addNotCrossLexeme(Lexeme lexeme) {
        if(this.isEmpty()) {
            this.addLexeme(lexeme);
            this.pathBegin = lexeme.getBegin();
            this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            this.payloadLength += lexeme.getLength();
            return true;
        } else if(this.checkCross(lexeme)) {
            return false;
        } else {
            this.addLexeme(lexeme);
            this.payloadLength += lexeme.getLength();
            Lexeme head = this.peekFirst();
            this.pathBegin = head.getBegin();
            Lexeme tail = this.peekLast();
            this.pathEnd = tail.getBegin() + tail.getLength();
            return true;
        }
    }

    Lexeme removeTail() {
        Lexeme tail = this.pollLast();
        if(this.isEmpty()) {
            this.pathBegin = -1;
            this.pathEnd = -1;
            this.payloadLength = 0;
        } else {
            this.payloadLength -= tail.getLength();
            Lexeme newTail = this.peekLast();
            this.pathEnd = newTail.getBegin() + newTail.getLength();
        }

        return tail;
    }

    boolean checkCross(Lexeme lexeme) {
        return lexeme.getBegin() >= this.pathBegin && lexeme.getBegin() < this.pathEnd || this.pathBegin >= lexeme.getBegin() && this.pathBegin < lexeme.getBegin() + lexeme.getLength();
    }

    int getPathBegin() {
        return this.pathBegin;
    }

    int getPathEnd() {
        return this.pathEnd;
    }

    int getPayloadLength() {
        return this.payloadLength;
    }

    int getPathLength() {
        return this.pathEnd - this.pathBegin;
    }

    int getXWeight() {
        int product = 1;

        for(Cell c = this.getHead(); c != null && c.getLexeme() != null; c = c.getNext()) {
            product *= c.getLexeme().getLength();
        }

        return product;
    }

    int getPWeight() {
        int pWeight = 0;
        int p = 0;

        for(Cell c = this.getHead(); c != null && c.getLexeme() != null; c = c.getNext()) {
            ++p;
            pWeight += p * c.getLexeme().getLength();
        }

        return pWeight;
    }

    LexemePath copy() {
        LexemePath theCopy = new LexemePath();
        theCopy.pathBegin = this.pathBegin;
        theCopy.pathEnd = this.pathEnd;
        theCopy.payloadLength = this.payloadLength;

        for(Cell c = this.getHead(); c != null && c.getLexeme() != null; c = c.getNext()) {
            theCopy.addLexeme(c.getLexeme());
        }

        return theCopy;
    }

    public int compareTo(LexemePath o) {
        return this.payloadLength > o.payloadLength?-1:(this.payloadLength < o.payloadLength?1:(this.size() < o.size()?-1:(this.size() > o.size()?1:(this.getPathLength() > o.getPathLength()?-1:(this.getPathLength() < o.getPathLength()?1:(this.pathEnd > o.pathEnd?-1:(this.pathEnd < o.pathEnd?1:(this.getXWeight() > o.getXWeight()?-1:(this.getXWeight() < o.getXWeight()?1:(this.getPWeight() > o.getPWeight()?-1:(this.getPWeight() < o.getPWeight()?1:0)))))))))));
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("pathBegin  : ").append(this.pathBegin).append("\r\n");
        sb.append("pathEnd  : ").append(this.pathEnd).append("\r\n");
        sb.append("payloadLength  : ").append(this.payloadLength).append("\r\n");

        for(Cell head = this.getHead(); head != null; head = head.getNext()) {
            sb.append("lexeme : ").append(head.getLexeme()).append("\r\n");
        }

        return sb.toString();
    }
}
