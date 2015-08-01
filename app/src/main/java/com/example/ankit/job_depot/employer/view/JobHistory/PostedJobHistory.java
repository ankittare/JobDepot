package com.example.ankit.job_depot.employer.view.JobHistory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.employer.model.DAO.EmployerHistory;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JobDetails {
    private String ID;
    private String jobTitle;
    private String jobDesc;
    private String jobLocation;

    public JobDetails(@NonNull String id, @NonNull String jobTitle, @NonNull String jobDesc, @NonNull String jobLocation) {
        this.ID=id;
        this.jobTitle = jobTitle;
        this.jobDesc = jobDesc;
        this.jobLocation = jobLocation;
    }

    public String getId() {
        return ID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public String getJobLocation() {
        return jobLocation;
    }
}

public class PostedJobHistory extends Fragment {
    private ExpandableListView expandableListView;
    private List<Map<String, String>> savedjobsData=null;
    private List<String> groupData;
    private ArrayList<JobDetails> jobDetails;


    String employerName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View jobsView = inflater.inflate(R.layout.fragment_posted_job_history, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", getActivity().MODE_PRIVATE);
        employerName = sharedPreferences.getString("employerName", "ankitb");

        if(jobDetails==null){
            jobDetails=new ArrayList<JobDetails>();
            EmployerHistory jobsQuery=new EmployerHistory();
            for(ParseObject o:jobsQuery.getJobs(employerName)){
                JobDetails _jd = new JobDetails( o.getObjectId(),o.getString("jobName"), o.getString("jobDesc"), o.getString("jobLocation"));
                jobDetails.add(_jd);
            }
        }

        ArrayList<String> groupString=new ArrayList<String>();
        for(JobDetails jd:jobDetails){
            groupString.add(jd.getJobTitle());
        }

        Map<String, List<String>> childData=new HashMap<String, List<String>>();
        for(JobDetails jd:jobDetails){
            List<String> _temp=new ArrayList<String>();
            _temp.add("Description: "+jd.getJobDesc());
            _temp.add("Location: "+jd.getJobLocation());
            childData.put(jd.getJobTitle(),_temp);
        }
        expandableListView = (ExpandableListView) jobsView.findViewById(R.id.expandableListViewJobHistory);
        ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(), groupString, childData);

        expandableListView.setAdapter(expListAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expandableListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
        //sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        return jobsView;
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
                convertView = inflater.inflate(R.layout.childlayout_job_history, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.text4);

//            item.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    JobLoacationFragment newFragment =  new JobLoacationFragment();
//                    android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container_history, newFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                }
//            });

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
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.grouplayout_job_history,null);
            }
            TextView item = (TextView) convertView.findViewById(R.id.text3);
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