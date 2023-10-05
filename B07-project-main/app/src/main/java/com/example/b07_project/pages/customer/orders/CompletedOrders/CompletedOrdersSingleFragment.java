package com.example.b07_project.pages.customer.orders.CompletedOrders;

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

public class CompletedOrdersSingleFragment extends Fragment {
    private String orderId;
    private RecyclerView recyclerView;
    private List<ProductOrderData> completedOrder_Detail_List;
    private CompletedOrder_Single_Adapter completedOrder_Single_Adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_order_detail, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_completed_detail_orders);
        completedOrder_Detail_List = new ArrayList<>();

        // Set up the RecyclerView
        completedOrder_Single_Adapter = new CompletedOrder_Single_Adapter(completedOrder_Detail_List);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(completedOrder_Single_Adapter);

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
                    completedOrder_Detail_List.clear();
                    completedOrder_Detail_List.addAll(items);
                    completedOrder_Single_Adapter.notifyDataSetChanged();
                },
                exception -> Log.e("CompletedOrderSingleFragment", "Exception: ", exception));
    }
}
