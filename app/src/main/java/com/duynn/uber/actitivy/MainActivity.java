package com.duynn.uber.actitivy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.duynn.uber.R;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    private Switch userTypeSwitch;
    private Button button;

    //onClick for start button
    public void getStarted(View view) {
        userTypeSwitch = findViewById(R.id.userTypeSwitch);
        button = findViewById(R.id.button);
        Log.i("Switch", String.valueOf(userTypeSwitch.isChecked()));

        String userType="Rider";
        if (userTypeSwitch.isChecked()) {
            userType = "Driver";
        }

        ParseUser.getCurrentUser().put("userType", userType);
        Log.i("UserType", userType);

        //redirect when login again
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                redirectActivity();
            }
        });

        //redirect to rider or driver activity
        redirectActivity();

    }

    //redirect to rider or driver activity
    public void redirectActivity() {
        if(ParseUser.getCurrentUser().get("userType").equals("Rider")){
            Intent intent = new Intent(this, RiderActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, ViewRequestActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        if (ParseUser.getCurrentUser() == null) {
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Log.i("Login", e.getMessage());
                    } else {
                        Log.i("Login", "Success");
                    }
                }
            });
        }else{
            if(ParseUser.getCurrentUser().get("userType") != null){
                redirectActivity();
                Log.i("UserType", ParseUser.getCurrentUser().get("userType").toString());
            }
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}