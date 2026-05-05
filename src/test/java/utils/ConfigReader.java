package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    private static final Map<String, String> config = new HashMap<>();

    static {
        String envPath = System.getProperty("env.file", ".env");
        // Try absolute path first, then relative
        File envFile = new File(envPath);
        if (!envFile.isAbsolute()) {
            // Try to find .env in the working directory or project root
            String[] paths = {".env", "../.env", "src/test/resources/.env"};
            for (String path : paths) {
                envFile = new File(path);
                if (envFile.exists()) {
                    System.out.println("ConfigReader: Found .env at " + envFile.getAbsolutePath());
                    break;
                }
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int idx = line.indexOf('=');
                if (idx > 0) {
                    String key = line.substring(0, idx).trim();
                    String value = line.substring(idx + 1).trim();
                    config.put(key, value);
                }
            }
            System.out.println("ConfigReader: Loaded " + config.size() + " properties from " + envFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("ConfigReader: Error loading .env file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value != null && !value.isEmpty()) {
            System.out.println("ConfigReader: Got " + key + " from system property");
            return value;
        }
        value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            System.out.println("ConfigReader: Got " + key + " from environment variable");
            return value;
        }
        value = config.get(key);
        if (value != null) {
            System.out.println("ConfigReader: Got " + key + " from .env file");
        } else {
            System.out.println("ConfigReader: Key " + key + " not found");
        }
        return value;
    }

    public static String getTemplateFileName() {
        String baseUrl = get("BASE_URL");
        String prefix = baseUrl != null && baseUrl.contains("demo") ? "demo" : "uat";
        String campaignType = get("CAMPAIGN_TYPE");
        return prefix + "_" + campaignType + "-unifiedtemplate.xlsx";
    }
}
