package Service;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * https://www.codecademy.com/courses/learn-advanced-java/lessons/java-database-connectivity/exercises/executing-sql-through-jdbc
 * testConnection() and loadConnection() methods closely follows a tutorial for JDBC that I followed to learn
 * the framework.
 * Both methods are very similar to what is posted on the site. Variable names and print statements were changed.
 */


public class DatabaseConnection {
    private static String localDBURL ;
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        localDBURL = "jdbc:mysql://localhost:3306/Problem1";
    }
    public void loadDrivers() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("SUCCESS: Load JDBC Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("FAILED: Load JDBC Driver");
            System.out.println(e);
            System.exit(1);
        }
    }

    public void testConnection() {
        try(
                Connection localConnection = DriverManager.getConnection(localDBURL, "root", "Kb1nkb3qnv34")
        ){
            System.out.println("SUCCESS: Local DB connection");
        } catch(SQLException e) {
            System.out.println("FAILED: Local DB Connection");
            System.out.println(e);
        }
    }

    public Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection(localDBURL, "root", "Kb1nkb3qnv34");
        connection.setAutoCommit(false);
        return connection;
    }

    public static DatabaseConnection getInstance() {
        if (DatabaseConnection.instance == null) {
            return DatabaseConnection.instance = new DatabaseConnection();
        }
        return instance;
    }
}
