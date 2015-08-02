package com.example.ankit.job_depot.candidate.model.DAO;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Ankit T. on 7/27/2015.
 */
public class CandidateQuery {

    private final String TAG=getClass().getSimpleName();
    public ParseObject getCandidateDetails(@NonNull String ID) {
        ParseObject result=null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        /*
        put actual ID here
         */
        query.whereEqualTo("objectId",ID );
        // synchronized (this){]
        try {
            result=query.getFirst();
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        ///Log.i("CandidateQUery", result.get(0).get("workexp")+"");
    }

    /*
    How to deal with users with same username?
     */
    public String getObjectId(@NonNull String username){
        Log.i(TAG, username);
        ParseObject result=null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        query.whereEqualTo("username",username);
        try {
            result=query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("CandidateQUery", result.getObjectId());
        return result.getObjectId();
    }

    public Boolean createUser(@NonNull String username){
        ParseObject newUser=new ParseObject("candidateDetails");
        newUser.put("username", username);
        try {
            newUser.save();
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    public Boolean updateProfile(@NonNull String username, String skills, String education, String work, String password){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        query.whereEqualTo("username", username);
        ParseObject newUser= null;
        try {
            newUser = query.getFirst();
            if(skills!=null && (!skills.equals(""))) newUser.put("skills", skills);
            if(education!=null && (!education.equals(""))) newUser.put("education", education);
            if(work!=null && (!work.equals(""))) newUser.put("workexp", work);
            if(password!=null && (!password.equals(""))) newUser.put("password", password);
            newUser.save();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
