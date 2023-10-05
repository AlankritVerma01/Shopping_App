package com.example.b07_project.pages.customer.orders.shoppingCart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.Cart;
import com.example.b07_project.data_classes.CartItem;
import com.example.b07_project.util.Utility;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Lambda;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class main_cart extends Fragment {

    private RecyclerView mainCartRecyclerView;
    private Cart_adapter cartAdapter;
    private Cart cart;
    private Button finalizeCartButton;
    private TextView totalCartValue;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_customer_main_cart, container, false);

        mainCartRecyclerView = rootView.findViewById(R.id.RV_main_Cart);
        mainCartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // Set layout manager here
        cartAdapter = new Cart_adapter(this, cart, ()->updateTotalCartValueInMain()); // Initialize the adapter after cart data is retrieved
        mainCartRecyclerView.setAdapter(cartAdapter);

        refreshCart();

//        cart = createSampleCart();
//        onCartRetrieved(cart);




//        super.onCreate(savedInstanceState);
//        View rootView = inflater.inflate(R.layout.fragment_customer_main_cart, container, false);
//
//        mainCartRecyclerView = rootView.findViewById(R.id.RV_main_Cart);
//
//        mainCartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // Set layout manager here
//
//        //cart = createSampleCart();
//        // onCartRetrieved(cart);
//        // Replace "customerID" with the actual customer ID obtained from your authentication process
//        String customerID = FirebaseRepository.getUserId();
//        FirebaseRepository.getCartData(customerID, this::onCartRetrieved);
//        cartAdapter = new Cart_adapter(this, cart);
//        mainCartRecyclerView.setAdapter(cartAdapter);
////        RVcartStore = findViewById(R.id.RV_main_Cart);
//
//        // Initialize your cartStoreArrayList and cartStoreItemArrayList with data
//        cartStoreArrayList = new ArrayList<>();
////
////        // Replace "customerID" with the actual customer ID obtained from your authentication process
//        FirebaseRepository.getCartData(customerID, cart -> {
//            cartStoreArrayList.addAll(cart.getCartStores());
//            setupRecyclerView();
//        });

        totalCartValue = rootView.findViewById((R.id.totalCartValue));

        FirebaseRepository.totalCartValue(FirebaseRepository.getUserId(),
                (cartValue) -> totalCartValue.setText("Total Cart Value: "+ Utility.priceIntToString(cartValue)));


        finalizeCartButton = rootView.findViewById(R.id.CartToOrderButton);
        finalizeCartButton.setEnabled(false);
        finalizeCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizeAndDeleteCart();
//                FirebaseRepository.finalizeCart(FirebaseRepository.getUserId());
//                FirebaseRepository.deleteCart(FirebaseRepository.getUserId());

                // Handle the "Finalize Cart" button click here
                // You can perform any actions you need when the button is clicked
            }
        });
        return rootView;
    }

    public void updateTotalCartValueInMain()
    {
        FirebaseRepository.totalCartValue(FirebaseRepository.getUserId(),
                (cartValue) -> totalCartValue.setText("Total Cart Value: "+ Utility.priceIntToString(cartValue)));
    }

    private void refreshCart() {
        String customerID = FirebaseRepository.getUserId();
        FirebaseRepository.getCartData(customerID, this::onCartRetrieved);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void finalizeAndDeleteCart() {
        FirebaseRepository.finalizeCart(FirebaseRepository.getUserId(),
                () -> FirebaseRepository.deleteCart(FirebaseRepository.getUserId(),
                        () -> {
                            Toast.makeText(this.getActivity(), "Order successfully placed!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(this).popBackStack();
                        },
                        exception -> Log.e("main_cart", "Exception", exception)),
                exception -> Log.e("main_cart", "Exception: ", exception));
    }

    // Callback method to handle the retrieved cart data
    private void onCartRetrieved(Cart cart) {
        this.cart = cart;
        cartAdapter.setCart(cart); // Update the cart data in the adapter
        cartAdapter.notifyDataSetChanged(); // Notify the adapter about the data change
        if (!cart.getStoreItems().isEmpty()) {
            finalizeCartButton.setEnabled(true);
        }
    }

    private Cart createSampleCart() {
        Cart cart = new Cart();

        // Sample cart items
        CartItem item1 = new CartItem("item1", 10, 2, "Item 1");
        CartItem item2 = new CartItem("item2", 5, 1, "Item 2");
        CartItem item3 = new CartItem("item3", 8, 3, "Item 3");
        CartItem item4 = new CartItem("item4", 15, 1, "Item 4");

        // Sample store 1 with cart items
        List<CartItem> store1Items = new ArrayList<>();
        store1Items.add(item1);
        store1Items.add(item2);
        cart.addStoreItems("bde1836d-9f93-4777-b1c5-6b36ac8cdc3c", store1Items);

        // Sample store 2 with cart items
        List<CartItem> store2Items = new ArrayList<>();
        store2Items.add(item3);
        store2Items.add(item4);
        cart.addStoreItems("store2Fomrb", store2Items);

        return cart;
    }
}