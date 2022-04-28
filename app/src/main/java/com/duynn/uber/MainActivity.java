package com.duynn.uber;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseObject object = new ParseObject("ExampleObject");
        object.put("myNumber", "123");
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.i("Parse", "Success");

                }else {
                    Log.i("Parse", "Failed");
                }
            }
        });
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}