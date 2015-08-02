package com.example.ankit.job_depot.candidate.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.model.DAO.JobDetails;
import com.example.ankit.job_depot.candidate.model.DAO.JobsQuery;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jobs extends android.support.v4.app.Fragment {
    private static final String TAG = "JOBS FRAGMENT";
    private ArrayList<JobDetails> jobDetailses;
    private ExpandableListView listView;
    private TextView nearByJobs;
    private SearchView searchView;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View jobsView = inflater.inflate(R.layout.fragment_jobs, container, false);

        if (jobDetailses == null) {
            jobDetailses = new ArrayList<JobDetails>();
            JobsQuery jobsQuery = new JobsQuery();
            for (ParseObject o : jobsQuery.getJobs()) {
                JobDetails _jd = new JobDetails(o.getObjectId(), o.getString("jobName"), o.getString("jobDesc"), o.getString("jobLocation"));
                jobDetailses.add(_jd);
            }
        }

        ArrayList<String> groupString = new ArrayList<String>();
        for (JobDetails jd : jobDetailses) {
            groupString.add(jd.getJobTitle());
        }

        Map<String, List<String>> childData = new HashMap<String, List<String>>();
        for (JobDetails jd : jobDetailses) {
            List<String> _temp = new ArrayList<String>();
            _temp.add("Description: " + jd.getJobDesc());
            _temp.add("Location: " + jd.getJobLocation());
            childData.put(jd.getJobTitle(), _temp);
        }

        listView = (ExpandableListView) jobsView.findViewById(R.id.expandableListView);
        ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(), groupString, childData);

        listView.setAdapter(expListAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        listView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        nearByJobs = (TextView) jobsView.findViewById(R.id.textView11);
        nearByJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*
               Bring up a new map
               */
                JobLoacationFragment newFragment = new JobLoacationFragment();
                ;
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_Container, newFragment);
                Bundle extras=new Bundle();
                extras.putCharSequence("queryType", "Location");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        searchView = (SearchView) jobsView.findViewById(R.id.searchView);
        //searchView.setQueryHint("SearchView");

        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                JobLoacationFragment newFragment = new JobLoacationFragment();
                ;
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_Container, newFragment);
                Bundle extras=new Bundle();
                extras.putCharSequence("queryType", "Search");
                extras.putCharSequence("queryString", query);
                newFragment.setArguments(extras);
                transaction.addToBackStack(null);
                transaction.commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return jobsView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Activity context;
        private Map<String, List<String>> laptopCollections;
        private List<String> laptops;

        public ExpandableListAdapter(Activity context, List<String> laptops,
                                     Map<String, List<String>> laptopCollections) {
            this.context = context;
            this.laptopCollections = laptopCollections;
            this.laptops = laptops;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String laptop = (String) getChild(groupPosition, childPosition);
            LayoutInflater inflater = context.getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.childlayout, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.text2);
            /*
            ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
            */
            item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to apply?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.i(TAG, "Dialog Onclick");
                                    /*List<String> child =
                                            laptopCollections.get(laptops.get(groupPosition));
                                    child.remove(childPosition);
                                    notifyDataSetChanged();*/
                                    /*
                                    Writing candidate data to Parse Cloud candidateList Table
                                     */
                                    ParseObject candidateApplyObject = new ParseObject("candidateList");
                                    candidateApplyObject.put("jobID", jobDetailses.get(childPosition).getId());
                                    candidateApplyObject.put("status", "Applied");
                                    candidateApplyObject.put("studentCandidateID", sharedPreferences.getString("ObjectId", ""));
                                    candidateApplyObject.saveInBackground();
                                }
                            });
                    builder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            item.setText(laptop);
            return convertView;
        }

        public int getChildrenCount(int groupPosition) {
            return laptopCollections.get(laptops.get(groupPosition)).size();
        }

        public Object getGroup(int groupPosition) {
            return laptops.get(groupPosition);
        }

        public int getGroupCount() {
            return laptops.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String laptopName = (String) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.grouplayout,
                        null);
            }
            TextView item = (TextView) convertView.findViewById(R.id.text1);
            item.setTypeface(null, Typeface.BOLD);
            item.setText(laptopName);
            return convertView;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */

    }

    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


}

