package com.example.b07_project.pages.customer.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07_project.R;
import com.example.b07_project.pages.customer.orders.PendingOrders.PendingOrderAdapter;
import com.example.b07_project.pages.customer.orders.CompletedOrders.CompletedOrderAdapter;

public class OrderStatusFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_customer_order_main_choice, container, false);

        Button pendingOrdersButton = rootView.findViewById(R.id.pending);
        Button completedOrdersButton = rootView.findViewById(R.id.completed);
        Button cart = rootView.findViewById(R.id.cart);

        pendingOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle pending orders button click, direct to activities_pending_orders
                NavHostFragment.findNavController(OrderStatusFragment.this).navigate(R.id.action_fragment_OrderStatus_to_pendingOrderAllFragment);
            }
        });

        completedOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle completed orders button click, direct to activities_completed_orders
                NavHostFragment.findNavController(OrderStatusFragment.this).navigate(R.id.action_fragment_OrderStatus_to_completedOrderActivity);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle completed orders button click, direct to activities_completed_orders
                NavHostFragment.findNavController(OrderStatusFragment.this).navigate(R.id.action_fragment_OrderStatus_to_main_cart);
            }
        });
        return rootView;
    }
}