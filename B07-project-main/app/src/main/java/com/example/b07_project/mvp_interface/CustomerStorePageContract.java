package com.example.b07_project.mvp_interface;

import android.graphics.Bitmap;

import com.example.b07_project.data_classes.ProductData;
import com.example.b07_project.data_classes.StoreData;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface CustomerStorePageContract {
    interface View {
        void updatePage();
        void navigateProductPage(String productId);
    }
    interface Presenter extends ProductListPresenter {
        void updateData();
        String getStoreId();
        String getStoreName();
        String getStoreDesc();
    }
    interface Model {
        String getStoreId();
        void getStoreData(Consumer<StoreData> processData, Consumer<Exception> onException);
        void getProductListData(List<String> productIds, Consumer<Map<String, ProductData>> processData, Consumer<Exception> onException);
        void getProductPreview(String productId, Consumer<Bitmap> processData, Consumer<Exception> onException);
    }
}
