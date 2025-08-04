package crud.clinica.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DatabaseConfig {
    private String dbUser;
    private String dbPassword;
    private String dbAddress;
    private String dbPort;
    private String dbName;

    public DatabaseConfig(String configFilePath) throws IOException {
        loadConfig(configFilePath);
    }

    private void loadConfig(String configFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFilePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("=", 2);
            if (parts.length < 2) continue;

            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (key) {
                case "DB_USER":
                    dbUser = value;
                    break;
                case "DB_PASSWORD":
                    dbPassword = value;
                    break;
                case "DB_ADDRESS":
                    dbAddress = value;
                    break;
                case "DB_PORT":
                    dbPort = value;
                    break;
                case "DB_NAME":
                    dbName = value;
                    break;
            }
        }

        reader.close();
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbAddress() {
        return dbAddress;
    }

    public String getDbPort() {
        return dbPort;
    }

    public String getDbName() {
        return dbName;
    }
}
