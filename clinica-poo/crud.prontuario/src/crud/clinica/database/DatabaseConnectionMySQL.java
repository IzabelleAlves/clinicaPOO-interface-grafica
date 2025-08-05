package crud.clinica.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import crud.clinica.config.DatabaseConfig;

public class DatabaseConnectionMySQL implements IConnection {

    private String username;
    private String password;
    private String address;
    private String port;
    private String database;

    public DatabaseConnectionMySQL() {
        try {
            DatabaseConfig config = new DatabaseConfig("src/dbconfig.txt");
            this.username = config.getDbUser();
            this.password = config.getDbPassword();
            this.address = config.getDbAddress();
            this.port = config.getDbPort();
            this.database = config.getDbName();
        } catch (Exception e) {
            System.out.println("Erro ao carregar configurações do banco: " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        Connection conn = null;
        try {
            String urlSemDb = "jdbc:mysql://" + address + ":" + port + "/?useSSL=false&allowPublicKeyRetrieval=true";
            conn = DriverManager.getConnection(urlSemDb, username, password);

            try (var stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
            }

            conn.close();

            
            String urlComDb = "jdbc:mysql://" + address + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true";
            conn = DriverManager.getConnection(urlComDb, username, password);

            try (var stmt = conn.createStatement()) {
                String sqlPacientes = """
                    CREATE TABLE IF NOT EXISTS pacientes (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        nome VARCHAR(255) NOT NULL,
                        cpf VARCHAR(14) NOT NULL UNIQUE,
                        data_nascimento DATETIME NOT NULL
                    )
                """;
                stmt.executeUpdate(sqlPacientes);

                String sqlExames = """
                	    CREATE TABLE IF NOT EXISTS exames (
                	        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                	        descricao VARCHAR(255) NOT NULL,
                	        data_exame DATE NOT NULL,
                	        paciente_id BIGINT NOT NULL,
                	        FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE
                	    )
                	""";
                stmt.executeUpdate(sqlExames);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ou configurar o banco de dados: " + e.getMessage(), e);
        }

        return conn;
    }

    @Override
    public void closeConnection() {
    }
}
