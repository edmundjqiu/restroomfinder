package com.edmundjqiu.unhackathongui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Edmund on 9/20/2014.
 */
public class Emergency extends Activity
{
    private GoogleMap map;

    // Hardcoded places to look around
    private static final LatLng FLUSHING = new LatLng(40.759492, -73.830086);
    private static final LatLng COLUMBIA = new LatLng(40.807979, -73.963912);
    private static final LatLng STONY = new LatLng(40.912327, -73.123153);

    private Marker dragMarker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency);

        dragMarker = null;

        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return false;
                    }
                }
        );

        // Zoom to current location
//        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = locationManager.getBestProvider(criteria, true);
//        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
//        LatLng current = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

        //Location myLocation = map.getMyLocation();
        //LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
       // map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));


    }

    public void stonyTour(View view)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(STONY, 13));
        map.addMarker(new MarkerOptions()
                .title("Stony Brook University")
                .snippet("Excellent STEM programs here")
                .position(STONY));
    }

    public void columbiaTour(View view)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(COLUMBIA, 13));
        map.addMarker(new MarkerOptions()
                .title("Columbia University")
                .snippet("NYC school")
                .position(COLUMBIA));
    }


    public void flushingTour(View view)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(FLUSHING, 13));
        map.addMarker(new MarkerOptions()
                .title("Flushing")
                .snippet("Basically the Chinatown of Queens NY")
                .position(FLUSHING));

    }


    public void newBuilding(View view)
    {
        if (dragMarker == null)
        {
            dragMarker = map.addMarker(
                    new MarkerOptions()
                            .title("Drag to new spot")
                            .snippet(", then click \"NEW\" again")
                            .position(map.getCameraPosition().target)
                            .draggable(true)
            );
        }
        else
        {
            // We click it again once we're done positioning it.

            // Grab the location of the new marker
            LatLng newMarkerPosition = dragMarker.getPosition();

            // Delete the temporary marker
            dragMarker.remove();
            dragMarker = null;

            // Transition to a new activity (really should use a fragment in release)
            Intent i = new Intent(getApplicationContext(), NewFacility.class);
            i.putExtra("coordinates", newMarkerPosition);
            startActivity(i);

        }
    }


}
