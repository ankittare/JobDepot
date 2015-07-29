package com.example.ankit.job_depot.employer.model.DAO;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by anjali on 7/28/15.
 */
public class EmployerHistory {
    public synchronized Map<String, String> getJobDetails(String employerName){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        final Map<String, String> result=new HashMap<String, String>();
        query.whereEqualTo("employerName", employerName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject o : list) {
                        result.put("jobTitle", o.getString("jobName"));
                        result.put("jobdesc", o.getString("jobDesc"));
                        result.put("jobLocation", o.getString("jobLocation"));
                        result.put("updatedAt",o.getString("updatedAt"));
                        Log.i("getJobDetails", o.getString("jobName"));
                    }
                } else {
                    e.printStackTrace();
                }
                Log.i("getJobDetails", result.entrySet().toString());
            }
        });
        notifyAll();
        return result;
    }
    public ParseObject getJobInfo(String employerName, int position){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        List<ParseObject> queryString=null;
        query.whereEqualTo("employerName", employerName);
        try {
            queryString=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return queryString.get(position);
    }

    public List<ParseObject> getJobHistory(String employerName){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        List<ParseObject> queryString=null;
        query.whereEqualTo("employerName", employerName);
        try {
            queryString=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return queryString;
    }

    public List<Map<String,String>> getPostedJobs(String employerName){
        final List<Map<String, String>> listSavedJobs=new ArrayList<Map<String, String>>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        Log.d("Abhartha", query.toString());
        query.whereEqualTo("employerName", employerName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject o : list) {
                        Log.d("Abharthan", o.toString());
                        Map<String, String> result = new HashMap<String, String>();
                        result.put("jobID", o.get("expRequired").toString());
                        Log.d("Abhartha", o.get("expRequired").toString());
                        result.put("status", o.getString("jobType"));
                        Log.d("Abhartha", o.getString("jobType"));
                        result.put("timesaved", o.getString("jobStartDate"));
                        Log.d("Abhartha", o.getString("jobStartDate"));
                        result.put("jobTitle", o.getString("jobName"));
                        Log.d("Abhartha", o.getString("jobName"));
                        result.put("jobdesc", o.getString("jobDesc"));
                        Log.d("Abhartha", o.getString("jobDesc"));
                        result.put("jobLocation", o.getString("jobLocation"));
                        Log.d("Abhartha", o.getString("jobLocation"));
                        listSavedJobs.add(result);
                        Log.d("Ankit123", listSavedJobs.toString());
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });
        Log.d("Abhartha", listSavedJobs.toString());
        return listSavedJobs;
    }

    public List<ParseObject> getJobs(String employerName) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        query.whereEqualTo("employerName", employerName);
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
