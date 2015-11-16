//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.core;

import com.ik.analyzer.core.Lexeme;

class QuickSortSet {
    private QuickSortSet.Cell head;
    private QuickSortSet.Cell tail;
    private int size = 0;

    QuickSortSet() {
    }

    boolean addLexeme(Lexeme lexeme) {
        QuickSortSet.Cell newCell = new QuickSortSet.Cell(lexeme);
        if(this.size == 0) {
            this.head = newCell;
            this.tail = newCell;
            ++this.size;
            return true;
        } else if(this.tail.compareTo(newCell) == 0) {
            return false;
        } else if(this.tail.compareTo(newCell) < 0) {
            this.tail.next = newCell;
            newCell.prev = this.tail;
            this.tail = newCell;
            ++this.size;
            return true;
        } else if(this.head.compareTo(newCell) > 0) {
            this.head.prev = newCell;
            newCell.next = this.head;
            this.head = newCell;
            ++this.size;
            return true;
        } else {
            QuickSortSet.Cell index;
            for(index = this.tail; index != null && index.compareTo(newCell) > 0; index = index.prev) {
                ;
            }

            if(index.compareTo(newCell) == 0) {
                return false;
            } else if(index.compareTo(newCell) < 0) {
                newCell.prev = index;
                newCell.next = index.next;
                index.next.prev = newCell;
                index.next = newCell;
                ++this.size;
                return true;
            } else {
                return false;
            }
        }
    }

    Lexeme peekFirst() {
        return this.head != null?this.head.lexeme:null;
    }

    Lexeme pollFirst() {
        Lexeme first;
        if(this.size == 1) {
            first = this.head.lexeme;
            this.head = null;
            this.tail = null;
            --this.size;
            return first;
        } else if(this.size > 1) {
            first = this.head.lexeme;
            this.head = this.head.next;
            --this.size;
            return first;
        } else {
            return null;
        }
    }

    Lexeme peekLast() {
        return this.tail != null?this.tail.lexeme:null;
    }

    Lexeme pollLast() {
        Lexeme last;
        if(this.size == 1) {
            last = this.head.lexeme;
            this.head = null;
            this.tail = null;
            --this.size;
            return last;
        } else if(this.size > 1) {
            last = this.tail.lexeme;
            this.tail = this.tail.prev;
            --this.size;
            return last;
        } else {
            return null;
        }
    }

    int size() {
        return this.size;
    }

    boolean isEmpty() {
        return this.size == 0;
    }

    QuickSortSet.Cell getHead() {
        return this.head;
    }

    class Cell implements Comparable<QuickSortSet.Cell> {
        private QuickSortSet.Cell prev;
        private QuickSortSet.Cell next;
        private Lexeme lexeme;

        Cell(Lexeme lexeme) {
            if(lexeme == null) {
                throw new IllegalArgumentException("lexeme must not be null");
            } else {
                this.lexeme = lexeme;
            }
        }

        public int compareTo(QuickSortSet.Cell o) {
            return this.lexeme.compareTo(o.lexeme);
        }

        public QuickSortSet.Cell getPrev() {
            return this.prev;
        }

        public QuickSortSet.Cell getNext() {
            return this.next;
        }

        public Lexeme getLexeme() {
            return this.lexeme;
        }
    }
}
