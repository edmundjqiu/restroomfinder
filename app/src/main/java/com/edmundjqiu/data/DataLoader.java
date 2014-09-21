package com.edmundjqiu.data;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Brian Yang
 */
public class DataLoader {

    private Restroom[] restrooms;

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
            int limit = 7;

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

            JSONObject restroomJson = new JSONObject(restroomJsonStr);
            JSONArray restroomArray = restroomJson.getJSONArray(RR_LIST);

            Restroom[] resultStrs = new Restroom[limit];
            for(int i = 0; i < restroomArray.length(); i++) {
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
                }

                Restroom currentRestroom = new Restroom(name, lat, lon, free, reviewsList);
                resultStrs[i] = currentRestroom;

            }

            return resultStrs;


        }

        @Override
        protected void onPostExecute(Restroom[] result) {
            restrooms = result;
        }
    }


}
