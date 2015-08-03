package com.example.ankit.job_depot.candidate.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.controller.CandidateController;
import com.example.ankit.job_depot.candidate.controller.DownloadImageTask;
import com.example.ankit.job_depot.candidate.model.DAO.AuthQuery;
import com.example.ankit.job_depot.candidate.model.DAO.CandidateQuery;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

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
    private SharedPreferences sharedPreferences;
    /*
    LinkedIn specific
     */

    //private static final String topCardUrl = "https://api.linkedin.com/v1/people/~?format=json";
    private static final String topCardUrl = "https://api.linkedin.com/v1/people/~:(id,firstName,lastName,headline,positions,picture-url)?format=json";
    /*
    Parse Specific
     */
    private ParseObject candidateDetails;
    private CandidateController candidateController;
    private List<String> groupData;
    private Map<String, List<String>> childData;
    private String parseUsername;

    /*
    Views
     */

    private ExpandableListView expandableListView;
    private ImageView imageView;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View resumeView = inflater.inflate(R.layout.fragment_resume, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        /*
        Getting data from LinkedIn
         */
        textView = (TextView) resumeView.findViewById(R.id.textView);
        imageView = (ImageView) resumeView.findViewById(R.id.imageView2);

        APIHelper apiHelper = APIHelper.getInstance(getActivity().getApplicationContext());
        apiHelper.getRequest(getActivity().getApplicationContext(), topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                String pictureURL = null;
                try {
                    Log.i(TAG, jsonObject.getString("positions"));
                    parseUsername = jsonObject.getString("firstName") + jsonObject.getString("lastName");
                    parseUsername = parseUsername.toLowerCase();
                    /*
                    Putting username in shared preferences because for some reason i am getting null when trying to access instance variables outside anonymous inner class
                    */
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Log.i(TAG, parseUsername);

                    pictureURL = jsonObject.getString("pictureUrl");
                    pictureURL = pictureURL.replace("\\", "");
                    DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
                    downloadImageTask.execute(pictureURL);
                    /*
                    Saving Picture URL to Parse
                     */

                    editor.putString("pictureURL", pictureURL);
                    imageView = downloadImageTask.getBmImage();
                    textView.setText(jsonObject.get("firstName").toString() + "\n" + jsonObject.get("headline"));
                    CandidateQuery candidateQuery = new CandidateQuery();
                    candidateQuery.savePicture(parseUsername, pictureURL);
                    if (!sharedPreferences.contains("username")) {
                        editor.putString("username", parseUsername);
                        if (new AuthQuery().verifyCredential(parseUsername) == false) {
                            if (candidateQuery.createUser(parseUsername))
                                Log.i(TAG, "Entry made in database");
                            else
                                Log.i(TAG, "Something went wrong as usual");
                        }
                    }
                    String id = candidateQuery.getObjectId(parseUsername);
                    candidateDetails = candidateQuery.getCandidateDetails(id);
                    editor.putString("ObjectId", candidateDetails.getObjectId());
                    initializeView(resumeView, candidateDetails);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ne) {
                    Toast.makeText(getActivity().getBaseContext(),
                            "Not enough data! Go to 'Settings' and fill more data", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, pictureURL);
            }

            @Override
            public void onApiError(LIApiError LIApiError) {

            }
        });

        return resumeView;
    }

    private void initializeView(View v, ParseObject candidateDetails) {

        candidateController = new CandidateController(candidateDetails);

        groupData = new ArrayList<String>();
        groupData.add(Education);
        groupData.add(Skills);
        groupData.add(Work_Experience);

        childData = new HashMap<String, List<String>>();

        if (candidateDetails.getString("education") != null)
            fillEducationData();
        if (candidateDetails.getString("skills") != null)
            fillSkillsData();
        if (candidateDetails.getString("workexp") != null)
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
            ImageView edit = (ImageView) convertView.findViewById(R.id.edit);

            edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /*
                    Bring up a a dialog to edit
                     */
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Edit Wizard");
                    alertDialog.setMessage("Enter new " + groupData.get(groupPosition));

                    final EditText input = new EditText(getActivity());
                    input.setHint(childData.get(groupData.get(groupPosition)).get(childPosition));
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


