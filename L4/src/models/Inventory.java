package models;

public class Inventory {
    Integer itemID;
    String itemName;
    Integer availableQuantity;

    public Inventory(Integer itemID, String itemName, Integer availableQuantity) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.availableQuantity = availableQuantity;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
