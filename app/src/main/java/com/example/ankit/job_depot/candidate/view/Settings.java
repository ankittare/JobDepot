package com.example.ankit.job_depot.candidate.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ankit.job_depot.R;

/**
 * Created by Ankit on 7/9/2015.
 */
public class Settings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View settingsVIew=inflater.inflate(R.layout.fragment_settings, container, false);
        return settingsVIew;
    }
}
