package com.example.ankit.job_depot.candidate.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.controller.CandidateController;
import com.example.ankit.job_depot.candidate.model.DAO.CandidateQuery;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Ankit on 7/9/2015.
 */
public class Resume extends Fragment {
    private String usernameID;
    private static final String TAG = "Resume Fragment";

    /*
    LinkedIn specific
     */
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://api.linkedin.com/v1/people/~:(id,first-name,skills,educations,languages,twitter-accounts)?format=json";
    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";
    /*
    Parse Specific
     */
    private ParseObject candidateDetails;
    private CandidateController candidateController;

    /*
    Views
     */
    LinearLayout workexp, skills;
    RelativeLayout education;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View resumeView = inflater.inflate(R.layout.fragment_resume, container, false);
        /*
        Getting data from LinkedIn
         */
        /*final TextView textView = (TextView) resumeView.findViewById(R.id.textView);
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
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
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
        }*/
        /*
        Getting user data from Parse
         */

        try {
            Bundle extras = getActivity().getIntent().getExtras();
            usernameID = extras.getString("usernameID");
        } catch (NullPointerException nne) {
            nne.printStackTrace();
        }


        initializeView(resumeView);
        fillEducationData();
        fillSkillsData();
        fillWorkData();
        return resumeView;
    }

    private void initializeView(View v) {
        education = (RelativeLayout) v.findViewById(R.id.educationLayout);
        workexp = (LinearLayout) v.findViewById(R.id.workExpLayout);
        skills = (LinearLayout) v.findViewById(R.id.skillsLayout);

        CandidateQuery candidateQuery = new CandidateQuery();

        /*
        get Actual Id from somewhere
         */

        candidateDetails = candidateQuery.getCandidateDetails("vGx1f5ygQf");
        Log.i(TAG, candidateDetails.getString("username"));
        candidateController = new CandidateController(candidateDetails);
    }

    private void fillEducationData() {
        List<String> educationList = candidateController.getEducation();
        EditText[] editTexts = new EditText[educationList.size()];
        TableRow tableRow=new TableRow(getActivity().getApplicationContext());
        tableRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int index = 0;
        for (EditText e : editTexts) {
            e=new EditText(getActivity().getApplicationContext());
            e.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Log.i(TAG, educationList.get(index));
            e.setText(educationList.get(index++));
            e.setTextColor(Color.parseColor("#000000"));
            //e.setFocusable(false);
            tableRow.addView(e);
        }
        education.addView(tableRow);
    }

    private void fillSkillsData() {
        List<String> skillsList = candidateController.getSkills();
        EditText[] editTexts = new EditText[skillsList.size()];
        TableRow tableRow=new TableRow(getActivity().getApplicationContext());
        tableRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int index = 0;
        for (EditText e : editTexts) {
            e=new EditText(getActivity().getApplicationContext());
            e.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            e.setTextColor(Color.parseColor("#000000"));
            Log.i(TAG, skillsList.get(index));
            e.setText(skillsList.get(index++));
            //e.setFocusable(false);
            tableRow.addView(e);
        }
        skills.addView(tableRow);
    }

    private void fillWorkData() {
        Integer workexperience = candidateController.getWorkExp();
        TableRow tableRow=new TableRow(getActivity().getApplicationContext());
        tableRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        EditText editText = new EditText(getActivity().getApplicationContext());
        editText.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setText("" + workexperience);
        editText.setTextColor(Color.parseColor("#000000"));
        editText.setFocusable(false);
        tableRow.addView(editText);
        workexp.addView(tableRow);
    }


}
