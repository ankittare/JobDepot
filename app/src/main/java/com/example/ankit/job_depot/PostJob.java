package com.example.ankit.job_depot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostJob extends Fragment {
    Button btnPostJob;
    EditText ETJobTitle, ETCompanyname, ETNumPos,ETJobDesc,ETJobLocation;
    DatePicker DPJobDate;
    Switch SJobType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View jobsView = inflater.inflate(R.layout.fragment_post_job, container, false);
        ETJobTitle = (EditText)jobsView.findViewById(R.id.editTextJobTitle);
        ETCompanyname = (EditText)jobsView.findViewById(R.id.editTextCompanyName);
        ETJobDesc = (EditText)jobsView.findViewById(R.id.editTextJobDesc);
        ETJobLocation = (EditText)jobsView.findViewById(R.id.editTextJobLocation);
        btnPostJob = (Button)jobsView.findViewById(R.id.buttonPostJob);
        DPJobDate = (DatePicker)jobsView.findViewById(R.id.datePicker);


        return jobsView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

