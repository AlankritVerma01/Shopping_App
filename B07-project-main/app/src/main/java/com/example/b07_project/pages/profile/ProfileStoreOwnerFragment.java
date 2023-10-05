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
import android.widget.TextView;

import com.example.b07_project.FirebaseRepository;
import com.example.b07_project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileStoreOwnerFragment extends Fragment {
    private TextView textViewFullName;
    private TextView textViewEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Load data from Firebase and display it on the fragment
        loadUserData();

        // Find the TextViews in your layout to display the data
        textViewFullName = view.findViewById(R.id.textView_show_full_name);
        textViewEmail = view.findViewById(R.id.textView_show_email);

        Button buttonLogout = view.findViewById(R.id.logout_button);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseRepository.logout();
                getActivity().finish();
            }
        });

        // Set click event for the Edit button
        FloatingActionButton buttonEdit = view.findViewById(R.id.floatingActionButton);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Replace the current fragment with the EditProfileFragment
                NavHostFragment.findNavController(ProfileStoreOwnerFragment.this).navigate(R.id.action_profileStoreOwnerFragment_to_editProfileFragment);
            }
        });
    }

    private void loadUserData() {
        String uid = FirebaseRepository.getUserId();

        // Display error values if user not logged in
        if (uid == null) {
            // No authenticated user found
            // Handle the case where the user is not authenticated
            textViewFullName.setText("User Not Authenticated");
            textViewEmail.setText("User Not Authenticated");
            return;
        }

        FirebaseRepository.getCurrentSellerData(
                user -> {
                    // Get the "name" and "email" values for the current user
                    String first_name = user.getFirstName();
                    String last_name = user.getLastName();
                    String email = user.getEmail();
                    String name = first_name + " " + last_name;
                    // Display the data on the TextViews
                    textViewFullName.setText(name);
                    textViewEmail.setText(email);
                },
                () -> {
                    // Data for the current user does not exist in the database
                    // Handle the case where the user data is not found
                    textViewFullName.setText("No User Data Found");
                    textViewEmail.setText("No User Data Found");
                });
    }
}
