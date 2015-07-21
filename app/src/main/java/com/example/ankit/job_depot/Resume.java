package com.example.ankit.job_depot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.DeepLinkListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ankit on 7/9/2015.
 */
public class Resume extends Fragment {

    private static final String TAG = "Resume Fragment";
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl ="https://api.linkedin.com/v1/people/~:(id,first-name,skills,educations,languages,twitter-accounts)?format=json";
    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View resumeView = inflater.inflate(R.layout.fragment_resume, container, false);
        final TextView textView = (TextView) resumeView.findViewById(R.id.textView);
        APIHelper apiHelper = null;
        Context context = null;
        try {
            context = getActivity().getApplicationContext();
        } catch (NullPointerException n) {
            Log.e(TAG, "I knew it would give null pointer");
        }
        if (context != null) {
            apiHelper = APIHelper.getInstance(context);
            apiHelper.getRequest(context, topCardUrl, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    Log.i(TAG, "API sucess");
                    JSONObject jsonObject=apiResponse.getResponseDataAsJson();
                    Log.i(TAG, jsonObject.toString());
                    try {
                        textView.setText(jsonObject.get("firstName").toString());
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
        return resumeView;
    }
//To get call back


}
