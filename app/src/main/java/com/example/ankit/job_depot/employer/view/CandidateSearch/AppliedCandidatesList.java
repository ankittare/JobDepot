package com.example.ankit.job_depot.employer.view.CandidateSearch;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.employer.model.DAO.EmployerHistory;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppliedCandidatesList extends android.support.v4.app.Fragment {

    private ListView listView;
    private String employerName;
    public AppliedCandidatesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View jobsView = inflater.inflate(R.layout.fragment_applied_candidates_list, container, false);
        listView = (ListView) jobsView.findViewById(R.id.CandidateList);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", getActivity().MODE_PRIVATE);
        employerName = sharedPreferences.getString("employerName", "ankitb");

        EmployerHistory o = new EmployerHistory();
        List<ParseObject> getJobIDs = o.getJobIDs(employerName);
        Log.d("Jobiiid", getJobIDs.toString());
        Log.d("Jobiiid", "hello" + getJobIDs.isEmpty()+getJobIDs.size());

        Map<String , List<ParseObject>> candidateDetails = new HashMap<String , List<ParseObject>>();
        if(!getJobIDs.isEmpty()) {
            List<String> jobIDs = new ArrayList<String>();
            for (ParseObject ids : getJobIDs) {
                jobIDs.add(ids.getObjectId());
                Log.d("Ankadf", "hello" + ids.getObjectId());
                List<ParseObject> candidateList = o.getAppliedCandidateIDs(ids.getObjectId());

                List<String> candID = new ArrayList<String>();
                List<ParseObject> cDetails=null;
                for(ParseObject candidateID: candidateList) {
                    candID.add(candidateID.getString("studentCandidateID"));
                    cDetails = o.getCandidateDetails(candidateID.getString("studentCandidateID"));

                }
                Log.d("Ankadf", "hello" + ids.getObjectId());
                if(cDetails != null && !(cDetails).toString().equals("[]")) {
                    Log.d("candidateDetails", "hello " + cDetails.toString());
                    candidateDetails.put(ids.getObjectId().toString(), cDetails);
                }
            }
        }
//        Log.d("Adfa", listView.toString());
//        Log.d("Abhartha -cand", candidateList.toString());
//        Log.d("Abhartahn", getActivity().toString());
//        listView.setAdapter(new FillCandidateList(getActivity(), candidateList));

//        LinearLayout mainLayout=(LinearLayout)getActivity().findViewById(R.id.searchcandidatefragmenttohide);
//        mainLayout.setVisibility(LinearLayout.GONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                String candidateName = candidateList.get(position).get("username").toString();
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("CandidateName", candidateName);
//                editor.commit();

//                CandidateDetails newFragment = new CandidateDetails();
//                //Toast.makeText(getActivity().getApplicationContext(), "Yea!!! click ho gae called " + position, Toast.LENGTH_SHORT).show();
//
//                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.CandidateDetails, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });
        return  jobsView;
    }


}
