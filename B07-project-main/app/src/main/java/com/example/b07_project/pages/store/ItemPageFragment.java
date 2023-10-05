package com.example.b07_project.pages.store;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.ImageRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.ProductData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemPageFragment extends Fragment {
    private String itemId;
    private ImageView imageView;
    private ImageButton btnPrevious, btnNext;
    private int currentImageIndex = 0;
    private List<Bitmap> images = new ArrayList<>();
    private boolean imagesEdited = false;
    private EditText itemName;
    private EditText itemDescription;
    private EditText itemPrice;
    private EditText itemQuantity;
    private Button editButton;
    private Button saveButton;
    private Button deleteImageButton;
    private Button changeImageButton;
    private Button addImageButton;

    // this function sets the item attributes to the ones within the Firebase
    public void loadProductData() {
        FirebaseRepository.getProductData(itemId,
                data -> {
                    itemName.setText(data.getProductName());
                    itemDescription.setText(data.getProductDescription());
                    itemPrice.setText(data.getProductPriceString());
                    itemQuantity.setText(String.valueOf(data.getProductQuantity()));
                },
                exception -> Log.e("ItemPage", exception.getMessage()));
    }

    // this function will write the current variable items to the ones in Firebase
    public void saveProductData() {
        String name = itemName.getText().toString();
        String description = itemDescription.getText().toString();
        int price = Integer.parseInt(itemPrice.getText().toString().replaceAll("[^0-9]", ""));
        int quantity = Integer.parseInt(itemQuantity.getText().toString());
        FirebaseRepository.updateProductData(itemId, new ProductData(itemId, name, description, price, quantity),
                () -> {
                    loadProductData();
                    Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show();
                },
                exception -> {});
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_item_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imageView);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnNext = view.findViewById(R.id.btnNext);

        // editable text variables
        itemName = view.findViewById(R.id.itemName);
        itemDescription = view.findViewById(R.id.itemDescription);
        itemPrice = view.findViewById(R.id.itemPrice);
        itemQuantity = view.findViewById(R.id.itemQuantity);

        editButton = view.findViewById(R.id.editButton);
        saveButton = view.findViewById(R.id.saveButton);

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

        // Set click listener for the edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable editing mode for the EditText
                itemName.setEnabled(true);
                itemDescription.setEnabled(true);
                itemPrice.setEnabled(true);
                itemQuantity.setEnabled(true);

                // Show the "Save" button and hide the "Edit" button
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);

                enableImageEditButtons();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the changes and disable editing mode
                itemName.setEnabled(false);
                itemDescription.setEnabled(false);
                itemPrice.setEnabled(false);
                itemQuantity.setEnabled(false);
                // Show the "Edit" button and hide the "Save" button
                saveButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                disableImageEditButtons();
                // Save data
                saveProductData();
                if (imagesEdited) {
                    saveImages();
                    imagesEdited = false;
                }
            }
        });

        // Initialize image buttons
        deleteImageButton = view.findViewById(R.id.btnDeleteImage);
        changeImageButton = view.findViewById(R.id.btnChangeImage);
        addImageButton = view.findViewById(R.id.btnAddImage);

        // Initially disable buttons
        disableImageEditButtons();

        // Set the click listener for buttons
        changeImageButton.setOnClickListener(event -> userChangeImage());
        deleteImageButton.setOnClickListener(event -> userDeleteImage());
        addImageButton.setOnClickListener(event -> userAddImage());

        // Retrieve and display item data
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("item_to_get")) {
            itemId = bundle.getString("item_to_get");
            loadProductData();
            refreshImages();
        } else {
            Log.e("ItemPageFragment", "Argument 'item_to_get' not found in bundle");
        }
    }

    private void refreshImages() {
        ImageRepository.getItemImages(itemId,
            images -> {
                ItemPageFragment.this.images = images;

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

    private void saveImages() {
        ImageRepository.setImages(itemId, images,
                () -> refreshImages(),
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

    private void enableImageEditButtons() {
        deleteImageButton.setEnabled(!images.isEmpty());
        changeImageButton.setEnabled(!images.isEmpty());
        addImageButton.setEnabled(true);
    }

    private void disableImageEditButtons() {
        deleteImageButton.setEnabled(false);
        changeImageButton.setEnabled(false);
        addImageButton.setEnabled(false);
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

    private static final int REQUEST_CODE_ADD_IMAGE = 100;
    private static final int REQUEST_CODE_CHANGE_IMAGE = 102;

    private void userAddImage() {
        imagesEdited = true;
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_ADD_IMAGE);
    }

    private void userDeleteImage() {
        if (images.size() != 0) {
            imagesEdited = true;
            images.remove(currentImageIndex);
            // Reset image to first image if last image was deleted
            if (currentImageIndex >= images.size()) {
                currentImageIndex = 0;
            }
            updateImageView();
            enableImageEditButtons(); // Refresh visibility of image edit buttons
            // Refresh visibility of carousel buttons
            btnNext.setEnabled(images.size() > 1);
            btnPrevious.setEnabled(images.size() > 1);
        }
    }

    private void userChangeImage() {
        if (images.size() != 0) {
            imagesEdited = true;
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_CHANGE_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE_ADD_IMAGE || requestCode == REQUEST_CODE_CHANGE_IMAGE)
                && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Convert image to bitmap
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    if (requestCode == REQUEST_CODE_ADD_IMAGE) {
                        images.add(image);
                        // Set current image to newly added image
                        currentImageIndex = images.size() - 1;
                        // Refresh visibility of carousel buttons
                        btnNext.setEnabled(images.size() > 1);
                        btnPrevious.setEnabled(images.size() > 1);
                        enableImageEditButtons(); // Refresh visibility of image edit buttons
                    } else if (requestCode == REQUEST_CODE_CHANGE_IMAGE) {
                        images.set(currentImageIndex, image);
                    }
                    updateImageView();
                } catch (IOException ioException) {
                    Log.e("ItemPageFragment", "Exception when getting gallery image: ", ioException);
                }
            }
        }
    }

}
