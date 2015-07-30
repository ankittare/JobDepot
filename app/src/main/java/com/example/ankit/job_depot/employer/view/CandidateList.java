package com.example.ankit.job_depot.employer.view;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ankit.job_depot.R;
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
        return  jobsView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
