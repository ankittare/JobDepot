package com.example.ankit.job_depot.employer.model.DAO;

import android.text.LoginFilter;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
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
    public ParseObject getCandidate(String candidateName){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        List<ParseObject> queryString=null;
        query.whereEqualTo("username", candidateName);
        Log.d("Abhartha", query.toString());

        try {
            queryString=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("Abhartha", queryString.toString());
        return queryString.get(0);
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

    public List<ParseObject> getCandidatesByskills(String keyword) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        String pattern = "^.*" + keyword + ".*$";
        query.whereMatches("skills", keyword, "i");
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ParseObject> getCandidatesByLocation(String keyword) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        String pattern = "^.*" + keyword + ".*$";
        query.whereMatches("preferredJobLocation", keyword, "i");
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ParseObject> getCandidatesByExperience(String keyword) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        String pattern = "^.*" + keyword + ".*$";
        query.whereMatches("workexp", keyword, "i");
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ParseObject> getCandidates(String keyword) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        String pattern = "^.*" + keyword + ".*$";

        ParseQuery<ParseObject> querySkills = ParseQuery.getQuery("candidateDetails");
        querySkills.whereMatches("skills", keyword, "i");

        ParseQuery<ParseObject> queryExperience = ParseQuery.getQuery("candidateDetails");
        queryExperience.whereMatches("workexp", keyword.toLowerCase(), "i");

        ParseQuery<ParseObject> queryEducation = ParseQuery.getQuery("candidateDetails");
        queryEducation.whereMatches("education", keyword.toUpperCase(), "i");

        ParseQuery<ParseObject> queryCombined = ParseQuery.or(Arrays.asList(querySkills, queryExperience, queryEducation));

        try {
            result=queryCombined.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ParseObject> getCandidatesAppliedList(String employerName) {
        List<ParseObject> result = new ArrayList<ParseObject>();
        List<ParseObject> postedJobs = new ArrayList<ParseObject>();
        List<String> postedJobIDs = new ArrayList<String>();
        ParseQuery employerID = ParseQuery.getQuery("JobDetails");
        employerID.whereEqualTo("employerName", employerName);
        try {
            postedJobs = employerID.find();
            for(int i=0; i< postedJobs.size(); i++) {

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ParseObject> getJobIDs(String employerName) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        Log.d("getJobIDs", employerName);
        query.whereEqualTo("employerName", employerName);
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("getJobIDs", result.toString());
        return result;
    }

    public List<ParseObject> getAppliedCandidateIDs(String jobID) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateList");
        Log.d("getJobIDs", jobID);
        query.whereEqualTo("jobID", jobID);
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("getJobIDs", result.toString());
        return result;
    }

    public List<ParseObject> getCandidateDetails(String candidateID) {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        Log.d("getJobIDs", candidateID);
        query.whereEqualTo("objectId", candidateID);
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("objectID", result.toString());
        return result;
    }

    public ParseObject getSingleCandidate(String parseObject) {
        ParseObject result=null;
        ParseQuery query = ParseQuery.getQuery("candidateDetails");
        query.whereEqualTo("objectId", parseObject);
        try {
            result=query.get(parseObject);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return result;
    }
    public ParseObject getSingleJobDetail(String parseObject) {
        ParseObject result=null;
        ParseQuery query = ParseQuery.getQuery("JobDetails");
        query.whereEqualTo("objectId", parseObject);
        try {
            result=query.get(parseObject);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return result;
    }
}
