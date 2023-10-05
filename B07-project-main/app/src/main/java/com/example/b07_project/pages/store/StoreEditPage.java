package com.example.b07_project.pages.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b07_project.R;
import com.example.b07_project.mvp_interface.StoreEditPageContract;
import com.example.b07_project.model.StoreEditPageModel;
import com.example.b07_project.presenter.StoreEditPagePresenter;
import com.example.b07_project.util.StoreErrors;

public class StoreEditPage extends Fragment implements StoreEditPageContract.View {
    // Page element variables
    private EditText nameField;
    private EditText descField;

    private StoreEditPageContract.Presenter presenter;

    public StoreEditPage() {
        // Required empty public constructor
    }

    private void initPresenter(String storeId) {
        presenter = new StoreEditPagePresenter(
                this,
                new StoreEditPageModel(storeId)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_edit_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Grab layout elements
        nameField = view.findViewById(R.id.field_store_name);
        descField = view.findViewById(R.id.field_store_desc);

        // Return immediately if storeId is not argument
        if (getArguments() == null || !getArguments().containsKey("storeId")) {
            return ;
        }

        initPresenter(getArguments().getString("storeId"));

        // Add click listener to buttons
        view.findViewById(R.id.save_button).setOnClickListener(v -> presenter.onSaveButtonClicked());
        view.findViewById(R.id.cancel_button).setOnClickListener(v -> presenter.onCancelButtonClicked());
    }

    @Override
    public void updatePage() {
        // Initialise text in fields
        nameField.setText(presenter.getOldStoreName());
        descField.setText(presenter.getOldStoreDesc());
    }

    @Override
    public void nameError(StoreErrors.NameError error) {
        switch (error) {
            case EMPTY:
                nameField.setError("Store name cannot be empty.");
                break;
            case TOO_LONG:
                nameField.setError("Store name cannot contain more than 40 characters.");
                break;
        }
    }

    @Override
    public void descError(StoreErrors.DescError error) { }

    @Override
    public String getNameFieldValue() {
        return nameField.getText().toString();
    }

    @Override
    public String getDescFieldValue() {
        return descField.getText().toString();
    }

    @Override
    public void navigateBack() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void displaySavedNotification() {
        Toast.makeText(this.getContext(), "Saved!", Toast.LENGTH_SHORT).show();
    }
}