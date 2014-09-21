package com.edmundjqiu.unhackathongui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Edmund on 9/21/2014.
 */
public class NewFacility extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_facility_form);

        Intent info = getIntent();
        LatLng coordinates = (LatLng)info.getParcelableExtra("coordinates");
        TextView text = (TextView)findViewById(R.id.newText);
        text.setText(coordinates.toString());
    }


}
