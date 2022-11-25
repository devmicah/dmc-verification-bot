package dev.micah.minecraftcompetitive.database;

import java.sql.*;
import java.util.UUID;

public class Database {

    private static Connection connection;
    private String table = "players";

    private String host, database, username, password;
    private int port;

    public boolean connect() {
        host = "sql5.freesqldatabase.com";
        port = 3306;
        database = "sql5527360";
        username = "sql5527360";
        password = System.getenv("process.sql.password");

        try {
            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return true;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static Connection getConnection() {
        return connection;
    }

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String getDiscord(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT discord FROM discords WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getString("discord");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public void createPlayer(UUID uuid, String code, String displayName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            results.next();
            System.out.println("Table updated!1");
            if (!playerExists(uuid)) {
                PreparedStatement insert = connection
                        .prepareStatement("INSERT INTO " + table + " (uuid, code, name) VALUES (?,?, ?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, code);
                insert.setString(3, displayName);
                insert.executeUpdate();
                System.out.println("Table updated!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
