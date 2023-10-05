package com.example.b07_project.pages.customer.orders.PendingOrders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.PendingOrder;
import com.example.b07_project.util.StoreHashTable;
import com.example.b07_project.util.Utility;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class PendingOrderAllFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<PendingOrder> pendingOrderList = new ArrayList<>();
    private PendingOrderAdapter pendingOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_pending_order, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.pending_order_view);
        // Set up the RecyclerView
        pendingOrderAdapter = new PendingOrderAdapter(pendingOrderList, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(pendingOrderAdapter);

        // Fetch pending orders from Firebase Realtime Database
        if (FirebaseRepository.getUserId() != null) {
            fetchPendingOrdersFromDatabase(FirebaseRepository.getUserId());
        }
    }

    private void fetchPendingOrdersFromDatabase(String customerId) {
        Query pendingOrdersQuery = FirebaseDatabase.getInstance().getReference().child("customers").child(customerId)
                .child("orders")
                .orderByChild("complete")
                .equalTo(false);

        // Retrieve list of orders associated with customer
        pendingOrdersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingOrderList.clear();
                List<Task> dataQueryTasks = new ArrayList<>(); // Create list of tasks so that we can wait for all tasks to finish before performing updates
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    // Generate set of suborder ids in order
                    Set<String> suborderIds = new HashSet<>();
                    for (DataSnapshot suborderSnapshot : orderSnapshot.child("suborders").getChildren()) {
                        suborderIds.add(suborderSnapshot.getKey());
                    }
                    // Retrieve list of suborders from "orders" table in database
                    dataQueryTasks.add(getSuborders(suborderIds,
                            suborders -> {
                                StoreHashTable pending = getPendingStoreToItemsTable(suborders);
                                String numSuborders = String.valueOf(calculateTotalOrderIDs(orderSnapshot));
                                String orderValue = Utility.priceIntToString(calculateOrderValue(suborders));
                                String dateOrdered = orderSnapshot.child("date").getValue(String.class);
                                String orderid = orderSnapshot.getKey();

                                // Create a new PendingOrder object and add it to the list
                                PendingOrder pendingOrder = new PendingOrder(pending, numSuborders, orderValue, dateOrdered, orderid);
                                pendingOrderList.add(pendingOrder);
                            },
                            exception -> Log.e("PendingOrderAllFragment", "Exception: ", exception)));
                }

                // Notify the adapter about the data change after all queries complete
                Tasks.whenAllSuccess(dataQueryTasks)
                        .addOnSuccessListener(_unused -> {
                            pendingOrderList.sort(
                                    (item1, item2) -> {
                                        if (item1.getDateOrdered() == null || item2.getDateOrdered() == null) {
                                            return 0;
                                        }
                                        return item1.getDateOrdered().compareTo(item2.getDateOrdered());
                                    }
                            );
                            pendingOrderAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(exception -> Log.e("PendingOrderAllFragment", "Exception: ", exception));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }

    private int calculateOrderValue(List<DataSnapshot> suborders) {
        int totalCost = 0;
        for (DataSnapshot suborder : suborders) {
            totalCost += calculateSuborderTotalCost(suborder);
        }
        return totalCost;
    }

    // calculate total cost of a suborder and return as long
    private int calculateSuborderTotalCost(DataSnapshot suborder) {
        int totalCost = 0;

        for (DataSnapshot item : suborder.child("items").getChildren()) {
            int price = item.hasChild("price") ? item.child("price").getValue(Integer.class) : 0; // Default price of 0
            int quantity = item.hasChild("quantity") ? item.child("quantity").getValue(Integer.class) : 0; // Default quantity of 0
            totalCost += price * quantity;
        }

        return totalCost;
    }

    private int calculateTotalOrderIDs(DataSnapshot orderSnapshot) {
        int totalOrderIDs = 0; // Initialize the total number of order IDs

        DataSnapshot subordersSnapshot = orderSnapshot.child("suborders");

        totalOrderIDs = (int) subordersSnapshot.getChildrenCount();

        return totalOrderIDs;
    }

    // Returns table mapping stores to names of pending items in given suborders
    private StoreHashTable getPendingStoreToItemsTable(List<DataSnapshot> suborders) {
        StoreHashTable pending_orders = new StoreHashTable();

        for (DataSnapshot suborderSnapshot : suborders) {
            // Only add items to table if order is not complete
            if (suborderSnapshot.hasChild("complete") && !suborderSnapshot.child("complete").getValue(Boolean.class)) {
                String storeName = suborderSnapshot.child("name").getValue(String.class);
                List<String> pendingItems = new ArrayList<>();
                for (DataSnapshot itemSnapshot : suborderSnapshot.child("items").getChildren()) {
                    pendingItems.add(itemSnapshot.child("name").getValue(String.class));
                }
                // Convert item name list to primitive array
                // TODO Why are we converting the list to a primitive array?
                pending_orders.put(storeName, pendingItems.toArray(new String[0]));
            }
        }
        // Convert the ArrayList to an array of strings and return it
        return pending_orders;
    }

    // Returns list of suborders from "orders" table in database where suborder id is in suborderIds
    private Task getSuborders(Set<String> suborderIds, Consumer<List<DataSnapshot>> onSuccess, Consumer<Exception> onFailure) {
        return FirebaseDatabase.getInstance().getReference().child("orders").get()
                .addOnSuccessListener(
                        result -> {
                            List<DataSnapshot> suborderSnapshots = new ArrayList<>();
                            for (DataSnapshot orderSnapshot : result.getChildren()) {
                                if (suborderIds.contains(orderSnapshot.getKey())) {
                                    suborderSnapshots.add(orderSnapshot);
                                }
                            }
                            onSuccess.accept(suborderSnapshots);
                        }
                )
                .addOnFailureListener(exception -> onFailure.accept(exception));
    }
}
