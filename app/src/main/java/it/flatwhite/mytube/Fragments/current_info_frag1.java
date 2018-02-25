package it.flatwhite.mytube.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

import it.flatwhite.mytube.Adapter.tubeAdapter;
import it.flatwhite.mytube.Helper.dbHelper;
import it.flatwhite.mytube.Model.dbModel;
import it.flatwhite.mytube.R;
import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 */


public class current_info_frag1 extends Fragment {
    public static current_info_frag1 newInstance() {
        current_info_frag1 fragment = new current_info_frag1();
        return fragment;
    }

    public dbHelper helper;

    // Bool to check if device has a network connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set title and Background colour of ActionBar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.item_1);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4c4c4c")));
        // execute 'getNews' method to get results from TfL API
        getStatus();
    }

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_info_frag1, container, false);

        dbHelper helper;
        List<dbModel> dbList;
        RecyclerView mRecyclerView;

        helper = new dbHelper(getContext());

        dbList = new ArrayList<>();

        dbList = helper.getDataFromLineTable();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.tubeRecycler);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify adapter
        mAdapter = new tubeAdapter(this,dbList);

        mRecyclerView.setAdapter(mAdapter);

        return view;
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
    private void finish() {
    }

    public void getStatus() {
     /* check if device network connectivity
     * If no connectivity, alert user with an option to launch WiFI settings.
     * Display data from SQLite db if can't get current from API.
     * */
        if (!isNetworkAvailable()) {

            // Log.i("Connection Status: ", "No Connection Available");
            // Show Dialog to user
            new AlertDialog.Builder(getActivity())
                    .setTitle("Oops! Something went wrong!!")
                    .setMessage("Please check your network connection - you can launch WiFI settings below\nThe most recent status will show!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.wifiSettings, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Launch WiFI settings if dialog appears
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null).show();
        } else {

            // USER HAS A CONNECTION, request data from API
            // Log.i("Connection Status: ", "You have a connection");
            RequestQueue queue = Volley.newRequestQueue(getContext());
            // API url
            String url = "https://api.tfl.gov.uk/line/mode/tube,overground,dlr,tflrail/status";

            final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        // Add JSON reponse data to array
                        JSONArray array = new JSONArray(response);
                        helper = new dbHelper(getContext());

                        /* execute 'clearTubeTable' method in the 'dbHelper' class
                        to clear table
                         */
                        helper.clearTubeTable();


                        // loop through array to extract the required strings.
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.optJSONObject(i);
                            String lines = object.optString("name");
                            //nested array
                            JSONArray arrayStatus = object.getJSONArray("lineStatuses");
                            int len = arrayStatus.length();

                            // loop  through arrayStatus and extract STRING "statusSeverityDescription"
                            //https://stackoverflow.com/questions/47064081/why-is-my-code-returning-value-of-null-from-a-nested-json-array
                            String statusSeverityDescription = null;
                            for (int j = 0; j < len; j++) {
                                JSONObject o = arrayStatus.getJSONObject(j);
                                statusSeverityDescription = o.optString("statusSeverityDescription", "");
                            }

                            if (lines != null) {
                                // adds data from API request and adds to SQLite using the halper claSS.
                                helper = new dbHelper(getContext());
                                helper.insertIntoLineTable(lines, statusSeverityDescription);
                                // Once we added the string to the array, we notify the arrayAdapter
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {}
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }
}