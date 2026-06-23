package com.aas.util;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("SUCCESS");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }
}
