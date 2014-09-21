package com.edmundjqiu.unhackathongui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Edmund on 9/20/2014.
 */
public class MainActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
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
