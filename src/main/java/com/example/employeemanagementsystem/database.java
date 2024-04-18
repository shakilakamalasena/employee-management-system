package com.example.employeemanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class database {

    public static Connection connectDb() {

        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/employeemanagementsystem", "root", "");
            return connection;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
