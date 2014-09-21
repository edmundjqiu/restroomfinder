package com.edmundjqiu.unhackathongui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.edmundjqiu.data.Restroom;
import com.edmundjqiu.data.RestroomReview;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

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

    private HashMap<Marker, Restroom> markerToRestroom;

    private Marker dragMarker;
    ArrayAdapter<String> adapter; // array adapter for the ListView
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency);

        dragMarker = null;
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        markerToRestroom = new HashMap<Marker, Restroom>();

        // Load all the data points into memory
        load("http://unhack.brianyang.me/unhack.json");

        // Handle loading more as we go...
        map.setOnCameraChangeListener(
                new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {

                        // If moving the map brings stuff that wasn't visible
                        // before into view, load them.

                        //cameraPosition.target
                    }
                }
        );

        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // Clicking the "new facility" marker doesn't count
                        if (!marker.equals(dragMarker))
                        {

                            Restroom markedRestroom = markerToRestroom.get(marker);
                            Intent i = new Intent(getApplicationContext(),
                                    Display.class);


                            i.putExtra("name", markedRestroom.getName());
                            i.putExtra("free", markedRestroom.getFree());
                            i.putExtra("review", markedRestroom.getReviews());
                            startActivity(i);


                        }
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



    public void load(String url) {
        new FetchRestroomTask().execute(url);
    }

    public class FetchRestroomTask extends AsyncTask<String, Void, Restroom[]> {

        private final String LOG_TAG = FetchRestroomTask.class.getSimpleName();

        @Override
        protected Restroom[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String restroomJsonStr = null;

            String format = "json";
            String units = "metric";
            int limit = 5;

            try {

                final String RESTROOM_API = params[0];

                Uri builtUri = Uri.parse(RESTROOM_API);

                URL url = new URL(builtUri.toString());


                // Open connection to JSON file
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // add new lines for readability
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                restroomJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getRestroomDataFromJson(restroomJsonStr, limit);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private Restroom[] getRestroomDataFromJson(String restroomJsonStr, int limit)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String RR_LIST = "restrooms";
            final String RR_NAME = "name";
            final String RR_LAT = "lat";
            final String RR_LONG = "long";
            final String RR_FREE = "free";
            final String RR_REVS = "reviews";

            Log.d("JSON: ",restroomJsonStr);
            JSONObject restroomJson = new JSONObject(restroomJsonStr);
            JSONArray restroomArray = restroomJson.getJSONArray(RR_LIST);

            //restroomArray.length()
            LinkedList<Restroom> resultStrs = new LinkedList<Restroom>();
            for(int i = 0; i < restroomArray.length() ; i++) {
                String name;
                double lat;
                double lon;
                int free;
                JSONArray reviews;

                // Get the JSON object representing the day
                JSONObject restroom = restroomArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                name = restroom.getString(RR_NAME);
                lat = restroom.getDouble(RR_LAT);
                lon = restroom.getDouble(RR_LONG);
                free = restroom.getInt(RR_FREE);

                reviews = restroom.getJSONArray(RR_REVS);

                RestroomReview[] reviewsList = new RestroomReview[reviews.length()];
                for (int j = 0; j < reviews.length(); j++) {
                    JSONObject reviewObject = reviews.getJSONObject(j);
                    String reviewer = reviewObject.getString("reviewer");
                    String date = reviewObject.getString("date");
                    int rating = reviewObject.getInt("rating");
                    String content = reviewObject.getString("content");
                    RestroomReview currentReview = new RestroomReview(reviewer, date, rating, content);
                    reviewsList[j] = currentReview;
                }

                Restroom currentRestroom = new Restroom(name, lat, lon, free, reviewsList);
                resultStrs.add(currentRestroom);
                Log.d("ADDING RESTROOMS", currentRestroom.getName());

            }

            int size = resultStrs.size();
            Restroom[] result = new Restroom[size];
            for (int i = 0; i < size; i++) result[i] = resultStrs.removeFirst();
            return result;


        }

        @Override
        protected void onPostExecute(Restroom[] result) {

            if (result != null) {

                Log.d("Result Length", result.length+"");

                String restroomsList = "";

                for (Restroom restrooms : result) {

                    LatLng coord = new LatLng(restrooms.getLatitude(), restrooms.getLongitude());
                    String title = restrooms.getName();
                    String snippet = "Click for more info!!";

                    Marker m = map.addMarker(new MarkerOptions()
                            .title(title)
                            .snippet(snippet)
                            .position(coord)
                    );
                    markerToRestroom.put(m, restrooms);

                    Log.d("Here's my marker: ", m.getPosition().toString() + " " + m.getTitle());

                }
            }
        }
    }


    public void stonyTour(View view)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(STONY, 13));
//        map.addMarker(new MarkerOptions()
//                .title("Stony Brook University")
//                .snippet("Excellent STEM programs here")
//                .position(STONY));
    }

    public void columbiaTour(View view)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(COLUMBIA, 13));
//        map.addMarker(new MarkerOptions()
//                .title("Columbia University")
//                .snippet("NYC school")
//                .position(COLUMBIA));
    }


    public void flushingTour(View view)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(FLUSHING, 13));
//        map.addMarker(new MarkerOptions()
//                .title("Flushing")
//                .snippet("Basically the Chinatown of Queens NY")
//                .position(FLUSHING));

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
