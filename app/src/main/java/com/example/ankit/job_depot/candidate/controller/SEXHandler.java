package com.example.ankit.job_depot.candidate.controller;

import android.util.Log;

import com.example.ankit.job_depot.candidate.model.DAO.CBJobs;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnkitT. on 7/29/2015.
 */
public class SEXHandler extends DefaultHandler {
    private final String TAG = getClass().getSimpleName();
    private List<CBJobs> CBjobs = new ArrayList<CBJobs>();
    private List<String> skills;
    private String attributeValue;
    private CBJobs cbJobs;

    public List<CBJobs> getCBjobs() {
        return CBjobs;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {


        switch (qName) {
            case CBJobs.ROOT_TAG: {
                cbJobs=new CBJobs();
                skills=new ArrayList<String>();
                break;
            }
            case CBJobs.SKILLS: {
                break;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        switch (qName) {
            case CBJobs.COMPANY: {
                cbJobs.setCompany(attributeValue);
               // Log.i(qName, cbJobs.getCompany());
                break;
            }
            case CBJobs.LOCATION: {
                cbJobs.setLocation(attributeValue);
               // Log.i(qName, attributeValue);
                break;
            }
            case CBJobs.EMPLOYMENT_TYPE: {
                cbJobs.setEmploymentType(attributeValue);
                //Log.i(qName, attributeValue);
                break;
            }
            case CBJobs.DID:{
                cbJobs.setJobURL(attributeValue);
                //Log.i(qName, attributeValue);
                break;
            }
            case CBJobs.LATITUDE:{
                cbJobs.setLocationLatitude(attributeValue);
                break;
            }
            case CBJobs.LONGITUDE:{
                cbJobs.setLocationLongitude(attributeValue);
                //Log.i(qName, attributeValue);
                break;
            }
            case CBJobs.SKILLS:{
                skills.add(attributeValue);
               // Log.i(qName, attributeValue);
                break;
            }
            case CBJobs.JOB_TITLE:{
                cbJobs.setJobTitle(attributeValue);
                // Log.i(qName, attributeValue);
                break;
            }
            case CBJobs.ROOT_TAG:{
                Log.i(TAG, "---------------");
                cbJobs.setSkills(skills);
                CBjobs.add(cbJobs);
                /*for (CBJobs cbJobs :CBjobs) {
                    Log.i(TAG, cbJobs.toString());
                }
                */
                skills.clear();
                break;
            }
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        attributeValue = new String(ac, i, j);
    }


}
