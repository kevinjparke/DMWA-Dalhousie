package services;

/**
 * https://www.codecademy.com/courses/learn-advanced-java/lessons/java-database-connectivity/exercises/executing-sql-through-jdbc
 * testConnection() and loadConnection methods closely follows a tutorial for JDBC that I followed to learn
 * the framework.
 * Both methods are very similar to what is posted on the site. Variable names and print statements were changed.
 */


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConnection {
    private final static String localDB = "jdbc:mysql://localhost:3306/A2_distkv736815";
    private final static String remoteDB = "jdbc:mysql://34.152.59.108:3306/A2_distkv736815";

    public static void loadDrivers() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("SUCCESS: Load JDBC Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("FAILED: Load JDBC Driver");
            System.out.println(e);
            System.exit(1);
        }
    }

    public static void testConnection() {
        try(
                Connection localConnection = DriverManager.getConnection(localDB, "root", "Kb1nkb3qnv34")
        ){
            System.out.println("SUCCESS: Local DB connection");
        } catch(SQLException e) {
            System.out.println("FAILED: Local DB Connection");
            System.out.println(e);
        }

        try(Connection remoteConnection = DriverManager.getConnection(remoteDB, "root", "lab4password1")) {
            System.out.println("SUCCESS: Remote DB connection");
        } catch (SQLException e) {
            System.out.println("FAILED: Remote DB connection");
            System.out.println(e);
        }
    }
    public static String getLocalDB() {
        return localDB;
    }

    public static String getRemoteDB() {
        return remoteDB;
    }
}
