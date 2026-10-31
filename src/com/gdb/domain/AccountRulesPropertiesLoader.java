package com.gdb.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AccountRulesPropertiesLoader {
    private final Properties properties = new Properties();
    private String loadedPath;

    public AccountRulesPropertiesLoader() {
    }

    public AccountRulesPropertiesLoader(String filePath) {
        loadProperties(filePath);
    }

    public boolean loadProperties(String filePath) {
        this.loadedPath = filePath;
        
        File file = new File(filePath);
        if (file.exists()) {
            try (InputStream is = new FileInputStream(file)) {
                properties.load(is);
                return true;
            } catch (IOException e) {
            }
        }

        String[] fallbackPaths = new String[] {
            filePath,
            "bin/" + (filePath.startsWith("src/main/resources/") ? filePath.substring("src/main/resources/".length()) : filePath),
            filePath.startsWith("src/main/resources/") ? filePath.substring("src/main/resources/".length()) : filePath,
            "src/main/resources/" + filePath
        };

        for (String p : fallbackPaths) {
            File f = new File(p);
            if (f.exists()) {
                try (InputStream is = new FileInputStream(f)) {
                    properties.load(is);
                    this.loadedPath = p;
                    return true;
                } catch (IOException e) {
                }
            }
        }

        String cpPath = filePath;
        if (cpPath.startsWith("src/main/resources/")) {
            cpPath = cpPath.substring("src/main/resources/".length());
        }
        if (!cpPath.startsWith("/")) {
            cpPath = "/" + cpPath;
        }
        try (InputStream is = getClass().getResourceAsStream(cpPath)) {
            if (is != null) {
                properties.load(is);
                return true;
            }
        } catch (IOException e) {
        }

        String noSlashCp = cpPath.startsWith("/") ? cpPath.substring(1) : cpPath;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(noSlashCp)) {
            if (is != null) {
                properties.load(is);
                return true;
            }
        } catch (IOException e) {
        }

        return false;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public double getDouble(String key, double defaultValue) {
        String val = properties.getProperty(key);
        if (val != null) {
            try {
                return Double.parseDouble(val.trim());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        String val = properties.getProperty(key);
        if (val != null) {
            try {
                return Integer.parseInt(val.trim());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public int getTenureBucketCount() {
        int count = 0;
        String[] buckets = {"new", "standard", "premium", "privilege"};
        for (String bucket : buckets) {
            if (hasBucketProperty(bucket)) {
                count++;
            }
        }
        return count;
    }

    private boolean hasBucketProperty(String bucket) {
        for (String key : properties.stringPropertyNames()) {
            if (key.endsWith("." + bucket)) {
                return true;
            }
        }
        return false;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getLoadedPath() {
        return loadedPath;
    }
}
