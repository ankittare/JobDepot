package com.example.ankit.job_depot.candidate.model.DAO;

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
    String employerName;
    public EmployerHistory(){}
    public EmployerHistory(String employerName) {
        this.employerName = employerName;
    }
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
    public List<ParseObject> getJobs(String employerName) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(ParseObject parseObject:result){
            Log.i("getJobs",parseObject.getString("jobName") );
        }
        return result;
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
}
