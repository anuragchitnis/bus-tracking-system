package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.unt.transportation.bustrackingsystem.model.BusStop;

public class MainActivity extends AppCompatActivity {
    public static final String ARG_ROUTE = "argRoute";
    public static final String ARG_PATHS = "argPaths";
    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";
    private final ImmutableList<LatLng> DISCOVERY_PARK_BUS_STOP_LOCATIONS = ImmutableList.<LatLng>builder()
            .add(new LatLng(33.2539457, -97.1550846))
            .add(new LatLng(33.2401949, -97.1624592))
            .add(new LatLng(33.2401949, -97.1624592))
            .add(new LatLng(33.2200683, -97.1618755))
            .add(new LatLng(33.2167771, -97.1619047))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase(FIREBASE_URL);
        myFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(AuthData authData)
            {
                Log.wtf("Auth", "Authorization changed: ");
                if(authData!=null)
                {
                    Log.wtf("Auth", "(Provider - Expires)" + authData.getProvider() + " - " + authData.getExpires());
                }
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCustomToken("vaaleKOSxYr6ApZTHL2OY1EFD1u44zBCGEdidQs9");
        myFirebaseRef.authWithCustomToken("vaaleKOSxYr6ApZTHL2OY1EFD1u44zBCGEdidQs9", new Firebase.AuthResultHandler()
        {
            @Override
            public void onAuthenticated(AuthData authData)
            {
                Log.wtf("onAuthenticated", "onAuthenticated: ");
                if(authData!=null)
                {
                    Log.wtf("onAuthenticated", "(Provider - Expires)" + authData.getProvider() + " - " + authData.getExpires());
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError)
            {
                Log.wtf("Auth", "onAuthenticationError: " + firebaseError.getMessage() + " -- " + firebaseError.getDetails());

            }
        });
        Button firebaseTest = (Button) findViewById(R.id.firebase_test);
        //Disabled the button to prevent it from making changes to database
        firebaseTest.setEnabled(false);
        firebaseTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ArrayList<BusStop> busStops = new ArrayList<>();
                int i = 1;
                for (LatLng l : DISCOVERY_PARK_BUS_STOP_LOCATIONS)
                {
                    //busStops.add(new BusStop(i++, l.latitude, l.longitude));
                }
                myFirebaseRef.child("/").setValue(busStops);
            }
        });
        myFirebaseRef.child("test").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Toast.makeText(MainActivity.this, "Data changed " + dataSnapshot.toString(), Toast.LENGTH_LONG);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        if (id == R.id.action_signIn) {
            startActivity(new Intent(this, SignInActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMap()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ROUTE, DISCOVERY_PARK_BUS_STOP_LOCATIONS);
        CustomPath customPath = new CustomPath();
        customPath.setLocations(DISCOVERY_PARK_BUS_STOP_LOCATIONS);
        ArrayList<CustomPath> pathList = new ArrayList<>();
        pathList.add(customPath);
        bundle.putSerializable(ARG_PATHS, pathList);
        ActivityUtil.showScreen(MainActivity.this, GoogleMapWithMarker.class, bundle);
    }
}
