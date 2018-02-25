package it.flatwhite.mytube;
//ANDROID

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//VOLLEY
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//MAPS
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//FLATWHITE.IT
import it.flatwhite.mytube.Adapter.stationAdapter;
import it.flatwhite.mytube.Helper.ClickListener;
import it.flatwhite.mytube.Helper.TouchListener;
import it.flatwhite.mytube.Model.Station;

/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 */

public class nearMe extends AppCompatActivity implements OnMapReadyCallback {

    ActionBar actionBar;

    private MapView mapView;
    public GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private List<Station> stationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private stationAdapter mAdapter;

    /*   LOCATION STUFF     */
    // Dummy Location Co-ordinates - overwritten when location data is received- LOCATION Covent garden
    public String lat = "51.5117302";
    public String lng = "-0.1407793";

    //https://developer.android.com/reference/android/location/LocationManager.html
    LocationManager locationManager;
    //https://developer.android.com/reference/android/location/LocationListener.html
    LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // execute startListening method to get users location
                startListening();
            }
        }
    }

    public void startListening() {
        // permission granted and gained using GPS.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location) {
        //updates lng and lat strings with users current longitude and latitude
        lat = "" + location.getLatitude();
        lng = "" + location.getLongitude();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        getNearestStations();
       updateMap();
        Toast.makeText(this, "Location Updated!", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Start listening for location
        startListening();

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4c4c4c")));
        actionBar.setTitle("Nearest Stations");


        mAdapter = new stationAdapter(stationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

// onClick
        recyclerView.addOnItemTouchListener(new TouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Toast.makeText(getApplicationContext(), stationList.get(position).getStation() + "", Toast.LENGTH_SHORT).show();

                String findStation = stationList.get(position).getStation();

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + findStation + "underground station+London&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mapIntent.setFlags(mapIntent.FLAG_ACTIVITY_CLEAR_TOP);
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        }));


        // insert Stations in to recycler
        getNearestStations();

        //map
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    // clear adapter before adding
    public void clear() {
        int size = this.stationList.size();
        this.stationList.clear();
        //update adapter
        mAdapter.notifyItemRangeRemoved(0, size);
    }

    public void getNearestStations() {

        // Volley request
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://transportapi.com/v3/uk/tube/stations/near.json?app_id=157c4895&app_key=091697cea8bae89519dd02ebb318fc51&lat=" + lat + "&lon=" + lng + "&rpp=5";

        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //JSON object created from API response
                    JSONObject jsonObject = new JSONObject(response);
                    // creates an array containing station names from the JSON object.
                    JSONArray jsonArray = jsonObject.getJSONArray("stations");

                    // Call 'Clear' method to clear mAdapter before adding new data.
                    clear();
                    if (jsonArray.length() > 0) {
                        /* if JSON array has data, loop through it and extract
                        STRING "name"
                         */
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObject = jsonArray.getJSONObject(i);

                            String station = jObject.getString("name");

                            /* For each STRING "name" extracted add to stationList
                            as a new station */
                            Station newStation = new Station(station);
                            stationList.add(newStation);
                            // notify Recycler adapter of change.
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        /*
                        *  If Volleyt request returns 0 results, add "no stations near"
                        *  message to recyclerView and display a dialog to the user.
                        * */
                        Station newStation = new Station("No stations near");
                        stationList.add(newStation);
                        mAdapter.notifyDataSetChanged();

                        // Show a diaog
                        AlertDialog alertDialog = new AlertDialog.Builder(nearMe.this).create();
                        alertDialog.setTitle("No Stations Found");
                        alertDialog.setMessage("You are either not in London or you have no network connectivity.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                } catch (JSONException e) {
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
// maps
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        gmap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        // PERMISSIONS CHECK FOR DEVICES RUNNING MARSHMALLOW AND LATER!
        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    updateLocationInfo(location);
                }
                updateMap();
            }
        }
    }

// Maps https://developers.google.com/maps/documentation/android-api/marker
    // ADD NEW MARKER TO MAP
    public void updateMap() {

        // Convert lng+lat STRINGS to DOUBLE for use with Google Map API.
             double longitude = Double.parseDouble(lng);
            double latitude = Double.parseDouble(lat);

            // clear marker from map before adding new.
            gmap.clear();

            gmap.setMinZoomPreference(12);
            LatLng ldn = new LatLng(latitude, longitude);
            gmap.moveCamera(CameraUpdateFactory.newLatLng(ldn));
            gmap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    // Custom icon/marker to display users location on map
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));
    }
}


