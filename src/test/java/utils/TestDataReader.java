package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestDataReader {

    private static final Properties props = new Properties();
    private static final String env;

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
        return props.getProperty(env + "." + key);
    }
}
