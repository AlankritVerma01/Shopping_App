package com.example.b07_project.model;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.data_classes.StoreData;
import com.example.b07_project.util.StoreErrors;
import com.example.b07_project.mvp_interface.StoreEditPageContract;

import java.util.function.Consumer;

public class StoreEditPageModel implements StoreEditPageContract.Model {
    private final String storeId;

    public StoreEditPageModel(String storeId) {
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
    public void setStoreData(StoreData newData, Runnable onSuccess, Consumer<Exception> onException) {
        FirebaseRepository.updateStoreData(storeId, newData, onSuccess, onException);
    }

    @Override
    public StoreErrors.NameError validateStoreName(String name) {
        if (name.length() == 0) {
            return StoreErrors.NameError.EMPTY;
        } else if (name.length() > 40) {
            return StoreErrors.NameError.TOO_LONG;
        }
        return null;
    }

    @Override
    public StoreErrors.DescError validateStoreDesc(String desc) {
        return null;
    }
}
