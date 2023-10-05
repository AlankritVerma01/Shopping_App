package com.example.b07_project.pages.customer.orders.CompletedOrders;

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
import com.example.b07_project.data_classes.completedOrder;
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


public class CompletedOrderAllFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<completedOrder> completedOrderList= new ArrayList<>();
    private CompletedOrderAdapter completedOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_completed_order,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_completed_orders);
        //completedOrderList = new ArrayList<>();

        // Set up the RecyclerView
        completedOrderAdapter = new CompletedOrderAdapter(completedOrderList, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(completedOrderAdapter);

        // Fetch completed orders from Firebase Realtime Database
        if (FirebaseRepository.getUserId() != null) {
            fetchCompletedOrdersFromDatabase(FirebaseRepository.getUserId());
        }

    }

    private void fetchCompletedOrdersFromDatabase(String customerID) {
        Query completedOrdersQuery = FirebaseDatabase.getInstance().getReference().child("customers").child(customerID)
                .child("orders")
                .orderByChild("complete")
                .equalTo(true);

        completedOrdersQuery.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                completedOrderList.clear();
                List<Task> dataQueryTasks = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    // Generate set of suborder ids in order
                    Set<String> suborderIds = new HashSet<>();
                    for (DataSnapshot suborderSnapshot : orderSnapshot.child("suborders").getChildren()){
                        suborderIds.add(suborderSnapshot.getKey());
                    }

                    // retrieve list of suborders from "orders" table in database
                    dataQueryTasks.add(getSuborders(suborderIds,
                            suborders ->  {
                                StoreHashTable complete = getCompletedStoreToItemsTable(suborders);
                                String numSuborders = String.valueOf(calculateTotalOrderIDs(orderSnapshot));
                                String orderValue = Utility.priceIntToString(calculateOrderValue(suborders));
                                String dateOrdered = orderSnapshot.child("date").getValue(String.class);
                                String orderid = orderSnapshot.getKey();

                                // Create a completedOrder object and add it to the list
                                completedOrder completeOrder = new completedOrder(complete, numSuborders, dateOrdered, orderValue, orderid);
                                completedOrderList.add(completeOrder);
                            },
                            exception -> Log.e("CompleteOrderAllFragment", "Exception: ", exception)));

                }

                // Notify the adapter about the data change
                // Notify the adapter about the data change after all queries complete
                Tasks.whenAllSuccess(dataQueryTasks)
                        .addOnSuccessListener(_unused -> {
                            completedOrderList.sort(
                                    (item1, item2) -> {
                                        if (item1.getDateOrdered() == null || item2.getDateOrdered() == null) {
                                            return 0;
                                        }
                                        return item1.getDateOrdered().compareTo(item2.getDateOrdered());
                                    }
                            );
                            completedOrderAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(exception -> Log.e("CompletedOrderAllFragment", "Exception: ", exception));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
                Log.e("CompletedOrderFragment", "Database Error: " + databaseError.getMessage());
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

    private StoreHashTable getCompletedStoreToItemsTable(List<DataSnapshot> suborders) {
        StoreHashTable complete_orders = new StoreHashTable();

        for(DataSnapshot suborderSnapshot : suborders) {
            // only add items if the order is complete
            if (suborderSnapshot.hasChild("complete") && suborderSnapshot.child("complete").getValue(Boolean.class)){
                String storeName = suborderSnapshot.child("name").getValue(String.class);
                List<String> completedItems = new ArrayList<>();
                for (DataSnapshot itemSnapshot : suborderSnapshot.child("items").getChildren()){
                    completedItems.add(itemSnapshot.child("name").getValue(String.class));
                }

                complete_orders.put(storeName, completedItems.toArray(new String[0]));
            }
        }

        return complete_orders;
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

