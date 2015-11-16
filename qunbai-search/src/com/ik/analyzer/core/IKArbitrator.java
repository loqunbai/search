package com.ik.analyzer.core;

import java.util.Stack;
import java.util.TreeSet;
import com.ik.analyzer.core.AnalyzeContext;
import com.ik.analyzer.core.Lexeme;
import com.ik.analyzer.core.LexemePath;
import com.ik.analyzer.core.QuickSortSet;
import com.ik.analyzer.core.QuickSortSet.Cell;

class IKArbitrator {
    IKArbitrator() {
    }

    void process(AnalyzeContext context, boolean useSmart) {
        QuickSortSet orgLexemes = context.getOrgLexemes();
        Lexeme orgLexeme = orgLexemes.pollFirst();

        LexemePath crossPath;
        Cell headCell;
        LexemePath judgeResult;
        for(crossPath = new LexemePath(); orgLexeme != null; orgLexeme = orgLexemes.pollFirst()) {
            if(!crossPath.addCrossLexeme(orgLexeme)) {
                if(crossPath.size() != 1 && useSmart) {
                    headCell = crossPath.getHead();
                    judgeResult = this.judge(headCell, crossPath.getPathLength());
                    context.addLexemePath(judgeResult);
                } else {
                    context.addLexemePath(crossPath);
                }

                crossPath = new LexemePath();
                crossPath.addCrossLexeme(orgLexeme);
            }
        }

        if(crossPath.size() != 1 && useSmart) {
            headCell = crossPath.getHead();
            judgeResult = this.judge(headCell, crossPath.getPathLength());
            context.addLexemePath(judgeResult);
        } else {
            context.addLexemePath(crossPath);
        }

    }

    private LexemePath judge(Cell lexemeCell, int fullTextLength) {
        TreeSet pathOptions = new TreeSet();
        LexemePath option = new LexemePath();
        Stack lexemeStack = this.forwardPath(lexemeCell, option);
        pathOptions.add(option.copy());
        Cell c = null;

        while(!lexemeStack.isEmpty()) {
            c = (Cell)lexemeStack.pop();
            this.backPath(c.getLexeme(), option);
            this.forwardPath(c, option);
            pathOptions.add(option.copy());
        }

        return (LexemePath)pathOptions.first();
    }

    private Stack<Cell> forwardPath(Cell lexemeCell, LexemePath option) {
        Stack conflictStack = new Stack();

        for(Cell c = lexemeCell; c != null && c.getLexeme() != null; c = c.getNext()) {
            if(!option.addNotCrossLexeme(c.getLexeme())) {
                conflictStack.push(c);
            }
        }

        return conflictStack;
    }

    private void backPath(Lexeme l, LexemePath option) {
        while(option.checkCross(l)) {
            option.removeTail();
        }

    }
}