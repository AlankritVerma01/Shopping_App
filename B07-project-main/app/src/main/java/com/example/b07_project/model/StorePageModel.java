package com.example.b07_project.model;

import android.graphics.Bitmap;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.ImageRepository;
import com.example.b07_project.data_classes.ProductData;
import com.example.b07_project.data_classes.StoreData;
import com.example.b07_project.mvp_interface.CustomerStorePageContract;
import com.example.b07_project.mvp_interface.StorePageContract;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class StorePageModel implements StorePageContract.Model, CustomerStorePageContract.Model {
    private final String storeId;

    public StorePageModel(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public void getStoreData(Consumer<StoreData> processData, Consumer<Exception> onException) {
        FirebaseRepository.getStoreData(storeId, processData, onException);
    }

    @Override
    public void getProductListData(List<String> productIds, Consumer<Map<String, ProductData>> processData, Consumer<Exception> onException) {
        FirebaseRepository.getProductListData(productIds, processData, onException);
    }

    @Override
    public void getProductPreview(String productId, Consumer<Bitmap> processData, Consumer<Exception> onException) {
        ImageRepository.getItemThumbnail(productId, processData,
                () -> processData.accept(null),
                onException);
    }
}
