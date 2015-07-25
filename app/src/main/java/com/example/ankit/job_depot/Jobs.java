package com.example.ankit.job_depot;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class JobDetails{
    private String jobTitle;
    private String jobDesc;
    private String jobLocation;

    public JobDetails(@NonNull String jobTitle, @NonNull String jobDesc, @NonNull String jobLocation){
        this.jobTitle=jobTitle;
        this.jobDesc=jobDesc;
        this.jobLocation=jobLocation;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public String getJobLocation() {
        return jobLocation;
    }
}

public class    Jobs extends android.support.v4.app.Fragment {
    private static final String TAG="JOBS FRAGMENT";
    private final List<JobDetails> jobDetails=new ArrayList<JobDetails>();
    private boolean lock=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View jobsView=inflater.inflate(R.layout.fragment_jobs, container, false);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        final List<JobDetails> jobsList=new ArrayList<JobDetails>();

       // synchronized (this){
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, com.parse.ParseException e) {
                    if (e == null) {
                        for (ParseObject o : list) {
                            JobDetails _jd=new JobDetails(o.getString("jobName"), o.getString("jobDesc"), o.getString("jobLocation"));
                            jobsList.add(_jd);
                            Log.i("Background",o.getString("jobName") );
                            Log.d("ID", o.getObjectId());
                        }
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                    lock=true;
                }
            });

           // this.notifyAll();
        //}


        /*synchronized (this){
            while(!lock){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            ExpandableListView listView=(ExpandableListView)jobsView.findViewById(R.id.expandableListView);
            Log.i(TAG, ""+jobsList.size());
            for(JobDetails jd:jobsList){
                TextView textView=new TextView(getActivity().getApplicationContext());
                Log.i(TAG, jd.getJobTitle());
                textView.setText(jd.getJobTitle());
                listView.addView(textView);
            }
//        }


        return jobsView;
    }
}
