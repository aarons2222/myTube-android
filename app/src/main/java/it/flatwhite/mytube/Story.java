package it.flatwhite.mytube;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//WebKit Imports
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.ArrayList;
import java.util.List;
import it.flatwhite.mytube.Helper.dbHelper;
import it.flatwhite.mytube.Model.dbModel;

/**
 * Created by Aaron on 30/11/2017.
 * flatwhite.it
 **/

public class Story extends AppCompatActivity {

    dbHelper helper;
    List<dbModel> dbList;
    int position;
    WebView WV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // get value from bundle
        position = bundle.getInt("position");

        helper = new dbHelper(this);
        dbList= new ArrayList<dbModel>();
        dbList = helper.getDataFromNewsTable();

        if(dbList.size()>0){
            String title= dbList.get(position).getTitle();
            String url=dbList.get(position).getUrl();

            // Set ActionBar title to Article Title
            ActionBar aBar = getSupportActionBar();
            aBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4c4c4c")));
            aBar.setTitle(title);


            WV = (WebView) findViewById(R.id.newsWebView);
            WV.setWebViewClient(new MyBrowser());

            WV.getSettings().setAppCachePath( getApplicationContext().getCacheDir().getAbsolutePath() );

            // allows filesystem access
            WV.getSettings().setAllowFileAccess( true );

            // enables website caching
            WV.getSettings().setAppCacheEnabled( true );

            //enables JavaScript
            WV.getSettings().getJavaScriptEnabled();

            WV.getSettings().setJavaScriptEnabled( true );
            WV.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );

            // Network check.   If unavailable, load cached website.
            if ( !isNetworkAvailable() ) {
                WV.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
            }
            WV.loadUrl(url);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}