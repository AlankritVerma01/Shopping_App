package com.example.b07_project.pages.customer;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.b07_project.R;
import com.example.b07_project.mvp_interface.CustomerStorePageContract;
import com.example.b07_project.model.StorePageModel;
import com.example.b07_project.pages.store.ProductListAdapter;
import com.example.b07_project.presenter.CustomerStorePagePresenter;

public class CustomerStorePage extends Fragment implements CustomerStorePageContract.View {
    private static final int NUM_COLUMNS = 2;

    // Layout elements
    private TextView nameView;
    private TextView descView;
    private RecyclerView productsListView;

    private CustomerStorePageContract.Presenter presenter;

    public CustomerStorePage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initPresenter(String storeId) {
        presenter = new CustomerStorePagePresenter(
                this,
                new StorePageModel(storeId)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_store_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialise layout element variables
        nameView = view.findViewById(R.id.store_name);
        descView = view.findViewById(R.id.store_description);
        productsListView = view.findViewById(R.id.product_list);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("storeId")) {
            String storeId = bundle.getString("storeId");
            initPresenter(storeId);

            // Bind adapter to recycler view
            productsListView.setLayoutManager(new GridLayoutManager(view.getContext(), NUM_COLUMNS));
            productsListView.setAdapter(new ProductListAdapter(presenter));

            updatePage();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updatePage() {
        // Set page text
        nameView.setText(presenter.getStoreName());
        descView.setText(presenter.getStoreDesc());
        if (productsListView.getAdapter() != null) {
            productsListView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void navigateProductPage(String productId) {
        Bundle bundle = new Bundle();
        bundle.putString("item_to_get", productId);
        bundle.putString("storeId", presenter.getStoreId());
        NavHostFragment.findNavController(this).navigate(R.id.action_customerStorePage_to_customerItemPageFragment, bundle);
    }

}