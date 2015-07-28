package com.example.ankit.job_depot.candidate.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.ankit.job_depot.candidate.model.DAO.JobsQuery;
import com.example.ankit.job_depot.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ankit on 7/9/2015.
 */

public class SavedJobs extends Fragment {

    private final String TAG=getClass().getSimpleName();
    private JobsQuery jobsQuery;
    private ExpandableListView expandableListView;
    private List<Map<String, String>> savedjobsData=null;
    private List<String> groupData;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View savedjobView=inflater.inflate(R.layout.fragment_savedjobs, container, false);
        groupData=new ArrayList<String>();

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        jobsQuery=new JobsQuery();
        if(savedjobsData==null){
            Log.i(TAG, sharedPreferences.getString("ObjectId", ""));
            savedjobsData=jobsQuery.getSavedJobs(sharedPreferences.getString("ObjectId", ""));
        }

        Map<String, List<String>> childData=new HashMap<String, List<String>>();
        for(Map<String, String> entry : savedjobsData){
            groupData.add(entry.get("jobTitle"));
            List<String> _temp=new ArrayList<String>();
            _temp.add("Description: "+entry.get("jobdesc"));
            _temp.add("Location: "+ entry.get("jobLocation"));
            _temp.add("Status: "+entry.get("status"));
            _temp.add("Time Saved: "+entry.get("timesaved"));
            Log.i(TAG,entry.get("jobdesc") );
            childData.put(entry.get("jobTitle"), _temp);
        }

        expandableListView = (ExpandableListView) savedjobView.findViewById(R.id.expandableListView2);
        ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(), groupData, childData);

        expandableListView.setAdapter(expListAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expandableListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));

        return savedjobView;
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
                convertView = inflater.inflate(R.layout.childlayout_savedjobs, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.text4);

            item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /*
                    Bring up a new map
                     */

                    JobLoacationFragment newFragment =  new JobLoacationFragment();;
                    android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_Container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
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
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.grouplayout_savedjobs,null);
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
