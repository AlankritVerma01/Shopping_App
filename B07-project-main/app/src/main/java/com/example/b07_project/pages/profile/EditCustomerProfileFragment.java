package com.example.b07_project.pages.profile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.example.b07_project.data_classes.CustomerData;

public class EditCustomerProfileFragment extends Fragment {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Button buttonSave;
    private CustomerData oldCustomerData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editprofile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Find the EditText fields and Save button by their IDs
        editTextFirstName = view.findViewById(R.id.editText_first_name);
        editTextLastName = view.findViewById(R.id.editText_last_name);
        buttonSave = view.findViewById(R.id.button_save);

        FirebaseRepository.getCurrentCustomerData(
                user -> {
                    oldCustomerData = user;
                    // Display the data on the EditText fields
                    editTextFirstName.setText(user.getFirstName());
                    editTextLastName.setText(user.getLastName());
                },
                () -> {
                    // Set fields to default text and disable them
                    editTextFirstName.setText("No User Data Found");
                    editTextFirstName.setEnabled(false);
                    editTextLastName.setText("No User Data Found");
                    editTextLastName.setEnabled(false);
                });

        // Set click event for the Save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated name and email values from the EditText fields
                String newFirstName = editTextFirstName.getText().toString().trim();
                String newLastName = editTextLastName.getText().toString().trim();

                CustomerData data = new CustomerData(newFirstName, newLastName, oldCustomerData.getEmail());
                FirebaseRepository.updateCurrentCustomerData(data);
                NavHostFragment.findNavController(EditCustomerProfileFragment.this).popBackStack();
            }
        });
    }
}
