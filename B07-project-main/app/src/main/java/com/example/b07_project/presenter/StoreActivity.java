package com.example.b07_project.presenter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;

import java.util.List;

public class StoreActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StoreItemAdapter adapter;
    private List<StoreItem> storeItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_store_items_customer);

        // Fetch the store items from the store (e.g., database, API, etc.)


        recyclerView = findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StoreItemAdapter(storeItemList);
        recyclerView.setAdapter(adapter);
    }

    // Fetch the store items from the store (database, API, etc.)

}
