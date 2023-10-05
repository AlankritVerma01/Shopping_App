package com.example.b07_project.pages.customer.orders.PendingOrders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.ProductOrderData;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderSingleFragment extends Fragment {
    private String orderId;
    private RecyclerView recyclerView;
    private List<ProductOrderData> pendingOrder_Detail_List;
    private PendingOrder_Single_Adapter pendingOrder_Single_Adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customer_pending_order_detail, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_pending_detail_orders);
        pendingOrder_Detail_List = new ArrayList<>();

        // Set up the RecyclerView
        pendingOrder_Single_Adapter = new PendingOrder_Single_Adapter(pendingOrder_Detail_List);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(pendingOrder_Single_Adapter);

        // Get orderId from bundle and load data
        if (getArguments() != null && getArguments().containsKey("orderId")) {
            orderId = getArguments().getString("orderId");
            refreshData();
        }
        return rootView;
    }

    private void refreshData() {
        FirebaseRepository.getFlattenedOrderItems(FirebaseRepository.getUserId(), orderId,
                items -> {
                    pendingOrder_Detail_List.clear();
                    pendingOrder_Detail_List.addAll(items);
                    pendingOrder_Single_Adapter.notifyDataSetChanged();
                },
                exception -> Log.e("PendingOrderSingleFragment", "Exception: ", exception));
    }
}