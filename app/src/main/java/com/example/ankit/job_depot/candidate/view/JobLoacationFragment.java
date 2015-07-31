package com.example.ankit.job_depot.candidate.view;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ankit.job_depot.R;
import com.example.ankit.job_depot.candidate.model.DAO.CBJobs;
import com.example.ankit.job_depot.candidate.model.DAO.CareerBuilderAPICalls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 6/28/2015.
 */

public class JobLoacationFragment extends android.support.v4.app.Fragment {
    private final String TAG = getClass().getSimpleName();
    private SupportMapFragment fragment;
    private GoogleMap map;
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private List<CBJobs> nearbyJobs;

    public JobLoacationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        nearbyJobs = new ArrayList<CBJobs>();

        CareerBuilderAPICalls careerBuilderAPICalls = new CareerBuilderAPICalls();
        careerBuilderAPICalls.execute("Pittsburgh");
        nearbyJobs = careerBuilderAPICalls.getNearbyJobs();
        try{
            for (CBJobs cbJobs : nearbyJobs) {
                Log.i(TAG, cbJobs.toString());
            }
        }
        catch(NullPointerException ne){
            ne.printStackTrace();;
        }
        /*
        Implement Sync Adapter
         */
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
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map == null) {

            map = fragment.getMap();

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    Geocoder geocoder = new Geocoder(context);
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    String countryName = addresses.get(0).getAddressLine(2);
                    Log.i("Current Location: ", cityName + " " + stateName);

                    List<String> addList = new ArrayList<String>();
                    for (Address a : addresses) {
                        addList.add(a.toString());
                    }
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                    map.setMyLocationEnabled(true);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));

                    map.addMarker(new MarkerOptions()
                            .title(addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getAddressLine(1))
                            .position(loc));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.i("Error", provider + " Not available");
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            //locationManager.removeUpdates(locationListener);
        }
    }

    public void onPause() {
        super.onPause();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }
}