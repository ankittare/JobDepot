package com.example.ankit.job_depot.candidate.model.DAO;

import java.util.List;

/**
 * Created by AnkitT. on 7/29/2015.
 */
public class CBJobs {
    public final static String ROOT_TAG="JobSearchResult";
    public final static String COMPANY="Company";
    public final static String LATITUDE="LocationLatitude";
    public final static String LONGITUDE="LocationLongitude";
    public final static String JOB_TITLE="JobTitle";
    public final static String SKILLS="Skills";
    public final static String LOCATION="Location";
    public final static String DID="DID";
    public final static String EMPLOYMENT_TYPE="EmploymentType";


    private String Company;
    private String LocationLatitude;
    private String LocationLongitude;
    private String JobTitle;
    private String Location;
    private String JobURL;
    private String EmploymentType;
    private List<String> skills;

    public String getEmploymentType() {
        return EmploymentType;
    }

    public void setEmploymentType(String employmentType) {
        EmploymentType = employmentType;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public void setLocationLatitude(String locationLatitude) {
        LocationLatitude = locationLatitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        LocationLongitude = locationLongitude;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setJobURL(String jobURL) {
        JobURL = jobURL;
    }

    public String getCompany() {

        return Company;
    }

    public String getLocationLatitude() {
        return LocationLatitude;
    }

    public String getLocationLongitude() {
        return LocationLongitude;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public String getLocation() {
        return Location;
    }

    public String getJobURL() {
        return JobURL;
    }

    @Override
    public String toString(){
        return getJobTitle()+" "+getCompany()+" "+getEmploymentType()+" ";
    }
}
