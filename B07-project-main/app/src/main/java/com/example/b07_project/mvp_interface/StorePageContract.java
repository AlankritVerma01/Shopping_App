package com.example.b07_project.mvp_interface;

import android.graphics.Bitmap;

import com.example.b07_project.data_classes.ProductData;
import com.example.b07_project.data_classes.StoreData;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface StorePageContract {
    interface View {
        void updatePage();
        void navigateEditPage(String storeId);
        void navigateProductPage(String productId);
        void createNewProduct(String shopID);
    }
    interface Presenter extends ProductListPresenter {
        void updateData();
        String getStoreName();
        String getStoreDesc();
        void onAddProductClicked();
        void onEditClicked();
    }
    interface Model {
        String getStoreId();
        void getStoreData(Consumer<StoreData> processData, Consumer<Exception> onException);
        void getProductListData(List<String> productIds, Consumer<Map<String, ProductData>> processData, Consumer<Exception> onException);
        void getProductPreview(String productId, Consumer<Bitmap> processData, Consumer<Exception> onException);
    }
}
