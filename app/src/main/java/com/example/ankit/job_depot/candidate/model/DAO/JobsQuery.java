package com.example.ankit.job_depot.candidate.model.DAO;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.ParseException;
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
    private final String TAG=getClass().getSimpleName();
    public List<Map<String, String>> getSavedJobs(String username) {
        String candidateID = new CandidateQuery().getObjectId(username);
        Log.i(TAG, candidateID);
        final List<Map<String, String>> listSavedJobs = new ArrayList<Map<String, String>>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateList");
        query.whereEqualTo("studentCandidateID", candidateID);
        List<ParseObject> list = null;
        try {
            list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (list != null) {
            for (ParseObject o : list) {
                Map<String, String> result = new HashMap<String, String>();
                result.put("jobID", o.getString("jobID"));
                Log.i(TAG, o.getString("jobID"));
                result.put("status", o.getString("status"));
                        /*
                        getting Job details
                         */
                ParseObject job = getJob(result.get("jobID"));
                result.put("jobTitle", job.getString("jobName"));
                result.put("jobdesc", job.getString("jobDesc"));
                result.put("jobLocation", job.getString("jobLocation"));
                listSavedJobs.add(result);

            }
        }
        return listSavedJobs;
    }


    public ParseObject getJob(@NonNull String jobID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        ParseObject queryString = null;
        query.whereEqualTo("objectId", jobID);
        try {
            queryString = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // notifyAll();
        return queryString;
    }

    public List<ParseObject> getJobs() {
        List<ParseObject> result = new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        try {
            result = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (ParseObject parseObject : result) {
            Log.i("getJobs", parseObject.getString("jobName"));
        }
        return result;
    }
    /*
    searching for job in candidate list
     */
    public Boolean isJobApplied(@NonNull String jobID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateList");
        ParseObject queryString = null;
        query.whereEqualTo("jobID", jobID);
        try {
            queryString = query.getFirst();
        } catch (ParseException e) {
            Log.e(TAG, "Some exceptions are meant to happen in Life");
        }
        // notifyAll();
       if(queryString!=null)
           return true;
        else
           return false;
    }
}
