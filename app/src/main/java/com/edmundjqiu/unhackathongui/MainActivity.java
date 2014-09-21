package com.edmundjqiu.unhackathongui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edmundjqiu.data.Restroom;
import com.edmundjqiu.data.RestroomReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Edmund on 9/20/2014.
 */
public class MainActivity extends Activity {

    private Restroom[] restrooms;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        String[] restrooms = {
                "Charles B. Wang - great",
                "New World Mall - terrible"
        };

        //List<String> restroomsList = new ArrayList<String>(Arrays.asList(restrooms));
        //TextView t = (TextView)findViewById(R.id.comments);
        //t.setText("hi");

//        ListView listView = (ListView)findViewById(R.id.restroom_list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
//                //Intent details = new Intent(getApplicationContext(), DetailActivity.class)
//                //.putExtra(Intent.EXTRA_TEXT, adapter.getItem(position));
//                Intent intent = new Intent(getApplicationContext(),
//                        Emergency.class);
//                startActivity(intent);
//            }
//        });
    }

    public void ohshit(View view)
    {
        Intent intent = new Intent(getApplicationContext(),
                Emergency.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

}
