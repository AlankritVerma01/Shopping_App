package com.example.b07_project.pages.customer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.ImageRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.CartItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerItemFragment extends Fragment {
    private String itemId;
    private String storeId;
    private ImageView imageView;
    private ImageButton btnPrevious, btnNext;
    private int currentImageIndex = 0;
    private List<Bitmap> images = new ArrayList<>();
    private TextView itemName;
    private TextView itemDescription;
    private TextView itemPrice;
    private EditText itemQuantityEditText;  // Add this line

    // this function sets the item attributes to the ones within the Firebase
    public void loadProductData() {
        // Load product data
        FirebaseRepository.getProductData(itemId,
                data -> {
                    itemName.setText(data.getProductName());
                    itemDescription.setText(data.getProductDescription());
                    itemPrice.setText(data.getProductPriceString());
                },
                exception -> Log.e("ItemPage", exception.getMessage()));
        // Load customer data
        FirebaseRepository.getCartData(FirebaseRepository.getUserId(),
                cart -> {
                    // If item is in customer cart, then set quantity to quantity in cart
                    // Otherwise, have default quantity of 0
                    if (cart.getStoreItems().containsKey(storeId)) {
                        for (CartItem item : cart.getStoreCartItems(storeId)) {
                            if (item.getItemID().equals(itemId)) {
                                itemQuantityEditText.setText(String.valueOf(item.getQuantity()));
                                return;
                            }
                        }
                    }
                    itemQuantityEditText.setText(String.valueOf(0));
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_item_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imageView);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnNext = view.findViewById(R.id.btnNext);

        itemName = view.findViewById(R.id.itemName);
        itemDescription = view.findViewById(R.id.itemDescription);
        itemPrice = view.findViewById(R.id.itemPrice);
        itemQuantityEditText = view.findViewById(R.id.itemQuantityEditText);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousImage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextImage();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("storeId")) {
            storeId = bundle.getString("storeId");
        } else {
            Log.e("CustomerItemFragment", "Argument 'storeId' not found in bundle");
        }
        if (bundle != null && bundle.containsKey("item_to_get")) {
            itemId = bundle.getString("item_to_get");
            loadProductData();
            refreshImages();
        } else {
            Log.e("CustomerItemFragment", "Argument 'item_to_get' not found in bundle");
        }

        // Plus button to increase quantity
        Button increaseQuantityButton = view.findViewById(R.id.increaseQuantityButton);
        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(itemQuantityEditText.getText().toString());
                currentQuantity++;
                itemQuantityEditText.setText(String.valueOf(currentQuantity));
            }
        });

        // Minus button to decrease quantity
        Button decreaseQuantityButton = view.findViewById(R.id.decreaseQuantityButton);
        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(itemQuantityEditText.getText().toString());
                if (currentQuantity > 0) {
                    currentQuantity--;
                    itemQuantityEditText.setText(String.valueOf(currentQuantity));
                }
            }
        });

        // Add to Cart button
        Button addToCartButton = view.findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(itemQuantityEditText.getText().toString());
                if (quantity > 0) {
                    // Extract the necessary details from the UI
                    String customerID = FirebaseRepository.getUserId(); // Replace with your actual method
                    String storeID = storeId; // Replace with your actual store ID
                    String itemID = itemId; // Use the item ID or generate one as needed
                    int price = Integer.parseInt(itemPrice.getText().toString().replaceAll("[^0-9]", ""));
                    String name = itemName.getText().toString();

                    // Add the item to the cart using your repository function
                    FirebaseRepository.addItemToCart(customerID, storeID, itemID, price, quantity, name,
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(requireContext(), "Added to Cart!", Toast.LENGTH_SHORT).show();
                                }
                            },
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(requireContext(), "Error adding to cart", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(requireContext(), "Quantity should be greater than 0!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refreshImages() {
        ImageRepository.getItemImages(itemId,
                images -> {
                    CustomerItemFragment.this.images = images;

                    // Disable image switching buttons if only single image
                    btnNext.setEnabled(images.size() > 1);
                    btnPrevious.setEnabled(images.size() > 1);

                    // Reset image index to 0
                    currentImageIndex = 0;
                    updateImageView();
                },
                exception -> Log.e("ItemPageFragment", "Exception: ", exception)
        );
    }

    private void showDefaultImage() {
        imageView.setImageResource(ImageRepository.getDefaultImageResource());
    }

    // carousel functions
    private void showPreviousImage() {
        currentImageIndex--;
        if (currentImageIndex < 0) {
            currentImageIndex = images.size() - 1;
        }
        updateImageView();
    }

    private void showNextImage() {
        currentImageIndex++;
        if (currentImageIndex >= images.size()) {
            currentImageIndex = 0;
        }
        updateImageView();
    }

    private void updateImageView() {
        // Default image for if no images have been uploaded
        if (images.isEmpty()) {
            showDefaultImage();
            return;
        }
        setCurrentImage(images.get(currentImageIndex));
    }

    private void setCurrentImage(Bitmap image) {
        imageView.setImageBitmap(image);
    }
}
