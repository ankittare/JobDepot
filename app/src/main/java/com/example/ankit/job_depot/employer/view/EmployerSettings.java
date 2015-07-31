package com.example.ankit.job_depot.employer.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ankit.job_depot.R;

public class EmployerSettings extends Fragment {
    String employerName;
    Button btnUpdate;
    EditText UpdateUserName, UpdatePassword, UpdateEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View empSettings = inflater.inflate(R.layout.fragment_employer_settings, container, false);
        UpdateUserName = (EditText)empSettings.findViewById(R.id.editTextUpdateUsername);
        UpdatePassword = (EditText)empSettings.findViewById(R.id.editTextUpdatePassowrd);
        UpdateEmail = (EditText)empSettings.findViewById(R.id.editTextUpdateEmail);
        btnUpdate = (Button)empSettings.findViewById(R.id.buttonUpdate);

        // Check if no view has focus:
//        View view = getActivity().getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)(getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", getActivity().MODE_PRIVATE);
        employerName = sharedPreferences.getString("employerName", "ankitb");

        //btnUpdate.setOnClickListener(onClickListener);
        return empSettings;
    }
}
