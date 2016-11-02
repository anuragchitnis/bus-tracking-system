package edu.unt.transportation.bustrackingsystem;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.BusStop;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * This shows how to place markers on a map.
 */
public class GoogleMapWithMarker extends AppCompatActivity implements
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnMarkerDragListener,
        OnSeekBarChangeListener,
        OnMapReadyCallback,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        ValueEventListener, ChildEventListener
{
    public static final String ARG_LOCATIONS = "argLocations";
    public static final String ARG_PATHS = "argPaths";
    public static final String ARG_VEHICLES = "argVehicles";
    public static final String ARG_CURRENT_ROUTE = "argRoute";
    View mapView;
    private GoogleMap mMap;
    /**
     * Keeps track of the last selected marker (though it may no longer be selected).  This is
     * useful for refreshing the info window.
     */
    private RadioGroup mOptions;
    private LatLngBounds bounds;
    private HashMap<LatLng, Marker> markerMap = new HashMap<>();
    private BusRoute currentRoute;
    private HashMap<String, BusStop> currentRouteStops;
    private List<CustomPath> currentPaths;
    private HashMap<String, Vehicle> currentVehicles;
    private FirebaseController firebaseController;

    @Override
    protected void onPause()
    {
        super.onPause();
        BusTrackingSystem.mapActivityPaused();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        BusTrackingSystem.mapActivityResumed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle args = getIntent().getExtras();
//        currentRouteStops = (HashMap<String, BusStop>) args.getSerializable(ARG_LOCATIONS);
//        currentPath = new PolylineOptions().addAll(currentRouteStops).width(5).color(Color.CYAN);
        currentPaths = (List<CustomPath>) args.getSerializable(ARG_PATHS);
//        currentVehicles = (HashMap<String, Vehicle>) args.getSerializable(ARG_VEHICLES);
        setCurrentRoute((BusRoute) args.getSerializable(ARG_CURRENT_ROUTE));

        mOptions = (RadioGroup) findViewById(R.id.custom_info_window_options);
        mOptions.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
//                if (mLastSelectedMarker != null && mLastSelectedMarker.isInfoWindowShown())
//                {
                // Refresh the info window when the info window's content has changed.
//                    mLastSelectedMarker.showInfoWindow();
//                }
            }
        });
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        firebaseController = new FirebaseController(GoogleMapWithMarker.this);
        firebaseController.getFirebaseInstance().addValueEventListener(this);
        firebaseController.getFirebaseInstance().addChildEventListener(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        firebaseController.getFirebaseInstance().removeEventListener((ChildEventListener) this);
        firebaseController.getFirebaseInstance().removeEventListener((ValueEventListener) this);
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(Marker marker)
    {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker)
    {
        Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mMap = map;

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add lots of markers to the map.
        addMarkersToMap();
        addPathsToMap();
        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        map.setContentDescription("Map");

        // Pan to see all markers in view.
        // Cannot zoom to bounds until the map has a size.
        mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView != null && mapView.getViewTreeObserver().isAlive())
        {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
            {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout()
                {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    else
                    {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    setUpLayout();
                }

            });
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        /*
        if (marker.equals(mPerth))
        {
            // This causes the marker at Perth to bounce into position when it is clicked.
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(
                            1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                    marker.setAnchor(0.5f, 1.0f + 2 * t);

                    if (t > 0.0)
                    {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }
        else if (marker.equals(mAdelaide))
        {
            // This causes the marker at Adelaide to change color and alpha.
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(mRandom.nextFloat() * 360));
            marker.setAlpha(mRandom.nextFloat());
        }

        // Markers have a z-index that is settable and gettable.
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(this, marker.getTitle() + " z-index set to " + zIndex,
                Toast.LENGTH_SHORT).show();

        // Markers can store and retrieve a data object via the getTag/setTag methods.
        // Here we use it to retrieve the number of clicks stored for this marker.
        Integer clickCount = (Integer) marker.getTag();
        // Check if a click count was set.
        if (clickCount != null)
        {
            clickCount = clickCount + 1;
            // Markers can store and retrieve a data object via the getTag/setTag methods.
            // Here we use it to store the number of clicks for this marker.
            marker.setTag(clickCount);
            mTagText.setText(marker.getTitle() + " has been clicked " + clickCount + " times.");
        }
        else
        {
            mTagText.setText("");
        }

        mLastSelectedMarker = marker;
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        */
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker)
    {
//        mTopText.setText("onMarkerDragStart");
    }

    @Override
    public void onMarkerDrag(Marker marker)
    {
//        mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
    }

    @Override
    public void onMarkerDragEnd(Marker marker)
    {
//        mTopText.setText("onMarkerDragEnd");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if (!checkReady())
        {
            return;
        }
        /*
        float rotation = seekBar.getProgress();
        for (Marker marker : mMarkerRainbow)
        {
            marker.setRotation(rotation);
        }
        */
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        // Do nothing.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        // Do nothing.
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError)
    {

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey)
    {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey)
    {
        if (dataSnapshot.getKey().equals(FirebaseController.FIREBASE_VEHICLES))
        {
            for (String key : currentVehicles.keySet())
            {
                currentVehicles.put(key, dataSnapshot.child(key).getValue(Vehicle.class));
            }
            setUpLayout();
            onResetMap(mapView);
            Toast.makeText(this,"Map refreshed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot)
    {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey)
    {

    }

    private void addMarkersToMap()
    {
        if (currentRouteStops == null) return;
        for (BusStop stop : currentRouteStops.values())
        {
            LatLng latLng = new LatLng(stop.getLatitude(), stop.getLongitude());
            markerMap.put(latLng, mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(stop.getStopName())
                    .snippet("Next Available Time")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop))
                    .infoWindowAnchor(0.5f, 0.5f)));
        }
        for (Vehicle v : currentVehicles.values())
        {
            LatLng latLng = new LatLng(v.getLatitude(), v.getLongitude());
            markerMap.put(latLng, mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Vehicle")
                    .snippet(currentRoute.getRouteName() + " Bus")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                    .infoWindowAnchor(0.5f, 0.5f)));
        }
    }

    //
    // Marker related listeners.
    //

    private void addPathsToMap()
    {
        if (currentPaths == null) return;
        for (CustomPath path : currentPaths)
        {
            mMap.addPolyline(path.getOptions());
        }

    }

    private boolean checkReady()
    {
        if (mMap == null)
        {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initBounds()
    {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (BusStop bs : currentRouteStops.values())
        {
            LatLng latLng = new LatLng(bs.getLatitude(), bs.getLongitude());
            boundsBuilder.include(latLng);
        }
        bounds = boundsBuilder.build();

    }

    /**
     * Called when the Clear button is clicked.
     */
    public void onClearMap(View view)
    {
        if (!checkReady())
        {
            return;
        }
        mMap.clear();
    }

    /**
     * Called when the Reset button is clicked.
     */
    public void onResetMap(View view)
    {
        if (!checkReady())
        {
            return;
        }
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
        addMarkersToMap();
    }

    private void setCurrentRoute(BusRoute route)
    {
        currentRoute = route;
        currentRouteStops = currentRoute.getBusStopObjectMap();
        currentVehicles = currentRoute.getVehicleObjectMap();
    }

    private void setUpLayout()
    {
        if (currentRouteStops != null && currentRouteStops.size() > 0) initBounds();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    /**
     * Demonstrates customizing the info window and/or its contents.
     */
    class CustomInfoWindowAdapter implements InfoWindowAdapter
    {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews
        // with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter()
        {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window)
            {
                // This means that getInfoContents will be called.
                return null;
            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents)
            {
                // This means that the default info contents will be used.
                return null;
            }
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view)
        {
            /*
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            if (marker.equals(mBrisbane))
            {
                badge = R.drawable.badge_qld;
            }
            else if (marker.equals(mAdelaide))
            {
                badge = R.drawable.badge_sa;
            }
            else if (marker.equals(mSydney))
            {
                badge = R.drawable.badge_nsw;
            }
            else if (marker.equals(mMelbourne))
            {
                badge = R.drawable.badge_victoria;
            }
            else if (marker.equals(mPerth))
            {
                badge = R.drawable.badge_wa;
            }
            else
            {
                // Passing 0 to setImageResource will clear the image view.
                badge = 0;
            }
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);
*/
            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null)
            {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            }
            else
            {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12)
            {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            }
            else
            {
                snippetUi.setText("");
            }
        }
    }

}