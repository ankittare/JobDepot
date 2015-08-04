package com.example.ankit.job_depot.employer.view.JobHistory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
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
    private String skillsReq, experience, jobType;

    public JobDetails(@NonNull String id, @NonNull String jobTitle, @NonNull String jobDesc,
                      @NonNull String jobLocation, String skillsReq, String experience, String jobType) {
        this.ID=id;
        this.jobTitle = jobTitle;
        this.jobDesc = jobDesc;
        this.jobLocation = jobLocation;
        this.experience = experience;
        this.jobType = jobType;
        this.skillsReq = skillsReq;
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

    public String getskillsReq() {
        return skillsReq;
    }

    public String getexperience() {
        return experience;
    }

    public String getjobType() {
        return jobType;
    }
}

public class PostedJobHistory extends Fragment {
    private ExpandableListView expandableListView;
    private List<Map<String, String>> savedjobsData=null;
    private List<String> groupData;
    private ArrayList<JobDetails> jobDetails;
    Fragment myfragment;

    String employerName;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View jobsView = inflater.inflate(R.layout.fragment_posted_job_history, container, false);

        myfragment = this;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", getActivity().MODE_PRIVATE);
        employerName = sharedPreferences.getString("employerName", "ankitb");


        if(jobDetails!=null){
            jobDetails.clear();
        }
        jobDetails=new ArrayList<JobDetails>();
            EmployerHistory jobsQuery=new EmployerHistory();
            for(ParseObject o:jobsQuery.getJobs(employerName)) {
                JobDetails _jd = new JobDetails(o.getObjectId(), o.getString("jobName"), o.getString("jobDesc"), o.getString("jobLocation"),
                        o.getString("skillsRequired"), o.getString("expRequired"), o.getString("jobType"));
                jobDetails.add(_jd);
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
            _temp.add("Skills Required: "+jd.getskillsReq());
            _temp.add("Job Type: "+jd.getjobType());
            _temp.add("Experience Required: "+jd.getexperience() + " years");
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

//        Button btnUpdate = (Button) jobsView.findViewById(R.id.button2);
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                FragmentManager manager = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                Fragment newFragment = myfragment;
//                myfragment.onDestroy();
//                ft.remove(myfragment);
//                ft.replace(container.getId(),myfragment);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });
        return jobsView;
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("Abhartha", "onPause of Job History");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Abhartha", "onDetach of Job History");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Abhartha", "onDestroy of Job History");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("Abhartha", "onResume of Job History");

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