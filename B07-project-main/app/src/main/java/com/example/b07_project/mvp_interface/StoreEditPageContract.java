package com.example.b07_project.mvp_interface;

import com.example.b07_project.data_classes.StoreData;
import com.example.b07_project.util.StoreErrors;

import java.util.function.Consumer;

public interface StoreEditPageContract {
    interface View {
        void updatePage();
        void nameError(StoreErrors.NameError error);
        void descError(StoreErrors.DescError error);
        String getNameFieldValue();
        String getDescFieldValue();
        void navigateBack();
        void displaySavedNotification();
    }
    interface Presenter {
        String getOldStoreName();
        String getOldStoreDesc();
        void onSaveButtonClicked();
        void onCancelButtonClicked();
    }
    interface Model {
        String getStoreId();
        void getStoreData(Consumer<StoreData> processData, Consumer<Exception> onException);
        void setStoreData(StoreData newData, Runnable onSuccess, Consumer<Exception> onException);
        StoreErrors.NameError validateStoreName(String name);
        StoreErrors.DescError validateStoreDesc(String desc);
    }
}
