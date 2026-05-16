package com.lab.util;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {

            System.out.println("SUCCESS");

        } else {

            System.out.println("FAILED");
        }
    }
}