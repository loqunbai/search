//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.dic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.ik.analyzer.dic.Hit;

class DictSegment implements Comparable<DictSegment> {
    private static final Map<Character, Character> charMap = new HashMap(16, 0.95F);
    private static final int ARRAY_LENGTH_LIMIT = 3;
    private Map<Character, DictSegment> childrenMap;
    private DictSegment[] childrenArray;
    private Character nodeChar;
    private int storeSize = 0;
    private int nodeState = 0;

    DictSegment(Character nodeChar) {
        if(nodeChar == null) {
            throw new IllegalArgumentException("参数为空异常，字符不能为空");
        } else {
            this.nodeChar = nodeChar;
        }
    }

    Character getNodeChar() {
        return this.nodeChar;
    }

    boolean hasNextNode() {
        return this.storeSize > 0;
    }

    Hit match(char[] charArray) {
        return this.match(charArray, 0, charArray.length, (Hit)null);
    }

    Hit match(char[] charArray, int begin, int length) {
        return this.match(charArray, begin, length, (Hit)null);
    }

    Hit match(char[] charArray, int begin, int length, Hit searchHit) {
        if(searchHit == null) {
            searchHit = new Hit();
            searchHit.setBegin(begin);
        } else {
            searchHit.setUnmatch();
        }

        searchHit.setEnd(begin);
        Character keyChar = new Character(charArray[begin]);
        DictSegment ds = null;
        DictSegment[] segmentArray = this.childrenArray;
        Map segmentMap = this.childrenMap;
        if(segmentArray != null) {
            DictSegment keySegment = new DictSegment(keyChar);
            int position = Arrays.binarySearch(segmentArray, 0, this.storeSize, keySegment);
            if(position >= 0) {
                ds = segmentArray[position];
            }
        } else if(segmentMap != null) {
            ds = (DictSegment)segmentMap.get(keyChar);
        }

        if(ds != null) {
            if(length > 1) {
                return ds.match(charArray, begin + 1, length - 1, searchHit);
            }

            if(length == 1) {
                if(ds.nodeState == 1) {
                    searchHit.setMatch();
                }

                if(ds.hasNextNode()) {
                    searchHit.setPrefix();
                    searchHit.setMatchedDictSegment(ds);
                }

                return searchHit;
            }
        }

        return searchHit;
    }

    void fillSegment(char[] charArray) {
        this.fillSegment(charArray, 0, charArray.length, 1);
    }

    void disableSegment(char[] charArray) {
        this.fillSegment(charArray, 0, charArray.length, 0);
    }

    private synchronized void fillSegment(char[] charArray, int begin, int length, int enabled) {
        Character beginChar = new Character(charArray[begin]);
        Character keyChar = (Character)charMap.get(beginChar);
        if(keyChar == null) {
            charMap.put(beginChar, beginChar);
            keyChar = beginChar;
        }

        DictSegment ds = this.lookforSegment(keyChar, enabled);
        if(ds != null) {
            if(length > 1) {
                ds.fillSegment(charArray, begin + 1, length - 1, enabled);
            } else if(length == 1) {
                ds.nodeState = enabled;
            }
        }

    }

    private DictSegment lookforSegment(Character keyChar, int create) {
        DictSegment ds = null;
        if(this.storeSize <= 3) {
            DictSegment[] segmentMap = this.getChildrenArray();
            DictSegment keySegment = new DictSegment(keyChar);
            int position = Arrays.binarySearch(segmentMap, 0, this.storeSize, keySegment);
            if(position >= 0) {
                ds = segmentMap[position];
            }

            if(ds == null && create == 1) {
                ds = keySegment;
                if(this.storeSize < 3) {
                    segmentMap[this.storeSize] = keySegment;
                    ++this.storeSize;
                    Arrays.sort(segmentMap, 0, this.storeSize);
                } else {
                    Map segmentMap1 = this.getChildrenMap();
                    this.migrate(segmentMap, segmentMap1);
                    segmentMap1.put(keyChar, keySegment);
                    ++this.storeSize;
                    this.childrenArray = null;
                }
            }
        } else {
            Map var8 = this.getChildrenMap();
            ds = (DictSegment)var8.get(keyChar);
            if(ds == null && create == 1) {
                ds = new DictSegment(keyChar);
                var8.put(keyChar, ds);
                ++this.storeSize;
            }
        }

        return ds;
    }

    private DictSegment[] getChildrenArray() {
        if(this.childrenArray == null) {
            synchronized(this) {
                if(this.childrenArray == null) {
                    this.childrenArray = new DictSegment[3];
                }
            }
        }

        return this.childrenArray;
    }

    private Map<Character, DictSegment> getChildrenMap() {
        if(this.childrenMap == null) {
            synchronized(this) {
                if(this.childrenMap == null) {
                    this.childrenMap = new HashMap(6, 0.8F);
                }
            }
        }

        return this.childrenMap;
    }

    private void migrate(DictSegment[] segmentArray, Map<Character, DictSegment> segmentMap) {
        DictSegment[] var6 = segmentArray;
        int var5 = segmentArray.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            DictSegment segment = var6[var4];
            if(segment != null) {
                segmentMap.put(segment.nodeChar, segment);
            }
        }

    }

    public int compareTo(DictSegment o) {
        return this.nodeChar.compareTo(o.nodeChar);
    }
}
