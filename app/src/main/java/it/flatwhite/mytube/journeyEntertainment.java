package it.flatwhite.mytube;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.flatwhite.mytube.Adapter.newsAdapter;
import it.flatwhite.mytube.Helper.dbHelper;
import it.flatwhite.mytube.Model.dbModel;

/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 */

public class journeyEntertainment extends AppCompatActivity {

    ActionBar actionBar;
    dbHelper helper;
    List<dbModel> dbList;

    // RecyclerView tutorial
    // http://www.androidtutorialshub.com/android-recyclerview-tutorial/
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    // network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_entertainment);

        // Set title and change colour of ActionBar
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4c4c4c")));
        actionBar.setTitle("Latest News");

        helper = new dbHelper(journeyEntertainment.this);

// getNews Method
        getNews();
        helper = new dbHelper(this);
        dbList= new ArrayList<dbModel>();

        dbList = helper.getDataFromNewsTable();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycleview);

        mRecyclerView.setHasFixedSize(true);

        // use linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify the adapter
        mAdapter = new newsAdapter(this,dbList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // getNews Method
    public void getNews(){
  /* check if device network connectivity
     * If no connectivity, alert user with an option to launch WiFI settings.
     * Display data from SQLite db if can't get current from API.
     * */
        if ( !isNetworkAvailable() ) {

            // Log.i("Connection Status: ", "No Connection Available");
            // Show Dialog to user
            new AlertDialog.Builder(journeyEntertainment.this)
                    .setTitle("Sorry! Unable to download latest news!")
                    .setMessage("Please check your network connection - you can launch WiFI settings below\nor if you have saved articles, you can read those!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.wifiSettings, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                            startActivity(intent);

                        }}).setNegativeButton(R.string.readCachedNews, null).show();
        }else{
        RequestQueue queue = Volley.newRequestQueue(this);
            // API url
        String url = "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=927e06d9dddb47ee929c9bb759055434";

        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");
                    // clear table using dbHelper class
                    helper.clearNewsTable();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);

                        // get required strings from JSON object
                        String title = jObject.getString("title");
                        String  desc = jObject.getString("description");
                        String  url = jObject.getString("url");

                        // Strip out all apostrophes, this stops the SQL insert from breaking
                        title = title.replaceAll("'", "");
                        desc = desc.replaceAll("'", "");
                        url = url.replaceAll("'", "");


                        // Insert data from Volley request into db using the dbHelper class
                        helper = new dbHelper(journeyEntertainment.this);
                        helper.insertIntoNewsTable(title, desc, url);
                    }
                    mRecyclerView.setAdapter(mAdapter);

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
    }
}
