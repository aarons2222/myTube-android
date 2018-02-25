package it.flatwhite.mytube;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {
    // set flag if app has been run
    SharedPreferences prefs = null;

    //***** Buttons for other activities  *****//
    public void currentTravelInfo(View view) {
        Intent intent = new Intent(getApplicationContext(), current_travel_info.class);
        startActivity(intent);
    }

    public void to_tube_map(View view) {
        Intent intent = new Intent(getApplicationContext(), map.class);
        startActivity(intent);
    }

    public void nearMe(View view) {
        Intent intent = new Intent(getApplicationContext(), nearMe.class);
        startActivity(intent);
    }

    public void forTheJourney(View view) {
        Intent intent = new Intent(getApplicationContext(), journeyEntertainment.class);
        startActivity(intent);
    }

    /* END OF BUTTONS */
    //Check if app jas been run before
    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Hides ActionBar giving homescreen a full screen appearance
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Shared Preference to determine if app has been launched before..
        prefs = getSharedPreferences("it.flatwhite.mytube", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        Checks to see if app has been run before...
        If first run display informative welcome dialog.
        */
        if (prefs.getBoolean("firstrun", true)) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Welcome to myTube");
            alertDialog.setMessage("Bringing you the latest travel information from \nTransport for London");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }
}