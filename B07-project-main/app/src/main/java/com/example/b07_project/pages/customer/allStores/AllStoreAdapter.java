package com.example.b07_project.pages.customer.allStores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.StoreData;
import java.util.ArrayList;
import java.util.List;

public class AllStoreAdapter extends RecyclerView.Adapter<AllStoreAdapter.StoreViewHolder> {
    private List<StoreData> storeList;

    public AllStoreAdapter() {
        storeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_store_details, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        holder.setStoreInfo(storeList.get(position));
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public void setData(List<StoreData> data) {
        storeList = data;
    }
    public List<StoreData> getData() {
        return storeList;
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView storeNameTextView;
        TextView storeDescriptionTextView;
        Button visitStoreButton;

        StoreViewHolder(View itemView) {
            super(itemView);
            storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
            storeDescriptionTextView = itemView.findViewById(R.id.storeDescriptionTextView);
            visitStoreButton = itemView.findViewById(R.id.visitStoreButton);
        }

        protected void setStoreInfo(StoreData store) {
            storeNameTextView.setText(store.getStoreName());
            storeDescriptionTextView.setText(store.getStoreDesc());
            visitStoreButton.setOnClickListener(
                    click -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("storeId", store.getStoreId());
                        Navigation.findNavController(itemView).navigate(R.id.action_allStores_to_customerStorePage, bundle);
                    }
            );
        }
    }
}
