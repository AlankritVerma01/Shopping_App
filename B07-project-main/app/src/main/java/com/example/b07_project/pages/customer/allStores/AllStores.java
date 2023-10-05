package com.example.b07_project.pages.customer.allStores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.StoreData;
import com.example.b07_project.data_classes.customer_register_data;

import java.util.ArrayList;
import java.util.List;

public class AllStores extends Fragment {

    private RecyclerView storeRecyclerView;
    private AllStoreAdapter storeAdapter;
    private customer_register_data userData;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_customer_all_stores, container, false);
        String userId = FirebaseRepository.getUserId();

        // Fetch user data from Firebase based on the user ID (replace "userId" with the actual user ID)
        FirebaseRepository.getUserData(userId, userData -> {
            // userData will contain the user's data, update the Welcome Name field
            String welcomeName = "Welcome " + userData.getFirstName() + " " + userData.getLastName();
            // Assuming you have a TextView with the id "welcomeNameTextView" in your layout
            TextView welcomeNameTextView = rootView.findViewById(R.id.welcomeTextView);
            welcomeNameTextView.setText(welcomeName);
        }, exception -> {
            // Handle the error if fetching user data fails
        });

        // Initialize RecyclerView and adapter
        storeRecyclerView = rootView.findViewById(R.id.storeRecyclerView);
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        storeAdapter = new AllStoreAdapter();
        storeRecyclerView.setAdapter(storeAdapter);
        // Fetch store IDs from Firebase
        FirebaseRepository.getStoreIds(
                storeId -> {
                    // For each store ID retrieved, fetch the store data
                    FirebaseRepository.getStoreData(
                            storeId,
                            storeData -> {
                                // Add the retrieved store data to the adapter's data list
                                List<StoreData> dataList = storeAdapter.getData();
                                if (dataList == null) {
                                    dataList = new ArrayList<>();
                                    storeAdapter.setData(dataList);
                                }
                                dataList.add(storeData);
                                storeAdapter.notifyDataSetChanged();
                            },
                            exception -> {
                                // Handle the error if fetching store data fails
                                // For example, display an error message to the user.
                            }
                    );
                },
                exception -> {
                    // Handle the error if fetching store IDs fails
                    // For example, display an error message to the user.
                }
        );
        return rootView;
    }
}
