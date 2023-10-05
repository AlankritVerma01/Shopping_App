package com.example.b07_project.mvp_interface;

import android.graphics.Bitmap;

import java.util.function.Consumer;

public interface ProductListPresenter {
    int getNumProducts();
    String getProductId(int index);
    void getProductPreview(int index, Consumer<Bitmap> processData, Consumer<Exception> onException);
    String getProductName(int index);
    String getProductDisplayedPrice(int index);
    void onProductClicked(int index);
}
