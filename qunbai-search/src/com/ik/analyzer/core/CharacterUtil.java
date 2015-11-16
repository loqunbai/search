
package com.ik.analyzer.core;

import java.lang.Character.UnicodeBlock;

class CharacterUtil {
    public static final int CHAR_USELESS = 0;
    public static final int CHAR_ARABIC = 1;
    public static final int CHAR_ENGLISH = 2;
    public static final int CHAR_CHINESE = 4;
    public static final int CHAR_OTHER_CJK = 8;

    CharacterUtil() {
    }

    static int identifyCharType(char input) {
        if(input >= 48 && input <= 57) {
            return 1;
        } else if(input >= 97 && input <= 122 || input >= 65 && input <= 90) {
            return 2;
        } else {
            UnicodeBlock ub = UnicodeBlock.of(input);
            return ub != UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS && ub != UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS && ub != UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A?(ub != UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS && ub != UnicodeBlock.HANGUL_SYLLABLES && ub != UnicodeBlock.HANGUL_JAMO && ub != UnicodeBlock.HANGUL_COMPATIBILITY_JAMO && ub != UnicodeBlock.HIRAGANA && ub != UnicodeBlock.KATAKANA && ub != UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS?0:8):4;
        }
    }

    static char regularize(char input) {
        if(input == 12288) {
            input = 32;
        } else if(input > '\uff00' && input < '｟') {
            input -= 'ﻠ';
        } else if(input >= 65 && input <= 90) {
            input = (char)(input + 32);
        }

        return input;
    }
}
