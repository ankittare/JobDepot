package com.example.ankit.job_depot.candidate.controller;

import android.util.Log;

import com.example.ankit.job_depot.candidate.model.DAO.CBJobs;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by AnkitT. on 7/31/2015.
 */

public class CareerBuilderAPICalls{

    private final String API_KEY = "WDHN1MW6NJX0KB74CJPD";
    private final String jobSearchURL = "http://api.careerbuilder.com/v2/jobsearch";
    private final Integer RESPONSE_OK = 200;
    private final Integer RADIUS = 50;
    private final String TAG = getClass().getSimpleName();
    private HttpURLConnection connection;
    private SEXHandler sexHandler = new SEXHandler();
    private List<CBJobs> CBjobs;

    public List<CBJobs> getCBjobs() {
        return CBjobs;
    }

    public void jobSearch(String query) throws ProtocolException, MalformedURLException, IOException, ParserConfigurationException, SAXException {
        CBjobs=new ArrayList<CBJobs>();
        //URL url = new URL(jobSearchURL + "?DeveloperKey=" + API_KEY + "&Location=" + query+"&Radius="+RADIUS);
        URL url = new URL(jobSearchURL + "?DeveloperKey=" + API_KEY + "&Location=" + query);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        //Log.i(TAG, connection.getResponseMessage());
        if (connection.getResponseCode() == RESPONSE_OK) {
            Log.i(TAG, connection.getResponseMessage());
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(connection.getInputStream(), sexHandler);
        }
    }



    private class SEXHandler extends DefaultHandler {
        private final String TAG = getClass().getSimpleName();
        private List<String> skills;
        private String attributeValue;
        private CBJobs cbJobs;

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
                    break;
                }
                case CBJobs.LOCATION: {
                    cbJobs.setLocation(attributeValue);
                    break;
                }
                case CBJobs.EMPLOYMENT_TYPE: {
                    cbJobs.setEmploymentType(attributeValue);
                    break;
                }
                case CBJobs.CompanyDetailsURL:{
                    cbJobs.setJobURL(attributeValue);
                    Log.i(TAG, attributeValue);
                    break;
                }
                case CBJobs.LATITUDE:{
                    cbJobs.setLocationLatitude(attributeValue);
                    break;
                }
                case CBJobs.LONGITUDE:{
                    cbJobs.setLocationLongitude(attributeValue);
                    break;
                }
                case CBJobs.SKILLS:{
                    skills.add(attributeValue);
                    break;
                }
                case CBJobs.JOB_TITLE:{
                    cbJobs.setJobTitle(attributeValue);
                    break;
                }
                case CBJobs.ROOT_TAG:{
                    cbJobs.setSkills(skills);
                    CBjobs.add(cbJobs);
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
}