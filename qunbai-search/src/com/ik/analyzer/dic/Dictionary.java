//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.ik.analyzer.cfg.Configuration;
import com.ik.analyzer.dic.DictSegment;
import com.ik.analyzer.dic.Hit;

public class Dictionary {
    private static Dictionary singleton;
    private DictSegment _MainDict;
    private DictSegment _StopWordDict;
    private DictSegment _QuantifierDict;
    private Configuration cfg;

    private Dictionary(Configuration cfg) {
        this.cfg = cfg;
        this.loadMainDict();
        this.loadStopWordDict();
        this.loadQuantifierDict();
    }

    public static Dictionary initial(Configuration cfg) {
        if(singleton == null) {
            Class var1 = Dictionary.class;
            synchronized(Dictionary.class) {
                if(singleton == null) {
                    singleton = new Dictionary(cfg);
                    return singleton;
                }
            }
        }

        return singleton;
    }

    public static Dictionary getSingleton() {
        if(singleton == null) {
            throw new IllegalStateException("词典尚未初始化，请先调用initial方法");
        } else {
            return singleton;
        }
    }

    public void addWords(Collection<String> words) {
        if(words != null) {
            Iterator var3 = words.iterator();

            while(var3.hasNext()) {
                String word = (String)var3.next();
                if(word != null) {
                    singleton._MainDict.fillSegment(word.trim().toLowerCase().toCharArray());
                }
            }
        }

    }

    public void disableWords(Collection<String> words) {
        if(words != null) {
            Iterator var3 = words.iterator();

            while(var3.hasNext()) {
                String word = (String)var3.next();
                if(word != null) {
                    singleton._MainDict.disableSegment(word.trim().toLowerCase().toCharArray());
                }
            }
        }

    }

    public Hit matchInMainDict(char[] charArray) {
        return singleton._MainDict.match(charArray);
    }

    public Hit matchInMainDict(char[] charArray, int begin, int length) {
        return singleton._MainDict.match(charArray, begin, length);
    }

    public Hit matchInQuantifierDict(char[] charArray, int begin, int length) {
        return singleton._QuantifierDict.match(charArray, begin, length);
    }

    public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit) {
        DictSegment ds = matchedHit.getMatchedDictSegment();
        return ds.match(charArray, currentIndex, 1, matchedHit);
    }

    public boolean isStopWord(char[] charArray, int begin, int length) {
        return singleton._StopWordDict.match(charArray, begin, length).isMatch();
    }

    private void loadMainDict() {
        this._MainDict = new DictSegment(Character.valueOf('\u0000'));
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.cfg.getMainDictionary());
        if(is == null) {
            throw new RuntimeException("Main Dictionary not found!!!");
        } else {
            try {
                BufferedReader ioe = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
                String theWord = null;

                do {
                    theWord = ioe.readLine();
                    if(theWord != null && !"".equals(theWord.trim())) {
                        this._MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                    }
                } while(theWord != null);
            } catch (IOException var12) {
                System.err.println("Main Dictionary loading exception.");
                var12.printStackTrace();
            } finally {
                try {
                    if(is != null) {
                        is.close();
                        is = null;
                    }
                } catch (IOException var11) {
                    var11.printStackTrace();
                }

            }

            this.loadExtDict();
        }
    }

    private void loadExtDict() {
        List extDictFiles = this.cfg.getExtDictionarys();
        if(extDictFiles != null) {
            InputStream is = null;
            Iterator var4 = extDictFiles.iterator();

            while(true) {
                do {
                    if(!var4.hasNext()) {
                        return;
                    }

                    String extDictName = (String)var4.next();
                    System.out.println("加载扩展词典：" + extDictName);
                    is = this.getClass().getClassLoader().getResourceAsStream(extDictName);
                } while(is == null);

                try {
                    BufferedReader ioe = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
                    String theWord = null;

                    while(true) {
                        theWord = ioe.readLine();
                        if(theWord != null && !"".equals(theWord.trim())) {
                            this._MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                        }

                        if(theWord == null) {
                            break;
                        }
                    }
                } catch (IOException var15) {
                    System.err.println("Extension Dictionary loading exception.");
                    var15.printStackTrace();
                } finally {
                    try {
                        if(is != null) {
                            is.close();
                            is = null;
                        }
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }

                }
            }
        }
    }

    private void loadStopWordDict() {
        this._StopWordDict = new DictSegment(Character.valueOf('\u0000'));
        List extStopWordDictFiles = this.cfg.getExtStopWordDictionarys();
        if(extStopWordDictFiles != null) {
            InputStream is = null;
            Iterator var4 = extStopWordDictFiles.iterator();

            while(true) {
                do {
                    if(!var4.hasNext()) {
                        return;
                    }

                    String extStopWordDictName = (String)var4.next();
                    System.out.println("加载扩展停止词典：" + extStopWordDictName);
                    is = this.getClass().getClassLoader().getResourceAsStream(extStopWordDictName);
                } while(is == null);

                try {
                    BufferedReader ioe = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
                    String theWord = null;

                    while(true) {
                        theWord = ioe.readLine();
                        if(theWord != null && !"".equals(theWord.trim())) {
                            this._StopWordDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                        }

                        if(theWord == null) {
                            break;
                        }
                    }
                } catch (IOException var15) {
                    System.err.println("Extension Stop word Dictionary loading exception.");
                    var15.printStackTrace();
                } finally {
                    try {
                        if(is != null) {
                            is.close();
                            is = null;
                        }
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }

                }
            }
        }
    }

    private void loadQuantifierDict() {
        this._QuantifierDict = new DictSegment(Character.valueOf('\u0000'));
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.cfg.getQuantifierDicionary());
        if(is == null) {
            throw new RuntimeException("Quantifier Dictionary not found!!!");
        } else {
            try {
                BufferedReader ioe = new BufferedReader(new InputStreamReader(is, "UTF-8"), 512);
                String theWord = null;

                do {
                    theWord = ioe.readLine();
                    if(theWord != null && !"".equals(theWord.trim())) {
                        this._QuantifierDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
                    }
                } while(theWord != null);
            } catch (IOException var12) {
                System.err.println("Quantifier Dictionary loading exception.");
                var12.printStackTrace();
            } finally {
                try {
                    if(is != null) {
                        is.close();
                        is = null;
                    }
                } catch (IOException var11) {
                    var11.printStackTrace();
                }

            }

        }
    }
}
