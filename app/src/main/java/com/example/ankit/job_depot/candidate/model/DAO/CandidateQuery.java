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

    public ParseObject getCandidateDetails(String ID) {
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



}
