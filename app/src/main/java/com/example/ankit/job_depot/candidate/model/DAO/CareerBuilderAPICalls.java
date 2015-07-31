package com.example.ankit.job_depot.candidate.model.DAO;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ankit.job_depot.candidate.controller.SEXHandler;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Ankit T. on 7/29/2015.
 */
public class CareerBuilderAPICalls extends AsyncTask<String, String, String> {

    private final String API_KEY = "WDHN1MW6NJX0KB74CJPD";
    private final String jobSearchURL = "http://api.careerbuilder.com/v2/jobsearch";
    private final Integer RESPONSE_OK = 200;
    private final String TAG = getClass().getSimpleName();
    private HttpURLConnection connection;
    private SEXHandler sexHandler = new SEXHandler();
    private List<CBJobs> nearbyJobs;

    public List<CBJobs> getNearbyJobs() {
        return nearbyJobs;
    }

    private void jobSearch(String query) throws ProtocolException, MalformedURLException, IOException, ParserConfigurationException, SAXException {
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

    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG, "Post Execte");
        //nearbyJobs=new ArrayList<CBJobs>();
        nearbyJobs = sexHandler.getCBjobs();
        for (CBJobs cbJobs : nearbyJobs) {
            Log.i(TAG, cbJobs.toString());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            jobSearch(params[0]);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
