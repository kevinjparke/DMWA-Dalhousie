package services;

import models.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Code for calculating the execution time was taken from the following website:
 * https://stackoverflow.com/questions/5204051/how-to-calculate-the-running-time-of-my-program
 * Variable names were changed to t1, t2, and executionTime.
 */


public abstract class InventoryService {
    public static List<Inventory> loadAllInventoryItems() {
        String remoteDBURL = DatabaseConnection.getRemoteDB();
        List<Inventory> inventoryList = new ArrayList<>();

        //This code was taken from
        long t1 = System.nanoTime();
        try (
                Connection connection = DriverManager.getConnection(remoteDBURL, "root", "lab4password1");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM INVENTORY;");
                ) {
            while(resultSet.next()) {
                int itemID = resultSet.getInt(1);
                String itemName = resultSet.getString(2);
                int availableQuantity = resultSet.getInt(3);
                inventoryList.add(new Inventory(itemID, itemName, availableQuantity));
            }

        } catch (SQLException e) {
            System.out.println("FAILED: Loading all inventory items");
            System.out.println(e);
        }
        long t2   = System.nanoTime();
        long executionTime = t2 - t1;
        System.out.println("Load inventory execution time: " + ((double)executionTime / 1000000000) + "s");

        return inventoryList;
    }

    public static void updateItemQuantity(Integer id, Integer quantity) {
        String remoteDBURL = DatabaseConnection.getRemoteDB();
        long t1 = System.nanoTime();
        try (
                Connection connection = DriverManager.getConnection(remoteDBURL, "root", "lab4password1");
                Statement statement = connection.createStatement();
        ) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE INVENTORY SET AVAILABLE_QUANTITY=AVAILABLE_QUANTITY - ? " +
                            "WHERE ITEM_ID = ?");
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
            System.out.println("SUCCESS: Inventory update.");
        } catch (SQLException e) {
            System.out.println("FAILED: Loading all inventory items");
            System.out.println(e);
        }
        long t2 = System.nanoTime();
        long executionTime = t2 - t1;
        System.out.println("Load inventory execution time: " + ((double)executionTime / 1000000000) + "s");
    }
}
