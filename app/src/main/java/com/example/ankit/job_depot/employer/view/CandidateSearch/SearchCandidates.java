package com.example.ankit.job_depot.employer.view.CandidateSearch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.employer.model.DAO.EmployerHistory;
import com.parse.ParseObject;

import java.util.List;

public class SearchCandidates extends Fragment {

    String keyword;
    Button SearchButton,AppliedCandidates;
    ImageButton SearchImageButton;
    EditText SearchField, SearchLoc, SearchSkills, SearchExp;
    String employerName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View jobsView = inflater.inflate(R.layout.fragment_search_candidates, container, false);

        SearchField = (EditText)jobsView.findViewById(R.id.editTextSearch);
        SearchLoc = (EditText)jobsView.findViewById(R.id.editTextSearchByLocation);
        SearchSkills = (EditText)jobsView.findViewById(R.id.editTextSearchSkills);
        SearchExp = (EditText)jobsView.findViewById(R.id.editTextSearchExp);
        SearchButton = (Button)jobsView.findViewById(R.id.buttonSearch);
        SearchImageButton = (ImageButton)jobsView.findViewById(R.id.imageButtonSearch);
        AppliedCandidates = (Button)jobsView.findViewById(R.id.buttonAppliedCandidates);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", getActivity().MODE_PRIVATE);
        employerName = sharedPreferences.getString("employerName", "ankitb");

        SearchButton.setOnClickListener(onClickListener);
        SearchImageButton.setOnClickListener(onClickListener);
        AppliedCandidates.setOnClickListener(appliedCandidatesClicked);


        return jobsView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Abhartha", "onPause of Search Candidate");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Abhartha", "onDetach of Search Candidate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Abhartha", "onDestroy of Search Candidate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Abhartha", "onResume of Search Candidate");

    }
    private View.OnClickListener appliedCandidatesClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppliedCandidatesList newFragment = new AppliedCandidatesList();
            android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.searchcandidatefragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imageButtonSearch:
                case R.id.buttonSearch:
                    Log.d("ABhartha", SearchField.getText().toString());
                    Log.d("ABhartha", SearchLoc.getText().toString());
                    Log.d("ABhartha", SearchSkills.getText().toString());
                    Log.d("ABhartha", SearchExp.getText().toString());
                    Log.d("ABhartha",  "" + SearchExp.getText().toString().equals(""));
                    if(SearchField.getText().toString().equals("") && SearchLoc.getText().toString().equals("")
                            && SearchSkills.getText().toString().equals("")  && SearchExp.getText().toString().equals("") ) {
                        Toast.makeText(getActivity(),
                                "Enter some keywords you want to search",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        EmployerHistory candidateList = new EmployerHistory();
                        CandidateList newFragment = null;
                        if(!(SearchField.getText().toString().equals(""))) {
                            keyword = (SearchField.getText().toString());
                            List<ParseObject> o = candidateList.getCandidates(keyword);
                            newFragment =  new CandidateList(o);
                            SearchField.setText("");
                        }
                        if(!(SearchLoc.getText().toString().equals(""))) {
                            keyword = (SearchLoc.getText().toString().toLowerCase());
                            List<ParseObject> o = candidateList.getCandidatesByLocation(keyword);
                            newFragment =  new CandidateList(o);
                            SearchLoc.setText("");
                        }
                        if(!(SearchSkills.getText().toString().equals("") )){
                            keyword = (SearchSkills.getText().toString().toLowerCase());
                            List<ParseObject> o = candidateList.getCandidatesByskills(keyword);
                            newFragment =  new CandidateList(o);
                            SearchSkills.setText("");
                        }
                        if(!(SearchExp.getText().toString().equals(""))){
                            keyword = (SearchExp.getText().toString().toLowerCase());
                            List<ParseObject> o = candidateList.getCandidatesByExperience(keyword);
                            newFragment =  new CandidateList(o);
                            SearchExp.setText("");
                        }


                        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.searchcandidatefragment, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    }
            }
        }
    };
}
