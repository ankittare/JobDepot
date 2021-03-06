package com.example.ankit.job_depot.candidate.view;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.controller.CareerBuilderAPIHelper;
import com.example.ankit.job_depot.candidate.model.DAO.CBJobs;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Ankit on 6/28/2015.
 */

public class JobLoacationFragment extends android.support.v4.app.Fragment {
    private final String TAG = getClass().getSimpleName();
    private SupportMapFragment fragment;
    private GoogleMap map;
    private Context context;
    private Bundle extras;
    private List<CBJobs> nearbyJobs;
    private CareerBuilderAPIHelper careerBuilderAPIHelper;
    private List<Address> address;
    private String QUERY_TYPE_LOCATION = "Location";
    private String QUERY_TYPE_KEYWORD = "Keywords";
    LocationManager locationManager;
    LocationListener locationListener;
    public JobLoacationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        nearbyJobs = new ArrayList<CBJobs>();
        careerBuilderAPIHelper = new CareerBuilderAPIHelper();
        extras = getArguments();
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity().getApplicationContext();
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
        //new getNearByJobs().execute("Pittsburgh");
    }

    @Override
    public void onResume() {

        super.onResume();
        if (map == null) {
            map = fragment.getMap();

            //Criteria crit = new Criteria();
            //crit.setAccuracy(Criteria.ACCURACY_FINE);

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //String best = locationManager.getBestProvider(crit, false);
            //locationListener=new CurrentLocationListener();

            LatLng sydney = new LatLng(-33.867, 151.206);
            /*map.addMarker(new MarkerOptions()
                    .title("Sydney")
                    .snippet("The most populous city in Australia.")
                    .position(sydney));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));*/

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    Log.i(TAG, "Location Changed");
                    Geocoder geocoder = new Geocoder(context);
                    address = null;
                    try {
                        address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i("Current Location: ", address.toString());
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                    map.setMyLocationEnabled(true);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                    map.addMarker(new MarkerOptions()
                                    .title(address.get(0).getAddressLine(0) + " " + address.get(0).getAddressLine(1))
                                    .position(loc)
                    );
                    if (extras.containsKey("queryType")) {
                        if (extras.getCharSequence("queryType").equals("Location")) {
                            String city = address.get(0).getAddressLine(2).split(",")[0];
                            Log.i(TAG, city);
                            new getNearByJobs().execute(city, QUERY_TYPE_LOCATION);
                        } else if (extras.getCharSequence("queryType").equals("Search")) {
                            new getNearByJobs().execute(extras.getString("queryString"), QUERY_TYPE_KEYWORD);
                        }
                    }
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(getActivity().getBaseContext(), "Provider Not Available!", Toast.LENGTH_LONG).show();
                }
            };
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 100000, 5, locationListener);
            //locationManager.removeUpdates(locationListener);
        }
    }

    public void onPause() {
        super.onPause();
        //locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }

    private class getNearByJobs extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "Post Execte");
            //nearbyJobs=new ArrayList<CBJobs>();
            nearbyJobs = careerBuilderAPIHelper.getCBjobs();
            for (CBJobs cbJobs : nearbyJobs) {
                Log.i(TAG, cbJobs.getCompany());
            }
            showNearByJobs();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                careerBuilderAPIHelper.jobSearch(params[0], params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void showNearByJobs() {
        try {
            for (final CBJobs cbJobs : nearbyJobs) {
                //Log.i(TAG, cbJobs.toString());
                LatLng _jobLoc = new LatLng(Double.parseDouble(cbJobs.getLocationLatitude()), Double.parseDouble(cbJobs.getLocationLongitude()));
                map.addMarker(new MarkerOptions()
                        .title(cbJobs.getCompany())
                        .snippet(cbJobs.getJobTitle())
                        .position(_jobLoc));
                map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                            /*
                            Displaying company website for user to apply for this job
                             */
                        Log.i(TAG, "On long click");
                        Log.i(TAG, cbJobs.getJobURL());
                        Bundle extras = new Bundle();
                        extras.putCharSequence("CompanyURL", cbJobs.getJobURL());
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Opening Webpage...",
                                Toast.LENGTH_LONG).show();
                        job_web_view newFragment = new job_web_view();
                        newFragment.setArguments(extras);
                        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.map, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }
        } catch (ConcurrentModificationException c) {
            c.printStackTrace();
        }


        /*
        LocationManager locationManager;
        LocationListener locationListener;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                try {
                    for (final CBJobs cbJobs : nearbyJobs) {
                        //Log.i(TAG, cbJobs.toString());
                        LatLng _jobLoc = new LatLng(Double.parseDouble(cbJobs.getLocationLatitude()), Double.parseDouble(cbJobs.getLocationLongitude()));
                        map.addMarker(new MarkerOptions()
                                .title(cbJobs.getCompany())
                                .snippet(cbJobs.getJobTitle())
                                .position(_jobLoc));
                        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                            @Override
                            public void onMapLongClick(LatLng latLng) {
                            /*
                            Displaying company website for user to apply for this job

                                Log.i(TAG, "On long click");
                                Log.i(TAG, cbJobs.getJobURL());
                                Bundle extras = new Bundle();
                                extras.putCharSequence("CompanyURL", cbJobs.getJobURL());
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Opening Webpage...",
                                        Toast.LENGTH_LONG).show();
                                job_web_view newFragment = new job_web_view();
                                newFragment.setArguments(extras);
                                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.map, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                    }
                } catch (ConcurrentModificationException c) {
                    c.printStackTrace();
                }
                // LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 5, locationListener);
    }
        */

    }
}