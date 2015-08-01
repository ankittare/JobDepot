package com.example.ankit.job_depot.candidate.controller;

import android.content.Context;

/**
 * Created by Ankit on 8/1/2015.
 */
public class LinkedInAPIHelper {
    private final String TAG=getClass().getSimpleName();
    private String email;
    public Context context;
    public LinkedInAPIHelper(Context c){
        context=c;
    }
   /* public String getEmailaddress() {
        String topCardUrl = "https://api.linkedin.com/v1/people/~:(id,firstName,headline,positions,picture-url)?format=json";
        APIHelper apiHelper = null;
        Log.i(TAG, "Context="+context.toString());
        if (context != null) {
            Log.i(TAG, "------------");
            apiHelper = APIHelper.getInstance(context);
            apiHelper.getRequest(context, topCardUrl, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    Log.i(TAG, "API sucess");

                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    Log.i(TAG, jsonObject.toString());
                    try {
                        email=jsonObject.getString("email-address");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onApiError(LIApiError LIApiError) {
                    Log.i(TAG, LIApiError.toString());
                }
            });
        }
        return email;
    }*/
}
