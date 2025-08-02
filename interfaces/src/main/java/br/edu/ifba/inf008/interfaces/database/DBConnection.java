package br.edu.ifba.inf008.interfaces.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Shared database connection utility for all plugins
 */
public class DBConnection {
    private static final String URL = "jdbc:mariadb://localhost:3307/bookstore";
    private static final String USER = "bookstore_user";
    private static final String PASSWORD = "pass";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
