package com.example.b07_project.data_classes;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart{

    // Map of the format where StoreID links to a List of items
    private Map<String, List<CartItem>> storeItems;

    public Cart() {
        storeItems = new HashMap<>();
    }

    public Cart(Map<String, List<CartItem>> storeItems) {
        this.storeItems = storeItems;
    }

    // getters and setters
    public Map<String, List<CartItem>> getStoreItems() {
        return storeItems;
    }

    public void addStoreItems(String storeID, List<CartItem> cartItems){
        storeItems.put(storeID, cartItems);
    }

    public List<CartItem> getStoreCartItems(String storeID) {
        return storeItems.get(storeID);
    }

    public void removeStoreItems(String storeID) {
        storeItems.remove(storeID);
    }

    public static void printCart(Cart cart) {
        if (cart == null) {
            Log.d("CartData", "Cart is empty or not fetched yet.");
            return;
        }

        Map<String, List<CartItem>> storeItems = cart.getStoreItems();

        if (storeItems.isEmpty()) {
            Log.d("CartData", "Cart is empty.");
            return;
        }

        for (Map.Entry<String, List<CartItem>> entry : storeItems.entrySet()) {
            String storeID = entry.getKey();
            List<CartItem> cartItems = entry.getValue();

            Log.d("CartData", "Store ID: " + storeID);

            for (CartItem cartItem : cartItems) {
                Log.d("CartData", "   Item ID: " + cartItem.getItemID());
                Log.d("CartData", "   Price: " + cartItem.getPrice());
                Log.d("CartData", "   Quantity: " + cartItem.getQuantity());
                Log.d("CartData", "   Name: " + cartItem.getName());
                Log.d("CartData", "------------------");
            }
        }
    }

}
