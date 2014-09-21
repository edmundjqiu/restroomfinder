package com.edmundjqiu.unhackathongui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.edmundjqiu.data.Restroom;
import com.edmundjqiu.data.RestroomReview;
import com.edmundjqiu.unhackathongui.R;

import java.io.Serializable;

public class Display extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Name of the place?
        Intent source = getIntent();
        String name = source.getStringExtra("name");
        ((TextView)findViewById(R.id.display_name)).setText(name);

        // Is this free?
        int free = source.getIntExtra("free", -1);
        ((TextView)findViewById(R.id.display_free)).setText(free + "");

        // Grab reviews
        Serializable s = (Serializable)getIntent().getSerializableExtra("review");
        Object[] ad = (Object[])s;
        RestroomReview[] rev = new RestroomReview[ad.length];
        for (int i = 0; i < ad.length; i++) rev[i] = (RestroomReview)ad[i];

        // Format reviews:
        StringBuilder b = new StringBuilder();
        for (RestroomReview review : rev)
        {
            b.append(review.getReviewer());
            b.append(" on ");
            b.append(review.getDate());
            b.append('\n');
            b.append("Rating: ");
            b.append(review.getRating());
            b.append('\n');
            b.append(review.getContent());
            b.append("\n\n");
        }

        ((TextView)findViewById(R.id.display_content)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView)findViewById(R.id.display_content)).setText(b.toString());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
