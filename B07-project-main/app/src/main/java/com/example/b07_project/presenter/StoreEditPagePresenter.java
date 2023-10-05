package com.example.b07_project.presenter;

import android.util.Log;

import com.example.b07_project.data_classes.StoreData;
import com.example.b07_project.util.StoreErrors;
import com.example.b07_project.mvp_interface.StoreEditPageContract;

public class StoreEditPagePresenter implements StoreEditPageContract.Presenter {
    private final StoreEditPageContract.View view;
    private final StoreEditPageContract.Model model;

    private StoreData data = StoreData.getDefault();

    public StoreEditPagePresenter(StoreEditPageContract.View view, StoreEditPageContract.Model model) {
        this.view = view;
        this.model = model;
        update();
    }

    private void update() {
        model.getStoreData(
                storeData -> {
                    data = storeData;
                    view.updatePage();
                },
                exception -> Log.e("StoreEditPagePresenter", exception.getMessage())
        );
    }

    private void back() { view.navigateBack(); }

    @Override
    public String getOldStoreName() { return data.getStoreName(); }

    @Override
    public String getOldStoreDesc() { return data.getStoreDesc(); }

    private boolean validateNewValues(String newStoreName, String newStoreDesc) {
        boolean error = false;
        StoreErrors.NameError nameError = model.validateStoreName(newStoreName);
        if (nameError != null) {
            view.nameError(nameError);
            error = true;
        }
        StoreErrors.DescError descError = model.validateStoreDesc(newStoreDesc);
        if (descError != null) {
            view.descError(descError);
            error = true;
        }
        return !error;
    }

    @Override
    public void onSaveButtonClicked() {
        // Get field values
        String storeName = view.getNameFieldValue();
        String storeDesc = view.getDescFieldValue();

        // Save and exit only if new values are valid
        if (validateNewValues(storeName, storeDesc)) {
            // Set new values
            model.setStoreData(new StoreData(data.getStoreId(), storeName, storeDesc, data.getProductIds()),
                    () -> {
                        view.displaySavedNotification();
                        back();
                    },
                    exception -> Log.e("StoreEditPagePresenter", exception.getMessage())
            );
        }
    }

    @Override
    public void onCancelButtonClicked() {
        back();
    }
}
