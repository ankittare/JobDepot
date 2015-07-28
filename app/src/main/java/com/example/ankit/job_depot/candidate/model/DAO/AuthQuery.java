package com.example.ankit.job_depot.candidate.model.DAO;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Ankit T on 7/28/2015.
 */
public class AuthQuery {

    private Boolean isAuthenticated;

    public boolean verifyCredential(String username, String password){
        //final Boolean result=null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        query.whereEqualTo("username", username);
        query.whereEqualTo("password", password);
        /*
        Handle synchronization
         */
        ParseObject parseObject=null;
        try {
            parseObject =query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(parseObject!=null)
            isAuthenticated=true;
        else
            isAuthenticated=false;
        return isAuthenticated;
    }
}
