package it.flatwhite.mytube.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
//twitter imports
//https://dev.twitter.com/twitterkit
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;
import it.flatwhite.mytube.R;
import static com.google.android.gms.internal.zzagz.runOnUiThread;

/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 **/

public class current_info_frag2 extends Fragment {
    public static current_info_frag2 newInstance() {
        current_info_frag2 fragment = new current_info_frag2();
        return fragment;
    }
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set title and Background colour of ActionBar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.item_2);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4c4c4c")));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_info_frag2, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Twitter.initialize(getActivity());
        // execut AsyncTask to get tweets.
        new current_info_frag2.getTweets().execute();
        return view;
    }

    //Used AsyncTask to improve performance..  Load times for activity were around 15 seconds before Async implementation
    private class getTweets extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //TwitterKit timeline search user @TflTravelAlerts
            final UserTimeline searchTimeline = new UserTimeline.Builder().screenName("TflTravelAlerts")
                    .maxItemsPerRequest(5)
                    .build();

            //https://dev.twitter.com/twitterkit/android/show-tweets
            final TweetTimelineRecyclerViewAdapter tweetAdapter =
                    new TweetTimelineRecyclerViewAdapter.Builder(getActivity())
                            .setTimeline(searchTimeline)
                            .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                            .build();

            // set the recycleradapter and display tweets.
            // 'runOnUi' used as 'doInBackdround' cannot interact with UI elements
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(tweetAdapter);
                }
            });

            return null;
        }
    }
}



