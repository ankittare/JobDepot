package com.example.ankit.job_depot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

public class Jobs extends android.support.v4.app.Fragment {
    private static final String TAG = "JOBS FRAGMENT";
    private final List<String> jobsList = new ArrayList<String>();
    private final ArrayList<JobDetails> jobDetailses = new ArrayList<JobDetails>();
    private boolean lock = false;
    ExpandableListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View jobsView = inflater.inflate(R.layout.fragment_jobs, container, false);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobDetails");
        // synchronized (this){
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject o : list) {
                        JobDetails _jd = new JobDetails( o.getObjectId(),o.getString("jobName"), o.getString("jobDesc"), o.getString("jobLocation"));
                        jobDetailses.add(_jd);
                        jobsList.add(o.getString("jobName") + "\n " + o.getString("jobDesc") + "\n" + o.getString("jobLocation"));
                        Log.i("Background", o.getString("jobName"));
                        Log.d("ID", o.getObjectId());
                    }
                } else {
                    e.printStackTrace();
                }
                lock = true;
                Log.i("Background", "" + jobsList.size());
            }
        });
        ArrayList<String> groupString=new ArrayList<String>();
        for(JobDetails jd:jobDetailses){
            groupString.add(jd.getJobTitle());
        }

        Map<String, List<String>> childData=new HashMap<String, List<String>>();
        for(JobDetails jd:jobDetailses){
            List<String> _temp=new ArrayList<String>();
            _temp.add(jd.getJobDesc());
            _temp.add(jd.getJobLocation());
            childData.put(jd.getJobTitle(),_temp);
        }
        listView = (ExpandableListView) jobsView.findViewById(R.id.expandableListView);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(), groupString, childData);

        listView.setAdapter(expListAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        listView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
        return jobsView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {

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
                                    /*
                                    Get actual acndidate ID
                                     */
                                    candidateApplyObject.put("studentCandidateID", "ankitT");
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

