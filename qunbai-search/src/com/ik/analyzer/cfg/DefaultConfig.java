package com.ik.analyzer.cfg;

/**
 * Created by aki on 11/16/15.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import com.ik.analyzer.cfg.Configuration;

public class DefaultConfig implements Configuration {
    private static final String PATH_DIC_MAIN = "com/ik/analyzer/dic/main2012.dic";
    private static final String PATH_DIC_QUANTIFIER = "com/ik/analyzer/dic/quantifier.dic";
    private static final String FILE_NAME = "IKAnalyzer.cfg.xml";
    private static final String EXT_DICT = "ext_dict";
    private static final String EXT_STOP = "ext_stopwords";
    private Properties props = new Properties();
    private boolean useSmart;

    public static Configuration getInstance() {
        return new DefaultConfig();
    }

    private DefaultConfig() {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("IKAnalyzer.cfg.xml");
        if(input != null) {
            try {
                this.props.loadFromXML(input);
            } catch (InvalidPropertiesFormatException var3) {
                var3.printStackTrace();
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

    }

    public boolean useSmart() {
        return this.useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public String getMainDictionary() {
        return "com/ik/analyzer/dic/main2012.dic";
    }

    public String getQuantifierDicionary() {
        return "com/ik/analyzer/dic/quantifier.dic";
    }

    public List<String> getExtDictionarys() {
        ArrayList extDictFiles = new ArrayList(2);
        String extDictCfg = this.props.getProperty("ext_dict");
        if(extDictCfg != null) {
            String[] filePaths = extDictCfg.split(";");
            if(filePaths != null) {
                String[] var7 = filePaths;
                int var6 = filePaths.length;

                for(int var5 = 0; var5 < var6; ++var5) {
                    String filePath = var7[var5];
                    if(filePath != null && !"".equals(filePath.trim())) {
                        extDictFiles.add(filePath.trim());
                    }
                }
            }
        }

        return extDictFiles;
    }

    public List<String> getExtStopWordDictionarys() {
        ArrayList extStopWordDictFiles = new ArrayList(2);
        String extStopWordDictCfg = this.props.getProperty("ext_stopwords");
        if(extStopWordDictCfg != null) {
            String[] filePaths = extStopWordDictCfg.split(";");
            if(filePaths != null) {
                String[] var7 = filePaths;
                int var6 = filePaths.length;

                for(int var5 = 0; var5 < var6; ++var5) {
                    String filePath = var7[var5];
                    if(filePath != null && !"".equals(filePath.trim())) {
                        extStopWordDictFiles.add(filePath.trim());
                    }
                }
            }
        }

        return extStopWordDictFiles;
    }
}

