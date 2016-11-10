package edu.unt.transportation.bustrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.ImmutableList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    private final ImmutableList<LatLng> DISCOVERY_PARK_BUS_STOP_LOCATIONS = ImmutableList
            .<LatLng>builder()
            .add(new LatLng(33.2539457, -97.1550846))
            .add(new LatLng(33.2401949, -97.1624592))
            .add(new LatLng(33.2401949, -97.1624592))
            .add(new LatLng(33.2200683, -97.1618755))
            .add(new LatLng(33.2167771, -97.1619047))
            .build();
    public FirebaseController firebaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        firebaseController = new FirebaseController(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);


/*

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
                Toast.makeText(MainActivity.this, "Data changed " + dataSnapshot.toString(),
                Toast.LENGTH_LONG);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {

            }
        });
        */
        Button btnGoToRoutes = (Button) findViewById(R.id.btn_go_to_routes);
        btnGoToRoutes.setOnClickListener(this);

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
        if (id == R.id.action_signIn)
        {
            startActivity(new Intent(this, SignInActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        return true;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btn_go_to_routes)
        {
            navigateToRouteList();
        }
    }

    private void navigateToRouteList()
    {
        Bundle b = new Bundle();
        ActivityUtil.showScreen(MainActivity.this, RouteListActivity.class, b);
    }


}
