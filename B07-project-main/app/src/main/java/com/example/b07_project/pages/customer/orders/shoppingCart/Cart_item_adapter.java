package com.example.b07_project.pages.customer.orders.shoppingCart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.CartItem;
import com.example.b07_project.util.Utility;

import java.util.List;

//import de.hdodenhof.circleimageview.CircleImageView;

public class Cart_item_adapter extends RecyclerView.Adapter<Cart_item_adapter.ViewHolder> {

    private List<CartItem> cartItemArrayList;
    private String current_storeID;
    private Runnable updateCartPrice;

    public Cart_item_adapter(List<CartItem> cartItemArrayList, String current_storeId, Runnable updateCartPrice) {
        this.cartItemArrayList = cartItemArrayList;
        this.current_storeID = current_storeId;
        this.updateCartPrice = updateCartPrice;
    }

    public interface QuantityChangeListener {
        void onIncrease(int position);

        void onDecrease(int position);
    }

    private QuantityChangeListener quantityChangeListener;

    public Cart_item_adapter(List<CartItem> cartItemArrayList, QuantityChangeListener quantityChangeListener) {
        this.cartItemArrayList = cartItemArrayList;
        this.quantityChangeListener = quantityChangeListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart_store_single_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItemArrayList.get(position);

        holder.tvStoreItemName.setText(cartItem.getName());
        holder.tvStoreItemQuantity.setText("Quantity: " + cartItem.getQuantity());
        holder.tvStoreItemPrice.setText(Utility.priceIntToString(cartItem.getPrice()));
        // holder.ivStoreItem.setImageResource(cartItem.imageID);

        holder.btnQuantityCartIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = cartItem.getQuantity();
                cartItem.setQuantity(currentQuantity + 1);
                holder.tvStoreItemQuantity.setText("Quantity: " + cartItem.getQuantity());
                FirebaseRepository.changeItemQuantity(FirebaseRepository.getUserId(), current_storeID, cartItem.getItemID(), 1, updateCartPrice);
                holder.tvStoreItemPrice.setText(Utility.priceIntToString(cartItem.getPrice()));
            }
        });

        holder.btnQuantityCartDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = cartItem.getQuantity();
                if (currentQuantity > 1) {
                    cartItem.setQuantity(currentQuantity - 1);

                    holder.tvStoreItemQuantity.setText("Quantity: " + cartItem.getQuantity());
                    FirebaseRepository.changeItemQuantity(FirebaseRepository.getUserId(), current_storeID, cartItem.getItemID(), -1, updateCartPrice);
                    holder.tvStoreItemPrice.setText(Utility.priceIntToString(cartItem.getPrice()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStoreItemName, tvStoreItemQuantity, tvStoreItemPrice;
        //CircleImageView ivStoreItem;
        Button btnQuantityCartIncrease, btnQuantityCartDecrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreItemName = itemView.findViewById(R.id.tvNameStoreItem);
            tvStoreItemPrice = itemView.findViewById(R.id.tvPriceStoreItem);
            tvStoreItemQuantity = itemView.findViewById(R.id.tvQtyStoreItem);
            //ivStoreItem = itemView.findViewById(R.id.ivStoreItem);
            btnQuantityCartIncrease = itemView.findViewById(R.id.btnQuantityCartIncrease); // Add this line
            btnQuantityCartDecrease = itemView.findViewById(R.id.btnQuantityCartDecrease); // Add this line
        }
    }
}
