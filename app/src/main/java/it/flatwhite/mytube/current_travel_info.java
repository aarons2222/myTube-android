package it.flatwhite.mytube;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import it.flatwhite.mytube.Fragments.current_info_frag1;
import it.flatwhite.mytube.Fragments.current_info_frag2;
import it.flatwhite.mytube.Fragments.current_info_frag3;

/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 */

public class current_travel_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_travel_info);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
//https://stackoverflow.com/questions/46406092/create-a-navigation-bottom-view-for-every-child-of-the-fragment
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = current_info_frag1.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = current_info_frag2.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = current_info_frag3.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        // displaying the first fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, current_info_frag1.newInstance());
        transaction.commit();
    }
}
