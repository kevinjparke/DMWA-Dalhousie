import models.User;
import models.Order;
import models.Inventory;
import services.DatabaseConnection;
import services.InventoryService;
import services.UserService;
import services.OrderService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }
    private void run() {
        //Testing DB Connections
        DatabaseConnection.loadDrivers();
        DatabaseConnection.testConnection();

        //Getting the list of inventory
        List<Inventory> list = InventoryService.loadAllInventoryItems();
        new Main().printInventoryItems(list);

        //Create a customer order
        User user = new User(9904, "ssin", "doe@dal.ca", "9342212231" ,"Halifax, NS" );
        Order order = new Order(231, 9904, "Shirt", 2);

        //Insert user first
        UserService.addUser(user);
        OrderService.addOrder(order);

        //Update inventory with new
        InventoryService.updateItemQuantity(1, 2);
    }

    private void printInventoryItems(List<Inventory> inventory) {
        for(Inventory item : inventory) {
            System.out.println("ID: " + item.getItemID());
            System.out.println("Item name: " + item.getItemName());
            System.out.println("Available quantity: " + item.getAvailableQuantity());
            System.out.println();
        }
    }
}