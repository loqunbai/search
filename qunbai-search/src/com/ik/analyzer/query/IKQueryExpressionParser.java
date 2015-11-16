//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.BytesRef;
import com.ik.analyzer.query.SWMCQueryBuilder;

public class IKQueryExpressionParser {
    private List<IKQueryExpressionParser.Element> elements = new ArrayList();
    private Stack<Query> querys = new Stack();
    private Stack<IKQueryExpressionParser.Element> operates = new Stack();

    public IKQueryExpressionParser() {
    }

    public Query parseExp(String expression, boolean quickMode) {
        Query lucenceQuery = null;
        if(expression != null && !"".equals(expression.trim())) {
            try {
                this.splitElements(expression);
                this.parseSyntax(quickMode);
                if(this.querys.size() != 1) {
                    throw new IllegalStateException("表达式异常： 缺少逻辑操作符 或 括号缺失");
                }

                lucenceQuery = (Query)this.querys.pop();
            } finally {
                this.elements.clear();
                this.querys.clear();
                this.operates.clear();
            }
        }

        return lucenceQuery;
    }

    private void splitElements(String expression) {
        if(expression != null) {
            IKQueryExpressionParser.Element curretElement = null;
            char[] expChars = expression.toCharArray();

            for(int i = 0; i < expChars.length; ++i) {
                switch(expChars[i]) {
                case ' ':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                        } else {
                            this.elements.add(curretElement);
                            curretElement = null;
                        }
                    }
                    break;
                case '&':
                    if(curretElement == null) {
                        curretElement = new IKQueryExpressionParser.Element();
                        curretElement.type = 38;
                        curretElement.append(expChars[i]);
                    } else if(curretElement.type == 38) {
                        curretElement.append(expChars[i]);
                        this.elements.add(curretElement);
                        curretElement = null;
                    } else if(curretElement.type == 39) {
                        curretElement.append(expChars[i]);
                    } else {
                        this.elements.add(curretElement);
                        curretElement = new IKQueryExpressionParser.Element();
                        curretElement.type = 38;
                        curretElement.append(expChars[i]);
                    }
                    break;
                case '\'':
                    if(curretElement == null) {
                        curretElement = new IKQueryExpressionParser.Element();
                        curretElement.type = 39;
                    } else if(curretElement.type == 39) {
                        this.elements.add(curretElement);
                        curretElement = null;
                    } else {
                        this.elements.add(curretElement);
                        curretElement = new IKQueryExpressionParser.Element();
                        curretElement.type = 39;
                    }
                    break;
                case '(':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 40;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case ')':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 41;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case ',':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 44;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case '-':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 45;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case ':':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 58;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case '=':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 61;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case '[':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 91;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case ']':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 93;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case '{':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 123;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                case '|':
                    if(curretElement == null) {
                        curretElement = new IKQueryExpressionParser.Element();
                        curretElement.type = 124;
                        curretElement.append(expChars[i]);
                    } else if(curretElement.type == 124) {
                        curretElement.append(expChars[i]);
                        this.elements.add(curretElement);
                        curretElement = null;
                    } else if(curretElement.type == 39) {
                        curretElement.append(expChars[i]);
                    } else {
                        this.elements.add(curretElement);
                        curretElement = new IKQueryExpressionParser.Element();
                        curretElement.type = 124;
                        curretElement.append(expChars[i]);
                    }
                    break;
                case '}':
                    if(curretElement != null) {
                        if(curretElement.type == 39) {
                            curretElement.append(expChars[i]);
                            break;
                        }

                        this.elements.add(curretElement);
                    }

                    curretElement = new IKQueryExpressionParser.Element();
                    curretElement.type = 125;
                    curretElement.append(expChars[i]);
                    this.elements.add(curretElement);
                    curretElement = null;
                    break;
                default:
                    if(curretElement == null) {
                        curretElement = new IKQueryExpressionParser.Element();
                        curretElement.type = 70;
                        curretElement.append(expChars[i]);
                    } else if(curretElement.type == 70) {
                        curretElement.append(expChars[i]);
                    } else if(curretElement.type == 39) {
                        curretElement.append(expChars[i]);
                    } else {
                        this.elements.add(curretElement);
                        curretElement = new IKQueryExpressionParser.Element();
                        curretElement.type = 70;
                        curretElement.append(expChars[i]);
                    }
                }
            }

            if(curretElement != null) {
                this.elements.add(curretElement);
                curretElement = null;
            }

        }
    }

    private void parseSyntax(boolean quickMode) {
        for(int eleOnTop = 0; eleOnTop < this.elements.size(); ++eleOnTop) {
            IKQueryExpressionParser.Element q = (IKQueryExpressionParser.Element)this.elements.get(eleOnTop);
            IKQueryExpressionParser.Element eleOnTop1;
            if(70 == q.type) {
                IKQueryExpressionParser.Element var10 = (IKQueryExpressionParser.Element)this.elements.get(eleOnTop + 1);
                if(61 != var10.type && 58 != var10.type) {
                    throw new IllegalStateException("表达式异常： = 或 ： 号丢失");
                }

                eleOnTop1 = (IKQueryExpressionParser.Element)this.elements.get(eleOnTop + 2);
                if(39 == eleOnTop1.type) {
                    eleOnTop += 2;
                    if(61 == var10.type) {
                        TermQuery var12 = new TermQuery(new Term(q.toString(), eleOnTop1.toString()));
                        this.querys.push(var12);
                    } else if(58 == var10.type) {
                        String var14 = eleOnTop1.toString();
                        Query var15 = SWMCQueryBuilder.create(q.toString(), var14, quickMode);
                        this.querys.push(var15);
                    }
                } else {
                    if(91 != eleOnTop1.type && 123 != eleOnTop1.type) {
                        throw new IllegalStateException("表达式异常：匹配值丢失");
                    }

                    eleOnTop += 2;
                    LinkedList var11 = new LinkedList();
                    var11.add(eleOnTop1);
                    ++eleOnTop;

                    while(eleOnTop < this.elements.size()) {
                        IKQueryExpressionParser.Element rangeQuery = (IKQueryExpressionParser.Element)this.elements.get(eleOnTop);
                        var11.add(rangeQuery);
                        if(93 == rangeQuery.type || 125 == rangeQuery.type) {
                            break;
                        }

                        ++eleOnTop;
                    }

                    TermRangeQuery var13 = this.toTermRangeQuery(q, var11);
                    this.querys.push(var13);
                }
            } else if(40 == q.type) {
                this.operates.push(q);
            } else {
                boolean doPeek;
                Query q1;
                if(41 == q.type) {
                    doPeek = true;

                    while(doPeek && !this.operates.empty()) {
                        eleOnTop1 = (IKQueryExpressionParser.Element)this.operates.pop();
                        if(40 == eleOnTop1.type) {
                            doPeek = false;
                        } else {
                            q1 = this.toBooleanQuery(eleOnTop1);
                            this.querys.push(q1);
                        }
                    }
                } else if(this.operates.isEmpty()) {
                    this.operates.push(q);
                } else {
                    doPeek = true;

                    while(doPeek && !this.operates.isEmpty()) {
                        eleOnTop1 = (IKQueryExpressionParser.Element)this.operates.peek();
                        if(40 == eleOnTop1.type) {
                            doPeek = false;
                            this.operates.push(q);
                        } else if(this.compare(q, eleOnTop1) == 1) {
                            this.operates.push(q);
                            doPeek = false;
                        } else if(this.compare(q, eleOnTop1) == 0) {
                            q1 = this.toBooleanQuery(eleOnTop1);
                            this.operates.pop();
                            this.querys.push(q1);
                        } else {
                            q1 = this.toBooleanQuery(eleOnTop1);
                            this.operates.pop();
                            this.querys.push(q1);
                        }
                    }

                    if(doPeek && this.operates.empty()) {
                        this.operates.push(q);
                    }
                }
            }
        }

        while(!this.operates.isEmpty()) {
            IKQueryExpressionParser.Element var9 = (IKQueryExpressionParser.Element)this.operates.pop();
            Query var8 = this.toBooleanQuery(var9);
            this.querys.push(var8);
        }

    }

    private Query toBooleanQuery(IKQueryExpressionParser.Element op) {
        if(this.querys.size() == 0) {
            return null;
        } else {
            BooleanQuery resultQuery = new BooleanQuery();
            if(this.querys.size() == 1) {
                return (Query)this.querys.get(0);
            } else {
                Query q2 = (Query)this.querys.pop();
                Query q1 = (Query)this.querys.pop();
                BooleanClause[] clauses;
                BooleanClause c;
                int var7;
                int var8;
                BooleanClause[] var9;
                if(38 == op.type) {
                    if(q1 != null) {
                        if(q1 instanceof BooleanQuery) {
                            clauses = ((BooleanQuery)q1).getClauses();
                            if(clauses.length > 0 && clauses[0].getOccur() == Occur.MUST) {
                                var9 = clauses;
                                var8 = clauses.length;

                                for(var7 = 0; var7 < var8; ++var7) {
                                    c = var9[var7];
                                    resultQuery.add(c);
                                }
                            } else {
                                resultQuery.add(q1, Occur.MUST);
                            }
                        } else {
                            resultQuery.add(q1, Occur.MUST);
                        }
                    }

                    if(q2 != null) {
                        if(q2 instanceof BooleanQuery) {
                            clauses = ((BooleanQuery)q2).getClauses();
                            if(clauses.length > 0 && clauses[0].getOccur() == Occur.MUST) {
                                var9 = clauses;
                                var8 = clauses.length;

                                for(var7 = 0; var7 < var8; ++var7) {
                                    c = var9[var7];
                                    resultQuery.add(c);
                                }
                            } else {
                                resultQuery.add(q2, Occur.MUST);
                            }
                        } else {
                            resultQuery.add(q2, Occur.MUST);
                        }
                    }
                } else if(124 == op.type) {
                    if(q1 != null) {
                        if(q1 instanceof BooleanQuery) {
                            clauses = ((BooleanQuery)q1).getClauses();
                            if(clauses.length > 0 && clauses[0].getOccur() == Occur.SHOULD) {
                                var9 = clauses;
                                var8 = clauses.length;

                                for(var7 = 0; var7 < var8; ++var7) {
                                    c = var9[var7];
                                    resultQuery.add(c);
                                }
                            } else {
                                resultQuery.add(q1, Occur.SHOULD);
                            }
                        } else {
                            resultQuery.add(q1, Occur.SHOULD);
                        }
                    }

                    if(q2 != null) {
                        if(q2 instanceof BooleanQuery) {
                            clauses = ((BooleanQuery)q2).getClauses();
                            if(clauses.length > 0 && clauses[0].getOccur() == Occur.SHOULD) {
                                var9 = clauses;
                                var8 = clauses.length;

                                for(var7 = 0; var7 < var8; ++var7) {
                                    c = var9[var7];
                                    resultQuery.add(c);
                                }
                            } else {
                                resultQuery.add(q2, Occur.SHOULD);
                            }
                        } else {
                            resultQuery.add(q2, Occur.SHOULD);
                        }
                    }
                } else if(45 == op.type) {
                    if(q1 == null || q2 == null) {
                        throw new IllegalStateException("表达式异常：SubQuery 个数不匹配");
                    }

                    if(q1 instanceof BooleanQuery) {
                        clauses = ((BooleanQuery)q1).getClauses();
                        if(clauses.length > 0) {
                            var9 = clauses;
                            var8 = clauses.length;

                            for(var7 = 0; var7 < var8; ++var7) {
                                c = var9[var7];
                                resultQuery.add(c);
                            }
                        } else {
                            resultQuery.add(q1, Occur.MUST);
                        }
                    } else {
                        resultQuery.add(q1, Occur.MUST);
                    }

                    resultQuery.add(q2, Occur.MUST_NOT);
                }

                return resultQuery;
            }
        }
    }

    private TermRangeQuery toTermRangeQuery(IKQueryExpressionParser.Element fieldNameEle, LinkedList<IKQueryExpressionParser.Element> elements) {
        boolean includeFirst = false;
        boolean includeLast = false;
        String firstValue = null;
        String lastValue = null;
        IKQueryExpressionParser.Element first = (IKQueryExpressionParser.Element)elements.getFirst();
        if(91 == first.type) {
            includeFirst = true;
        } else {
            if(123 != first.type) {
                throw new IllegalStateException("表达式异常");
            }

            includeFirst = false;
        }

        IKQueryExpressionParser.Element last = (IKQueryExpressionParser.Element)elements.getLast();
        if(93 == last.type) {
            includeLast = true;
        } else {
            if(125 != last.type) {
                throw new IllegalStateException("表达式异常, RangeQuery缺少结束括号");
            }

            includeLast = false;
        }

        if(elements.size() >= 4 && elements.size() <= 5) {
            IKQueryExpressionParser.Element e2 = (IKQueryExpressionParser.Element)elements.get(1);
            IKQueryExpressionParser.Element e3;
            if(39 == e2.type) {
                firstValue = e2.toString();
                e3 = (IKQueryExpressionParser.Element)elements.get(2);
                if(44 != e3.type) {
                    throw new IllegalStateException("表达式异常, RangeQuery缺少逗号分隔");
                }

                IKQueryExpressionParser.Element e4 = (IKQueryExpressionParser.Element)elements.get(3);
                if(39 == e4.type) {
                    lastValue = e4.toString();
                } else if(e4 != last) {
                    throw new IllegalStateException("表达式异常，RangeQuery格式错误");
                }
            } else {
                if(44 != e2.type) {
                    throw new IllegalStateException("表达式异常, RangeQuery格式错误");
                }

                firstValue = null;
                e3 = (IKQueryExpressionParser.Element)elements.get(2);
                if(39 != e3.type) {
                    throw new IllegalStateException("表达式异常，RangeQuery格式错误");
                }

                lastValue = e3.toString();
            }

            return new TermRangeQuery(fieldNameEle.toString(), new BytesRef(firstValue), new BytesRef(lastValue), includeFirst, includeLast);
        } else {
            throw new IllegalStateException("表达式异常, RangeQuery 错误");
        }
    }

    private int compare(IKQueryExpressionParser.Element e1, IKQueryExpressionParser.Element e2) {
        return 38 == e1.type?(38 == e2.type?0:1):(124 == e1.type?(38 == e2.type?-1:(124 == e2.type?0:1)):(45 == e2.type?0:-1));
    }

    public static void main(String[] args) {
        IKQueryExpressionParser parser = new IKQueryExpressionParser();
        String ikQueryExp = "(id=\'ABcdRf\' && date:{\'20010101\',\'20110101\'} && keyword:\'魔兽中国\') || (content:\'KSHT-KSH-A001-18\'  || ulr=\'www.ik.com\') - name:\'林良益\'";
        Query result = parser.parseExp(ikQueryExp, true);
        System.out.println(result);
    }

    private class Element {
        char type = 0;
        StringBuffer eleTextBuff = new StringBuffer();

        public Element() {
        }

        public void append(char c) {
            this.eleTextBuff.append(c);
        }

        public String toString() {
            return this.eleTextBuff.toString();
        }
    }
}
