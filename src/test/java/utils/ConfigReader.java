package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    private static final Map<String, String> config = new HashMap<>();

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value != null) return value;
        value = System.getenv(key);
        if (value != null) return value;
        return config.get(key);
    }

    public static String getTemplateFileName() {
        String baseUrl = get("BASE_URL");
        String prefix = baseUrl != null && baseUrl.contains("demo") ? "demo" : "uat";
        String campaignType = get("CAMPAIGN_TYPE");
        return prefix + "_" + campaignType + "-unifiedtemplate.xlsx";
    }
}
