package com.example.ankit.job_depot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ankit on 7/9/2015.
 */
public class Settings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View settingsVIew=inflater.inflate(R.layout.fragment_resume, container, false);
        return settingsVIew;
    }
}
