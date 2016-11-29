package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import java.lang.Object;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;


/**
 *
 * This is added by nitesh sharma on
 * 11/25/2016
 * this is activity which send us to facebook page
 * if user is logged in facebook it takes credentials
 * else it ask for user login
 *
 */

public class Like_Us extends AppCompatActivity implements View.OnClickListener,
        ChildEventListener {
    private GoogleApiClient client;
    public static boolean IsPost = false;

    WebView myBrowser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {




                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_like__us);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

              //  ImageButton button = (ImageButton) findViewById(R.id.myButton);

                //button.setOnClickListener(this);

                 OpenFacebookPage();
                client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        }
        catch (Exception e) {
            e.printStackTrace();}
    }

    @Override public void onClick(View v)
    {
        OpenFacebookPage();
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

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }
}
