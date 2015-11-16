package com.ik.analyzer.cfg;

/**
 * Created by aki on 11/16/15.
 */
import java.util.List;

public interface Configuration {
    boolean useSmart();

    void setUseSmart(boolean var1);

    String getMainDictionary();

    String getQuantifierDicionary();

    List<String> getExtDictionarys();

    List<String> getExtStopWordDictionarys();
}
