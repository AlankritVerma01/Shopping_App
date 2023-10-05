package com.example.b07_project.util;

import java.util.Hashtable;

public class StoreHashTable extends Hashtable<String, String[]> {
    // Override the toString method
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        // Iterate through the hashtable
        for (String storeName : this.keySet()) {
            String[] items = this.get(storeName);

            // Append the store name
           stringBuilder.append(storeName).append(":");
            //System.out.print(storeName+":");

            // Append the items
            for (int i = 0; i < items.length; i++) {
                stringBuilder.append(items[i]);
                if (i < items.length - 1) {
                    stringBuilder.append(", ");
                }
            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
