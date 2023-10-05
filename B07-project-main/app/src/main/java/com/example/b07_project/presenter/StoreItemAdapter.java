package com.example.b07_project.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.R;

import java.util.List;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.ViewHolder> {
    private List<StoreItem> storeItemList;

    public StoreItemAdapter(List<StoreItem> storeItemList) {
        this.storeItemList = storeItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;
        private TextView itemNameTextView;
        private TextView itemPriceTextView;
        private EditText itemQuantityEditText;
        private Button increaseQuantityButton;
        private Button decreaseQuantityButton;
        private Button addToCartButton;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize views
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            itemQuantityEditText = itemView.findViewById(R.id.itemQuantityEditText);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreItem item = storeItemList.get(position);

        // Bind data to views
        holder.itemImageView.setImageResource(item.getImageResourceId());
        holder.itemNameTextView.setText(item.getItemName());
        holder.itemPriceTextView.setText(String.format("$%.2f", item.getItemPrice()));
        holder.itemQuantityEditText.setText(String.valueOf(item.getItemQuantity()));

        holder.increaseQuantityButton.setOnClickListener(view -> {
            int quantity = Integer.parseInt(holder.itemQuantityEditText.getText().toString());
            quantity++;
            holder.itemQuantityEditText.setText(String.valueOf(quantity));
        });

        holder.decreaseQuantityButton.setOnClickListener(view -> {
            int quantity = Integer.parseInt(holder.itemQuantityEditText.getText().toString());
            if (quantity > 0) {
                quantity--;
                holder.itemQuantityEditText.setText(String.valueOf(quantity));
            }
        });

        holder.addToCartButton.setOnClickListener(view -> {
            int quantity = Integer.parseInt(holder.itemQuantityEditText.getText().toString());
            if (quantity > 0) {
                item.setItemQuantity(quantity);
                // Add item to cart logic here
                // For example: cart.addItem(item);
                Toast.makeText(view.getContext(), "Item added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeItemList.size();
    }
}