package com.example.b07_project.pages.store.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView; // Import ImageView
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.b07_project.ImageRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.ProductOrderData;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<ProductOrderData> itemList;

    // Remove the itemId variable

    public ItemAdapter(List<ProductOrderData> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ProductOrderData item = itemList.get(position);

        holder.textViewItemName.setText("Name: " + item.getProductName());
        holder.textViewItemQuantity.setText("Quantity: " + item.getQuantity());
        holder.textViewItemPrice.setText("Price: $" + item.getProductPriceString());

        // Initialize the ImageView from the layout
        ImageView imageView = holder.itemView.findViewById(R.id.imageViewProduct);

        // Load the thumbnail image using ImageRepository
        ImageRepository.getItemThumbnail(
                item.getProductId(), // Use the correct item ID
                thumbnailBitmap -> {
                    // Set the thumbnail image to the ImageView
                    imageView.setImageBitmap(thumbnailBitmap);
                },
                () -> {
                    // Handle case when thumbnail data does not exist
                    // You can set a default image or do something else
                    imageView.setImageResource(ImageRepository.getDefaultImageResource());
                },
                exception -> {
                    // Handle exception when loading the thumbnail
                    // You can log the error or show a placeholder image
                    imageView.setImageResource(ImageRepository.getDefaultImageResource());
                }
        );
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewItemName;
        public TextView textViewItemQuantity;
        public TextView textViewItemPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
        }
    }
}
