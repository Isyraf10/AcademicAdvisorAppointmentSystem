package com.aas.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/academic_advisor";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    URL, 
                    USER,
                    PASSWORD
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver was not found. Check that mysql-connector-j is included in WEB-INF/lib.", e);
        }
    }
}
