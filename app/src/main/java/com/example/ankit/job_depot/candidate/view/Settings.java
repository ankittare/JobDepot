package com.example.ankit.job_depot.candidate.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.controller.DownloadImageTask;
import com.example.ankit.job_depot.candidate.model.DAO.CandidateQuery;

/**
 * Created by Ankit on 7/9/2015.
 */
public class Settings extends Fragment {
    private final String TAG=getClass().getSimpleName();
    private EditText skills, education, work, password;
    private Button apply;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;
    private String username, pictureURL;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View settingsVIew=inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        if(sharedPreferences.contains("username"))
            username=sharedPreferences.getString("username", "");
        else
            Log.i(TAG, "I hate shared preferences");

        if(sharedPreferences.contains("pictureURL"))
            pictureURL=sharedPreferences.getString("pictureURL", "");
        else
            Log.i(TAG, "I hate shared preferences");

        init(settingsVIew);
        return settingsVIew;
    }
    public void init(View view){
        skills=(EditText)view.findViewById(R.id.editText2);
        education=(EditText)view.findViewById(R.id.editText3);
        work=(EditText)view.findViewById(R.id.editText);
        password=(EditText)view.findViewById(R.id.editText4);

        imageView=(ImageView) view.findViewById(R.id.imageView);
        try{
            DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
            downloadImageTask.execute(pictureURL);
            imageView = downloadImageTask.getBmImage();
            imageView.setOnClickListener(new ImageListener());
        }
       catch(NullPointerException ne){
           ne.printStackTrace();
       }


        apply=(Button)view.findViewById(R.id.button);
        apply.setOnClickListener(new ApplyListener());

    }
    public Boolean saveData() {
        CandidateQuery candidateQuery = new CandidateQuery();
        try{
            Log.i(TAG, skills.getText().toString());
            return candidateQuery.updateProfile(username,skills.getText().toString(),education.getText().toString(), work.getText().toString(), password.getText().toString()  );
        }
       catch(NullPointerException ne){
           Log.i(TAG, "???");
           return false;
       }

    }
    private class ApplyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(saveData())
                 Toast.makeText(getActivity().getBaseContext(),"Update Sucessfull", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity().getBaseContext(),"Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
    private class ImageListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            /*
            code for uploading image from phone
             */
        }
    }
}
