package com.example.ankit.job_depot.candidate.model.DAO;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by Ankit T on 7/28/2015.
 */
public class AuthQuery {

    public  Map.Entry<Boolean,String> verifyCredential(String username, String password) {
        Map.Entry<Boolean, String> entry;
        //final Boolean result=null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        query.whereEqualTo("username", username);
        //query.whereEqualTo("password", password);
        ParseObject parseObject = null;
        try {
            parseObject = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
            entry = new AbstractMap.SimpleEntry<Boolean, String>(false, "Username not found in database");
            return entry;
        }
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("candidateDetails");
        query2.whereEqualTo("username", username);
        query2.whereEqualTo("password", password);
        ParseObject parseObject2 = null;
        try {
            parseObject2 = query2.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
            entry = new AbstractMap.SimpleEntry<Boolean, String>(false, "Password Incorrect");
            return entry;
        }
        return new AbstractMap.SimpleEntry<Boolean, String>(true, "Hey, You logged in!");
    }

    public Boolean verifyCredential(String username){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("candidateDetails");
        query.whereEqualTo("username", username);
        ParseObject parseObject = null;
        try {
            parseObject = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
