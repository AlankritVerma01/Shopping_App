package com.example.b07_project.pages.store.order;

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

public class ItemListFragment extends Fragment {

    private RecyclerView recyclerViewItemList;
    private List<ProductOrderData> itemList;
    private ItemAdapter itemAdapter;
    private String orderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewItemList = view.findViewById(R.id.recyclerViewItemList);
        recyclerViewItemList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        itemList = new ArrayList<>();
        //Bundle bundle = getArguments();
        itemAdapter = new ItemAdapter(itemList);
        recyclerViewItemList.setAdapter(itemAdapter);

        // Get the order ID passed from OrderListActivity
        if (getArguments() != null && getArguments().containsKey("orderId")) {
            orderId = getArguments().getString("orderId");
            // Retrieve the list of items that the order contains from the database
            FirebaseRepository.getOrderData(
                    orderId,
                    order -> {
                        // Copy items in order to itemList
                        itemList.clear();
                        itemList.addAll(order.getItems());
                        itemAdapter.notifyDataSetChanged();
                    },
                    exception -> Log.e("ItemListActivity", exception.getMessage())
            );
        } else {
            // Handle the case when orderId is null (optional based on your app logic)
            // For example, show an error message or finish the activity.
            // finish();
        }
    }
}
