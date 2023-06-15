package services;
import models.Order;

/**
 * Code for calculating the execution time was taken from the following website:
 * https://stackoverflow.com/questions/5204051/how-to-calculate-the-running-time-of-my-program
 * Variable names were changed to t1, t2, and executionTime.
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderService {
    public static void addOrder(Order order) {
        String localDBURL = DatabaseConnection.getLocalDB();

        long t1 = System.nanoTime();
        try (Connection connection = DriverManager.getConnection(localDBURL, "root", "Kb1nkb3qnv34");
             Statement statement = connection.createStatement();){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `ORDER` VALUES(?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, order.getOrderID());
            preparedStatement.setInt(2, order.getUserID());
            preparedStatement.setString(3, order.getItemName());
            preparedStatement.setInt(4, order.getOrderQuantity());
            preparedStatement.setString(5, order.getDate());
            preparedStatement.execute();
            System.out.println("SUCCESS: order added");
        } catch (SQLException e) {
            System.out.println("FAILED: order not added");
            System.out.println(e);
        }
        long t2   = System.nanoTime();
        long executionTime = t2 - t1;
        System.out.println("Insert order execution time: " + ((double) executionTime / 1000000000) + "s");
    }
}
