package com.example.b07_project.pages.store.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;

public class StoreOrderPage extends Fragment {

    private Button buttonPendingOrders;
    private Button buttonCompletedOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_order_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the buttons by their IDs
        buttonPendingOrders = view.findViewById(R.id.buttonPendingOrders);
        buttonCompletedOrders = view.findViewById(R.id.buttonCompletedOrders);

        FirebaseRepository.getCurrentSellerData(
                data -> {
                    String storeId = data.getShopId();
                    // Set OnClickListener for the "Pending Orders" button
                    buttonPendingOrders.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Switch screen to OrderListFragment with the "pending" status
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("showCompletedOrders", false);
                            bundle.putString("storeId", storeId);
                            NavHostFragment.findNavController(StoreOrderPage.this).navigate(R.id.action_storeOrderPage_to_orderListFragment, bundle);
                        }
                    });

                    // Set OnClickListener for the "Completed Orders" button
                    buttonCompletedOrders.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Switch screen to OrderListFragment with the "pending" status
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("showCompletedOrders", true);
                            bundle.putString("storeId", storeId);
                            NavHostFragment.findNavController(StoreOrderPage.this).navigate(R.id.action_storeOrderPage_to_orderListFragment, bundle);
                        }
                    });
                },
                () -> Log.e("StoreOrderPage", "Current user data does not exit")
        );
    }
}
