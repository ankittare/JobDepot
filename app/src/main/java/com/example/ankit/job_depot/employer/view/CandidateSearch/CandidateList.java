package com.example.ankit.job_depot.employer.view.CandidateSearch;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.employer.model.DAO.EmployerHistory;
import com.example.ankit.job_depot.employer.view.CandidateSearch.FillCandidateList;
import com.parse.ParseObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CandidateList extends android.support.v4.app.Fragment {

    private ListView listView;
    List<ParseObject> candidateList;

    public CandidateList() {}
    public CandidateList(List<ParseObject> candidateList) {
        this.candidateList =candidateList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View jobsView = inflater.inflate(R.layout.fragment_candidate_list, container, false);
        listView = (ListView) jobsView.findViewById(R.id.CandidateList);
        Log.d("Adfa", listView.toString());
        Log.d("Abhartha -cand", candidateList.toString());
        Log.d("Abhartahn", getActivity().toString());
        listView.setAdapter(new FillCandidateList(getActivity(), candidateList));

        LinearLayout mainLayout=(LinearLayout)getActivity().findViewById(R.id.searchcandidatefragmenttohide);
        mainLayout.setVisibility(LinearLayout.GONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String candidateName = candidateList.get(position).get("username").toString();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("CandidateName", candidateName);
                editor.commit();

                CandidateDetails newFragment = new CandidateDetails();
                //Toast.makeText(getActivity().getApplicationContext(), "Yea!!! click ho gae called " + position, Toast.LENGTH_SHORT).show();

                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.CandidateDetails, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return  jobsView;
    }

    @Override
    public void onPause() {
        super.onPause();
        LinearLayout mainLayout=(LinearLayout)getActivity().findViewById(R.id.searchcandidatefragmenttohide);
        mainLayout.setVisibility(LinearLayout.VISIBLE);
    }
}
