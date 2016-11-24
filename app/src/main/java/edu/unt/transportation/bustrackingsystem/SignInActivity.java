package edu.unt.transportation.bustrackingsystem;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unt.transportation.bustrackingsystem.model.BusRoute;
import edu.unt.transportation.bustrackingsystem.model.Driver;
import edu.unt.transportation.bustrackingsystem.model.Vehicle;

/**
 * <b>Driver Activity:</b> When driver logs in, this activity updates the
 * vehicle location.
 * <b>Created By:</b> Anurag
 * <b>Revised By:</b> Satyanarayana
 * <b>Descritpion:</b> An activity that shows driver checkout form<br/>
 * The vehicle and route is selected in this activity
 * <b>Data Structre:</b> Uses some final static variables, instance of
 * Vechicle and BusRoute classes. Spinners to show the vehicle and route dropdowns
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener,
        ChildEventListener {

    /**
     * Firebase root URL
     */
    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";

    /**
     * Log based Activity
     */
    private static final String TAG = "SignInActivity";

    /**
     * Firebase Databse reference,
     * Auth reference, and Firebase reference
     */
    private DatabaseReference mDatabase = null;
    private FirebaseAuth mAuth = null;
    private Firebase mFirebase = null;

    /**
     * Input to email id field
     */
    private EditText mEmailField = null;

    /**
     * Input to password field
     */
    private EditText mPasswordField = null;

    /**
     * Sign In buttong
     */
    private Button mSignInButton = null;

    /**
     * Sign Out Buttong
     */
    private Button mSignUpButton = null;


    private ProgressDialog mProgressDialog = null;

    /**
     * Drop Downs to
     * Vehicle and Route list
     */
    private Spinner spinner1 = null, spinner2 = null;

    private String vehicleId = null;
    private String routeId = null;
    private List<String> list1 = new ArrayList<String>();
    private List<String> list2 = new ArrayList<String>();

    /**
     * Data Adapaters of spinners,
     * which lets notify on data change
     */
    private ArrayAdapter<String> dataAdapter1 = null;
    private ArrayAdapter<String> dataAdapter2 = null;

    /**
     * list of vehicle and routes
     */
    private Map<String, Vehicle> vehicles = null;
    private Map<String, BusRoute> routes = null;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Set up toolbar to be used in activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
        dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);

        vehicles = new HashMap<String, Vehicle>();
        routes = new HashMap<String, BusRoute>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mEmailField.setText("untbustracking@gmail.com");
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mPasswordField.setText("TrackMyBus");

        mSignInButton = (Button) findViewById(R.id.button_sign_in);
        mSignUpButton = (Button) findViewById(R.id.button_sign_up);
        //Temperarily disabling the Sign up button to prevent users from creating new account
        mSignUpButton.setEnabled(false);

        // Click listeners
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);

        addItemsOnSpinner1();
        addItemsOnSpinner2();

        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);

        mFirebase.child("/routes/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BusRoute route = dataSnapshot.getValue(BusRoute.class);
                routes.put(dataSnapshot.getKey(), route);
                dataAdapter2.add(dataSnapshot.getKey());
                dataAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mFirebase.child("/vehicles/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                if(!vehicle.isAssigned()){
                    vehicles.put(dataSnapshot.getKey(), vehicle);
                    dataAdapter1.add(dataSnapshot.getKey());
                    dataAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    // add items into spinner dynamically
    public void addItemsOnSpinner1() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setVisibility(View.VISIBLE);
        spinner1.setPrompt("Select Vehicle");
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleId = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addItemsOnSpinner2() {
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setPrompt("Select Route");
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                routeId = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
//        if (mAuth.getCurrentUser() != null) {
//            onAuthSuccess(mAuth.getCurrentUser());
//        }
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(SignInActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(SignInActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        // Changed by Satya, to pass vehicle id to driver activity
        Bundle bundle = new Bundle();
        Intent dataServiceIntent = new Intent(this, DriverService.class);
        bundle.putSerializable(vehicleId, vehicles.get(vehicleId));
        bundle.putSerializable(routeId, routes.get(routeId));
        bundle.putString("vehicleId", vehicleId);
        bundle.putString("routeId", routeId);
        dataServiceIntent.putExtras(bundle);
        startService(dataServiceIntent);
        addNotification();
        finish();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void addNotification() {
        Intent stopSharingIntent = new Intent(this, NotificationReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(vehicleId, vehicles.get(vehicleId));
        bundle.putSerializable(routeId, routes.get(routeId));
        bundle.putString("vehicleId", vehicleId);
        bundle.putString("routeId", routeId);

        stopSharingIntent.putExtras(bundle);
        PendingIntent pStopIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), stopSharingIntent, 0);

        Intent intent = new Intent(this, RouteListActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification.Action action = new Notification.Action.Builder(android.R.drawable.screen_background_light_transparent, "Stop Sharing", pStopIntent).build();

        Notification n  = new Notification.Builder(this)
                .setContentTitle("Bus Tracking System")
                .setContentText("Location is being Shared")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .addAction(action)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        Driver user = new Driver(userId, name, email);
        mDatabase.child("drivers").child(userId).setValue(user);
    }
    // [END basic_write]

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_sign_in) {
            signIn();
        } else if (i == R.id.button_sign_up) {
            signUp();
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
