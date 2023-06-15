package services;
import models.User;

/**
 * Code for calculating the execution time was taken from the following website:
 * https://stackoverflow.com/questions/5204051/how-to-calculate-the-running-time-of-my-program
 * Variable names were changed to t1, t2, and executionTime.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class UserService {
    public static void addUser(User user) {
        String localDBURL = DatabaseConnection.getLocalDB();

        long t1   = System.nanoTime();
        try (Connection connection = DriverManager.getConnection(localDBURL, "root", "Kb1nkb3qnv34");
             Statement statement = connection.createStatement();)
        {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER VALUES(?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, user.getUserID());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getUserEmail());
            preparedStatement.setString(4, user.getUserPhone());
            preparedStatement.setString(5, user.getUserAddress());
            preparedStatement.execute();
            System.out.println("SUCCESS: User added");
        } catch (SQLException e) {
            System.out.println("FAILED: User not added");
            System.out.println(e);
        }
        long t2   = System.nanoTime();
        long executionTime = t2 - t1;
        System.out.println("User insertion execution time: " + ((double) executionTime / 1000000000) + "s");
    }
}
