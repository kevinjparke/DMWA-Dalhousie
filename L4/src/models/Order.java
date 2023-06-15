package models;

import java.sql.Date;

public class Order {
    Integer orderID;
    Integer userID;
    String itemName;
    Integer orderQuantity;

    String orderDate;




    public Order(Integer orderID, Integer userID, String itemName, Integer orderQuantity) {
        this.orderID = orderID;
        this.userID = userID;
        this.itemName = itemName;
        this.orderQuantity = orderQuantity;

        //Get date
        long millis = System.currentTimeMillis();
        Date date = new java.sql.Date(millis);
        this.orderDate = date.toString();
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDate() {
        return orderDate;
    }

    public Integer getOrderQuantity() {
       return orderQuantity;
    }
}
