package com.example.ankit.job_depot.candidate.model.DAO;

import android.support.annotation.NonNull;

/**
 * Created by AnkitT. on 7/29/2015.
 */
public class JobDetails {
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
