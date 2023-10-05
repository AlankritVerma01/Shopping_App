package com.example.b07_project.pages.store;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.mvp_interface.StorePageContract;
import com.example.b07_project.model.StorePageModel;
import com.example.b07_project.presenter.StorePagePresenter;

import java.util.UUID;

public class StorePage extends Fragment implements StorePageContract.View {
    private static final int NUM_COLUMNS = 2;

    // Layout elements
    private TextView nameView;
    private TextView descView;
    private RecyclerView productsListView;

    private StorePageContract.Presenter presenter;

    public StorePage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initPresenter(String storeId) {
        presenter = new StorePagePresenter(
                this,
                new StorePageModel(storeId)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialise layout element variables
        nameView = view.findViewById(R.id.store_name);
        descView = view.findViewById(R.id.store_description);
        productsListView = view.findViewById(R.id.product_list);

        FirebaseRepository.getCurrentSellerData(
                data -> {
                    String storeId = data.getShopId();
                    initPresenter(storeId);

                    // Bind adapter to recycler view
                    productsListView.setLayoutManager(new GridLayoutManager(view.getContext(), NUM_COLUMNS));
                    productsListView.setAdapter(new ProductListAdapter(presenter));

                    // Add click listener for buttons
                    view.findViewById(R.id.add_product_button).setOnClickListener(v -> presenter.onAddProductClicked());
                    view.findViewById(R.id.edit_store_button).setOnClickListener(v -> presenter.onEditClicked());

                    updatePage();
                },
                () -> Log.e("StorePage", "User " + FirebaseRepository.getUserId() + " is not a store owner")
        );
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
    public void navigateEditPage(String storeId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("storeId", storeId);
        NavHostFragment.findNavController(this).navigate(R.id.action_StorePage_to_StoreEditPage, bundle);
    }

    @Override
    public void navigateProductPage(String productId) {
        Bundle bundle = new Bundle();
        bundle.putString("item_to_get", productId);
        NavHostFragment.findNavController(this).navigate(R.id.action_StorePage_to_itemPageFragment, bundle);
    }

    // this function lets you create a new product with the plus button
    @Override
    public void createNewProduct(String shopID){
        String default_name = "Enter product name";
        String default_description = "Enter product description";
        int default_quantity = 0;
        double default_price = 0.00;
        // randomly generate a string to be the ID of the item
        String newItemID = UUID.randomUUID().toString().replaceAll("_", "");
        Log.e("StorePage generate random ID", newItemID);

        // create the product in the Firebase with the random ID generate
        FirebaseRepository.createItem(newItemID, shopID, default_name, default_description, default_price, default_quantity);
        navigateProductPage(newItemID);

        //FirebaseRepository.addItemToCart("customerID1", "StoreID2", "ItemID1", 29.99, 1);
    }

}