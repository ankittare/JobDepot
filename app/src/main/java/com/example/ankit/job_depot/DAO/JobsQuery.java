package com.example.ankit.job_depot.DAO;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ankit.T on 7/26/2015.
 */
public class JobsQuery {



    public List<Map<String,String>> getSavedJobs(String candidateID){
        final List<Map<String, String>> listSavedJobs=new ArrayList<Map<String, String>>();
        Log.i("Background Query ", "getSavedJobs");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateList");
        query.whereEqualTo("studentCandidateID", candidateID);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject o : list) {
                        Map<String, String> result=new HashMap<String, String>();
                        result.put("jobID", o.getString("jobID"));
                        result.put("status", o.getString("status"));
                        result.put("timesaved", o.getString("createdAt"));
                        result.putAll(getJobDetails(result.get("jobID")));
                        listSavedJobs.add(result);
                        Log.i("getSavedJobs", result.get("jobID"));

                    }

                } else {
                    e.printStackTrace();
                }
                for(Map<String, String> map:listSavedJobs){
                    Log.i("",map.entrySet().toString());
                }
            }
        });

        return listSavedJobs;
    }

    public Map<String, String> getJobDetails(String jobID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        final Map<String, String> result=new HashMap<String, String>();
        query.whereEqualTo("objectId", jobID);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject o : list) {

                        result.put("jobTitle", o.getString("jobName"));
                        result.put("jobdesc", o.getString("jobDesc"));
                        result.put("jobLocation", o.getString("jobLocation"));
                         Log.i("getJobDetails", o.getString("jobName"));
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return result;
    }
}
