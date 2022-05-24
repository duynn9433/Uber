package com.duynn.uber.actitivy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.duynn.uber.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ViewRequestActivity extends AppCompatActivity {
    private ListView requestListView;
    private ArrayList<String> requestList;
    private ArrayAdapter adapter;
    private ArrayList<Double> requestLatitudes;
    private ArrayList<Double> requestLongitudes;
    private ArrayList<String> usernames ;
    LocationManager locationManager;
    LocationListener locationListener;

    public void updateListView(Location location) {
        if (location != null) {
            requestList.clear();
            requestLatitudes.clear();
            requestLongitudes.clear();
            usernames.clear();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
            final ParseGeoPoint userLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            query.whereNear("location", userLocation);
            query.setLimit(10);
            query.whereDoesNotExist("driverUsername");

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        requestList.clear();
                        requestLongitudes.clear();
                        requestLatitudes.clear();
                        if(objects.size() > 0) {
                            for (ParseObject object : objects) {
                                ParseGeoPoint requestLocation = (ParseGeoPoint) object.get("location");
                                if(requestLocation != null) {
                                    Double distanceInMeters = userLocation.distanceInKilometersTo(requestLocation);
                                    Double distanceOneDecimal = (double) Math.round(distanceInMeters * 10.0) / 10.0;
                                    requestList.add(object.getString("username") + " - " + distanceOneDecimal.toString() + " km");
                                    requestLatitudes.add((requestLocation.getLatitude()));
                                    requestLongitudes.add((requestLocation.getLongitude()));
                                    usernames.add(object.getString("username"));
                                }
                            }

                        }else {
                            requestList.add("No requests found nearby");
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });

//            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, requestList);
//            requestListView.setAdapter(adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateListView(lastKnownLocation);

                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        requestLatitudes = new ArrayList<>();
        requestLongitudes = new ArrayList<>();
        usernames = new ArrayList<>();
        setTitle("Nearby Requests");
        requestListView = findViewById(R.id.requestListView);
        requestList = new ArrayList<>();
        requestList.clear();
        requestList.add("Getting nearby requests...");
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, requestList);

        requestListView.setAdapter(adapter);

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Build.VERSION.SDK_INT < 23 ||
                        ContextCompat.checkSelfPermission(ViewRequestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (requestLatitudes.size() > i && requestLongitudes.size() > i && lastKnownLocation != null) {
                        Intent intent = new Intent(ViewRequestActivity.this, DriverLocationActivity.class);
                        intent.putExtra("requestLatitude", requestLatitudes.get(i));
                        intent.putExtra("requestLongitude", requestLongitudes.get(i));
                        intent.putExtra("driverLatitude", lastKnownLocation.getLatitude());
                        intent.putExtra("driverLongitude", lastKnownLocation.getLongitude());
                        intent.putExtra("username", usernames.get(i));
                        startActivity(intent);

                    }
                }

            }
        });


        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateListView(location);

                ParseUser.getCurrentUser().put("location", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));

                ParseUser.getCurrentUser().saveInBackground();

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    updateListView(lastKnownLocation);
                }
            }
        }

    }
}