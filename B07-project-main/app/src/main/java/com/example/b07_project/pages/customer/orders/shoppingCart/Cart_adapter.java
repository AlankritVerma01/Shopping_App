package com.example.b07_project.pages.customer.orders.shoppingCart;
import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.Cart;
import com.example.b07_project.data_classes.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

//import de.hdodenhof.circleimageview.CircleImageView;

public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.ViewHolder> {

    private Fragment fragment;
    Cart cart;

    private Runnable updateCartPrice;

    public Cart_adapter(Fragment fragment, Cart cart, Runnable updateCartPrice) {
        this.fragment = fragment;
        this.cart = cart;
        this.updateCartPrice=updateCartPrice;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart_store_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        List<String> cartKeyList = new ArrayList<String>(cart.getStoreItems().keySet());
        String cartKey = cartKeyList.get(position);

        // First retrieve the store data
        FirebaseRepository.getStoreData(cartKey,
                storeData -> {
                    //Get the store name from there
                    String storeName = storeData.getStoreName();
                    // Set store name or other details as needed
                    holder.storeName.setText("Store: " + storeName);
                    holder.cart_to_store.setOnClickListener(
                            click -> {
                                // Pass storeId to the customerStorePage fragment
                                Bundle bundle = new Bundle();
                                bundle.putString("storeId", cartKey);
                                Navigation.findNavController(holder.itemView).navigate(R.id.action_main_cart_to_customerStorePage, bundle);
                            }
                    );
                },
                exception -> {
                    Log.e(TAG, "onBindViewHolder: Store id not found", exception);
                });





        // Get the cartItems for the current store
        List<CartItem> cartItems = cart.getStoreCartItems(cartKey);


        // Create and set up the nested RecyclerView for cart items
        Cart_item_adapter cart_item_adapter = new Cart_item_adapter(cartItems, cartKey,updateCartPrice);
        holder.nested_rv.setAdapter(cart_item_adapter);
    }

    @Override
    public int getItemCount() {
        if(cart == null){
            return 0;
        }
        return cart.getStoreItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView storeName;
        RecyclerView nested_rv;
        Button cart_to_store;

        //CircleImageView ivCart_Store;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.tvCartStoreName);
            cart_to_store=itemView.findViewById(R.id.cart_to_store);
            nested_rv = itemView.findViewById(R.id.nested_rv);
            nested_rv.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
        }
    }


}
