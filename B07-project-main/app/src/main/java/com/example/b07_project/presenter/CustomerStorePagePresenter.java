package com.example.b07_project.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.b07_project.data_classes.StoreData;
import com.example.b07_project.mvp_interface.CustomerStorePageContract;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CustomerStorePagePresenter implements CustomerStorePageContract.Presenter {
    private static class ProductInfo {
        private final String id;
        private final String name;
        private final String price;

        public ProductInfo(String id, String name, String price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    private final CustomerStorePageContract.View view;
    private final CustomerStorePageContract.Model model;

    private StoreData data = StoreData.getDefault();
    private final List<ProductInfo> products = new ArrayList<>();

    public CustomerStorePagePresenter(CustomerStorePageContract.View view, CustomerStorePageContract.Model model) {
        this.view = view;
        this.model = model;
        updateData();
    }

    @Override
    public void updateData() {
        model.getStoreData(
                storeData -> {
                    data = storeData;
                    model.getProductListData(data.getProductIds(),
                            productDataList -> {
                                products.clear();
                                for (String productId : data.getProductIds()) {
                                    products.add(new ProductInfo(
                                            productDataList.get(productId).getProductId(),
                                            productDataList.get(productId).getProductName(),
                                            productDataList.get(productId).getProductPriceString())
                                    );
                                }
                                view.updatePage();
                            },
                            exception -> Log.e("CustomerStorePagePresenter", exception.getMessage())
                    );
                },
                exception -> Log.e("CustomerStorePagePresenter", exception.getMessage())
        );
    }

    @Override
    public String getStoreId() { return data.getStoreId(); }

    @Override
    public String getStoreName() { return data.getStoreName(); }
    @Override
    public String getStoreDesc() { return data.getStoreDesc(); }
    @Override
    public int getNumProducts() { return products.size(); }
    @Override
    public String getProductId(int index) { return products.get(index).id; }
    @Override
    public void getProductPreview(int index, Consumer<Bitmap> processData, Consumer<Exception> onException) { model.getProductPreview(products.get(index).id, processData, onException); }
    @Override
    public String getProductName(int index) { return products.get(index).name; }

    @Override
    public String getProductDisplayedPrice(int index) { return products.get(index).price; }

    @Override
    public void onProductClicked(int index) { view.navigateProductPage(getProductId(index)); }
}
