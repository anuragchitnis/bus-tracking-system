package edu.unt.transportation.bustrackingsystem;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



/**
 *
 * This is added by nitesh sharma on
 * 11/25/2016
 * this is activity which gives about us
 * and details about the developer of this project
 * developed at UNT
 * for software engineering couse
 *
 */



public class About_Us extends AppCompatActivity {


    private GoogleApiClient client;

    WebView myBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        TextView txtView = (TextView) findViewById(R.id.text_id);
        txtView.append("UNT BusTrackingSystem Android application is developed by computer science students for software engineering course in fall 2016 at University of North Texas.\n" +
                "\n" +
                "We are a team of four members with different expertise in programming language. We have contributed, coordinated and worked hard to develop this app.\n" +
                        "\n"+
                "1. Anurag Chitnis" +
                        "\n"+
                "2. Gill Wasserman" +
                        "\n"+
                "3. Satyanarayana" +
                        "\n"+
                "4. Nitesh Sharma");

        // UiLifecycleHelper uiHelper = new UiLifecycleHelper(this, callback);

        // myBrowser = (WebView)findViewById(R.id.mybrowser);
        // myBrowser.loadUrl("file:///android_asset/fb.html");

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }




    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("About_Us Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();


        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();


        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
