package com.example.ankit.job_depot.employer.view.CandidateSearch;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.employer.model.DAO.EmployerHistory;
import com.parse.ParseObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CandidateDetails extends android.support.v4.app.Fragment {

    String imageUrl;
    TextView candidateName, skills, experience, college;
    Button btnCallCandidate;
    String candiName;
    public CandidateDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View jobsView = inflater.inflate(R.layout.fragment_blank, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", getActivity().MODE_PRIVATE);
        candiName = sharedPreferences.getString("CandidateName", "ankit");

        candidateName = (TextView) jobsView.findViewById(R.id.textViewName);
        skills = (TextView) jobsView.findViewById(R.id.textViewSkills);
        experience = (TextView) jobsView.findViewById(R.id.textViewExperience);
        college = (TextView) jobsView.findViewById(R.id.textViewCollegeName);
        btnCallCandidate = (Button) jobsView.findViewById(R.id.buttonCallForInterview);

        LinearLayout mainLayout=(LinearLayout)getActivity().findViewById(R.id.CandidateDetailstohide);
        mainLayout.setVisibility(LinearLayout.GONE);

        EmployerHistory candidateDetails = new EmployerHistory();
        Log.d("Abharthakjkljkjl", candiName);
        ParseObject candidateDetail = candidateDetails.getCandidate(candiName);

        candidateName.setText(candidateDetail.getString("username"));
        skills.setText(candidateDetail.getString("skills"));
        experience.setText(candidateDetail.getString("workexp"));
        college.setText(candidateDetail.getString("education"));

        return  jobsView;
    }


}
