package com.example.b07_project.pages.store.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.OrderData;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<OrderData> orderList;
    private boolean showCompletedOrders = false;
    private String storeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_order_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Process showCompletedOrders argument
        if (getArguments() != null && getArguments().containsKey("showCompletedOrders")) {
            showCompletedOrders = getArguments().getBoolean("showCompletedOrders");
        }

        // Process storeId argument
        if (getArguments() != null && getArguments().containsKey("storeId")) {
            storeId = getArguments().getString("storeId");

            // Initialise recyclerView
            recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
            orderList = new ArrayList<>();
            orderAdapter = new OrderAdapter(orderList);
            // Set click listener for each order switch screen to item list screen and display items in order
            orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    OrderData selectedOrder = orderList.get(position);
                    if (selectedOrder != null) {
                        // Switch to ItemListFragment and pass the selected order ID
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId", selectedOrder.getOrderId());
                        NavHostFragment.findNavController(OrderListFragment.this).navigate(R.id.action_orderListFragment_to_itemListFragment, bundle);
                    }
                }

                @Override
                public void onMoveToCompletedClick(View view, int position) {
                    OrderData selectedOrder = orderList.get(position);
                    if (selectedOrder != null) {
                        // Move the order to the "completed" section in the database
                        FirebaseRepository.markOrderComplete(selectedOrder.getOrderId(),
                                () -> loadOrders());
                        String suborderId = selectedOrder.getOrderId();
                        FirebaseRepository.findBigOrderID(suborderId,
                                (orderId, customerId) -> {
                                    FirebaseRepository.updateBigOrderCompleted(customerId, orderId);
                                });
                    }
//                    selectedOrder.getOrderId();
//                    String customerId = selectedOrder.getCustomerId();
//                    String bigOrderId = selectedOrder.getBigOrderId();
//
//                    FirebaseRepository.checkBigOrderCompletion(customerId, bigOrderId, isCompleted -> {
//                        if (isCompleted) {
//                            FirebaseRepository.markBigOrderComplete(customerId, bigOrderId);
//                            loadOrders(); // Refresh the order list
//                        } else {
//                            // Handle the case where not all suborders are completed
//                            // You can show a message or perform any other action here
//                        }
//                    });
                }
            });
            recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerViewOrders.setAdapter(orderAdapter);

            loadOrders();
        }
    }

    private void loadOrders() {
        if (storeId != null) {
            FirebaseRepository.getStoreOrders(
                    storeId,
                    orders -> {
                        orderList.clear();
                        for (OrderData order : orders) {
                            if (order.isComplete() == showCompletedOrders) {
                                orderList.add(order);
                            }
                        }
                        orderAdapter.setShowCompletedOrders(showCompletedOrders);
                        orderAdapter.notifyDataSetChanged();
                    },
                    exception -> Log.e("OrderListActivity", exception.getMessage())
            );
        }
    }
}
