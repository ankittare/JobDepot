package com.example.ankit.job_depot.candidate.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.controller.CandidateController;
import com.example.ankit.job_depot.candidate.model.DAO.CandidateQuery;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ankit on 7/9/2015.
 */
public class Resume extends Fragment {
    private String usernameID;
    private static final String TAG = "Resume Fragment";
    private static final String Education = "Education";
    private static final String Skills = "Skills";
    private static final String Work_Experience = "Work Experience";
    /*
    LinkedIn specific
     */
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://api.linkedin.com/v1/people/~:(id,first-name,skills,educations,languages,twitter-accounts)?format=json";
    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";
    /*
    Parse Specific
     */
    private ParseObject candidateDetails;
    private CandidateController candidateController;
    private List<String> groupData;
    private Map<String, List<String>> childData;

    /*
    Views
     */
    // TableLayout education,workexp,skills;
    ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View resumeView = inflater.inflate(R.layout.fragment_resume, container, false);
        /*
        Getting data from LinkedIn
         */
        /*final TextView textView = (TextView) resumeView.findViewById(R.id.textView);
        APIHelper apiHelper = null;
        Context context = null;
        try {
            context = getActivity().getApplicationContext();
        } catch (NullPointerException n) {
            Log.e(TAG, "I knew it would give null pointer");
        }
        if (context != null) {
            apiHelper = APIHelper.getInstance(context);
            apiHelper.getRequest(context, topCardUrl, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {
                    Log.i(TAG, "API sucess");
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    Log.i(TAG, jsonObject.toString());
                    try {
                        textView.setText(jsonObject.get("firstName").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onApiError(LIApiError LIApiError) {
                    Log.i(TAG, LIApiError.toString());
                }
            });
        }*/
        /*
        Getting user data from Parse
         */

        try {
            Bundle extras = getActivity().getIntent().getExtras();
            usernameID = extras.getString("usernameID");
        } catch (NullPointerException nne) {
            nne.printStackTrace();
        }

        initializeView(resumeView);
        return resumeView;
    }

    private void initializeView(View v) {
        CandidateQuery candidateQuery = new CandidateQuery();

        /*
        get Actual Id from somewhere
         */

        candidateDetails = candidateQuery.getCandidateDetails("vGx1f5ygQf");
        Log.i(TAG, candidateDetails.getString("username"));
        candidateController = new CandidateController(candidateDetails);

        groupData = new ArrayList<String>();
        groupData.add(Education);
        groupData.add(Skills);
        groupData.add(Work_Experience);

        childData = new HashMap<String, List<String>>();

        fillEducationData();
        fillSkillsData();
        fillWorkData();


        expandableListView = (ExpandableListView) v.findViewById(R.id.resumeDetails);
        ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(), groupData, childData);

        expandableListView.setAdapter(expListAdapter);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expandableListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    private void fillEducationData() {
        List<String> educationList = candidateController.getEducation();
        childData.put(groupData.get(groupData.indexOf(Education)), educationList);
    }

    private void fillSkillsData() {
        List<String> skillsList = candidateController.getSkills();
        childData.put(groupData.get(groupData.indexOf(Skills)), skillsList);
    }

    private void fillWorkData() {
        List<String> workexperience = candidateController.getWorkExp();
        childData.put(groupData.get(groupData.indexOf(Work_Experience)), workexperience);
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
                convertView = inflater.inflate(R.layout.childlayout_resume, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.text5);
            ImageView edit=(ImageView)convertView.findViewById(R.id.edit);

            edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /*
                    Bring up a a dialog to edit
                     */
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Edit Wizard");
                    alertDialog.setMessage("Enter new "+ groupData.get(groupPosition));

                    final EditText input = new EditText(getActivity());
                    input.setHint(childData.get( groupData.get(groupPosition)).get(childPosition));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    //alertDialog.setIcon(R.drawable.key);

                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final String newContent;
                                    newContent = input.getText().toString();
                                    if (newContent.compareTo("") == 0) {
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Nothing Entered!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        List<String> child = laptopCollections.get(laptops.get(groupPosition));
                                        //child.remove(childPosition);
                                        child.set(childPosition, newContent);
                                        /*
                                        write modified data to parse
                                         */

                                        notifyDataSetChanged();
                                    }
                                }
                            }
                    );

                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }

                    );
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
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.grouplayout_resume, null);
            }
            TextView item = (TextView) convertView.findViewById(R.id.text4);
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


