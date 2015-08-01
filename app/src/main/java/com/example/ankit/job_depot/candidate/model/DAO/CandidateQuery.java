package com.example.ankit.job_depot.candidate.model.DAO;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit T. on 7/27/2015.
 */
public class CandidateQuery {

    public ParseObject getCandidateDetails(String ID) throws ArrayIndexOutOfBoundsException {
        List<ParseObject> result=new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        /*
        put actual ID here
         */
        query.whereEqualTo("objectId",ID );
        // synchronized (this){]
        try {
            result=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("CandidateQUery", result.get(0).get("workexp")+"");
        return result.get(0);
    }

    /*
    How to deal with users with same username?
     */
    public String getObjectId(String username){
        ParseObject result=null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        if(username.contains("@"))
            query.whereEqualTo("email",username);
        else
            query.whereEqualTo("username",username);
        try {
            result=query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("CandidateQUery", result.getObjectId());
        return result.getObjectId();
    }
}
