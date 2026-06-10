package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class TestDataReader {

    private static final Properties props = new Properties();
    private static final String env;

    // Per-thread cache: random pick from a comma-separated value is made once per test
    private static final ThreadLocal<Map<String, String>> sessionCache =
            ThreadLocal.withInitial(HashMap::new);

    static {
        String baseUrl = ConfigReader.get("BASE_URL");
        if (baseUrl != null && baseUrl.contains("demo")) {
            env = "demo";
        } else if (baseUrl != null && baseUrl.contains("qa")) {
            env = "qa";
        } else {
            env = "uat";
        }

        try (InputStream is = TestDataReader.class.getClassLoader().getResourceAsStream("testdata.properties")) {
            if (is == null) throw new RuntimeException("testdata.properties not found in classpath");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load testdata.properties", e);
        }
    }

    public static String get(String key) {
        String value = props.getProperty(env + "." + key);
        return value != null ? value : props.getProperty(key);
    }

    /**
     * Picks one value at random from a comma-separated testdata entry and caches
     * the result for the life of the current test thread. All calls with the same
     * key within one test return the same value, so multi-step flows stay consistent.
     */
    public static String getSessionValue(String key) {
        return sessionCache.get().computeIfAbsent(key, k -> {
            String raw = get(k);
            if (raw == null || raw.trim().isEmpty()) return "";
            String[] values = raw.split(",");
            return values[new Random().nextInt(values.length)].trim();
        });
    }

    /** Pin a specific value for the current test, overriding the random pick. Must be called after BaseTest.setup() clears the session. */
    public static void setSessionValue(String key, String value) {
        sessionCache.get().put(key, value);
    }

    /** Returns the upload template filename for the current session's campaign type. */
    public static String getTemplateFileName() {
        String baseUrl = ConfigReader.get("BASE_URL");
        String prefix = baseUrl != null && baseUrl.contains("demo") ? "demo" : "uat";
        String campaignType = getSessionValue("CAMPAIGN_TYPE");
        return prefix + "_" + campaignType + "-unifiedtemplate.xlsx";
    }

    /** Call once per test (in BaseTest.setup) to reset session-scoped picks. */
    public static void clearSession() {
        sessionCache.get().clear();
    }
}
