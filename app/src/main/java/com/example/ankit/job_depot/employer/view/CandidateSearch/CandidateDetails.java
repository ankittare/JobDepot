package com.example.ankit.job_depot.employer.view.CandidateSearch;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.employer.model.DAO.EmployerHistory;
import com.parse.ParseObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class CandidateDetails extends android.support.v4.app.Fragment {

    String imageUrl;
    TextView candidateName, skills, experience, college;
    Button btnCallCandidate;
    String candiName;
    ImageView candidateImage;
    public CandidateDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View jobsView = inflater.inflate(R.layout.fragment_candidate_details, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyData", getActivity().MODE_PRIVATE);
        candiName = sharedPreferences.getString("CandidateName", "ankit");

        candidateName = (TextView) jobsView.findViewById(R.id.textViewName);
        skills = (TextView) jobsView.findViewById(R.id.textViewSkills);
        experience = (TextView) jobsView.findViewById(R.id.textViewExperience);
        college = (TextView) jobsView.findViewById(R.id.textViewCollegeName);
        btnCallCandidate = (Button) jobsView.findViewById(R.id.buttonCallForInterview);
        candidateImage = (ImageView) jobsView.findViewById(R.id.imageViewCandidate);

        LinearLayout mainLayout=(LinearLayout)getActivity().findViewById(R.id.CandidateDetailstohide);
        mainLayout.setVisibility(LinearLayout.GONE);



        EmployerHistory candidateDetails = new EmployerHistory();
        Log.d("Abharthakjkljkjl", candiName);
        ParseObject candidateDetail = candidateDetails.getCandidate(candiName);

        candidateName.setText(candidateDetail.getString("username"));
        skills.setText(candidateDetail.getString("skills"));
        experience.setText(candidateDetail.getString("workexp"));
        college.setText(candidateDetail.getString("education"));
        imageUrl = candidateDetail.getString("imageURL");

        new ImageLoadTask(imageUrl, candidateImage).execute();

        return  jobsView;
    }

    class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

}
