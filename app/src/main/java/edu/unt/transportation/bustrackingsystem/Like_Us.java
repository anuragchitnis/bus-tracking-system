package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 *
 * This is added by nitesh sharma on
 * 11/25/2016
 * this is activity which send us to facebook page
 * if user is logged in facebook it takes credentials
 * else it ask for user login
 *
 */

public class Like_Us extends AppCompatActivity {

    private GoogleApiClient client;

    WebView myBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_like__us);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            OpenFacebookPage();
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        }
        catch (Exception e) {
            e.printStackTrace();}
    }

    protected void OpenFacebookPage(){


        String facebookPageID = "UNT-1118685708250263";

        // URL
        String facebookUrl = "https://www.facebook.com/" + facebookPageID;


        String facebookUrlScheme = "fb://page/" + facebookPageID;
// try
        //catch block for exception
        try {

            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if (versionCode >= 3002850) {

                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrlScheme)));
            }
        } catch (PackageManager.NameNotFoundException e) {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));

        }

    }
}
