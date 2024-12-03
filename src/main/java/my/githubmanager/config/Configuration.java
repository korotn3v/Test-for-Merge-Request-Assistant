package my.githubmanager.config;

import my.githubmanager.exception.GitHubManagerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final String CONFIG_FILE = "src/main/resources/github-config.properties";
    private final Properties properties;

    public Configuration() throws GitHubManagerException {
        this.properties = new Properties();
        loadConfig();
    }

    public Configuration(String token) {
        this.properties = new Properties();
        properties.setProperty("github.token", token);
    }

    private void loadConfig() throws GitHubManagerException {
        File configFile = new File(CONFIG_FILE);

        if (!configFile.exists()) {
            throw new GitHubManagerException("Configuration file not found: " + CONFIG_FILE);
        }

        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new GitHubManagerException("Failed to load configuration", e);
        }
    }

    public String getToken() {
        return properties.getProperty("github.token");
    }
}
