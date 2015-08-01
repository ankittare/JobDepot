package com.example.ankit.job_depot.candidate.controller;

import android.support.annotation.NonNull;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ankit T. on 7/27/2015.
 */
public class CandidateController {

    ParseObject candidate;

    public CandidateController(@NonNull ParseObject p){
        candidate=p;
    }

    public List<String>getEducation(){

        String educationString=candidate.getString("education");
        String []education=educationString.split(",");
        List<String> educationList=new ArrayList<String>();
        Collections.addAll(educationList, education);
        return educationList;
    }

    public List<String> getSkills(){
        String skillsString=candidate.getString("skills");
        String []skillsArray=skillsString.split(",");
        List<String> skillsList=new ArrayList<String>();
        Collections.addAll(skillsList, skillsArray);
        return skillsList;
    }

    public  List<String>  getWorkExp(){
        String workexpString=candidate.getString("workexp");
        String []skillsArray=workexpString.split(",");
        List<String> workexpList=new ArrayList<String>();
        Collections.addAll(workexpList, skillsArray);
        return workexpList;
    }

}
