package it.flatwhite.mytube;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import it.flatwhite.mytube.Adapter.MapPageAdapter;
import it.flatwhite.mytube.Fragments.MapTab1Fragment;
import it.flatwhite.mytube.Fragments.MapTab2Fragment;
import it.flatwhite.mytube.Fragments.MapTab3Fragment;

/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 */

public class map extends AppCompatActivity {

    private static final String TAG = "map";

    private MapPageAdapter mMapPageAdapter;

    private ViewPager mViewPager;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.d(TAG, "onCreate: Starting.");


        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4c4c4c")));
        actionBar.setTitle("TfL Maps");

        mMapPageAdapter = new MapPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));
    }
// add fragments to the view and set title for tabs
    private void setupViewPager(ViewPager viewPager) {
        MapPageAdapter adapter = new MapPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MapTab1Fragment(), "Day");
        adapter.addFragment(new MapTab2Fragment(), "Night");
        adapter.addFragment(new MapTab3Fragment(), "Overground");
        viewPager.setAdapter(adapter);
    }
}
