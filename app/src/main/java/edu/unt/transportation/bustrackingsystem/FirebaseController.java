package edu.unt.transportation.bustrackingsystem;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

/**
 * Created by gdawg on 10/15/2016.
 */

public class FirebaseController implements Serializable
{
    private static final String FIREBASE_URL = "https://untbustracking-acb72.firebaseio.com/";
    public static final String KEY_FIREBASE_CONTROLLER = "keyFirebaseController";
    private Firebase myFirebaseRef;

    public Firebase getFirebaseInstance()
    {
        if (myFirebaseRef == null)
        {
            myFirebaseRef = new Firebase(FIREBASE_URL);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithCustomToken("vaaleKOSxYr6ApZTHL2OY1EFD1u44zBCGEdidQs9");
            myFirebaseRef.authWithCustomToken("vaaleKOSxYr6ApZTHL2OY1EFD1u44zBCGEdidQs9", new Firebase.AuthResultHandler()
            {
                @Override
                public void onAuthenticated(AuthData authData)
                {

                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError)
                {
                    Log.e("Auth", "onAuthenticationError: " + firebaseError.getMessage() + " -- " + firebaseError.getDetails());
                }
            });
        }
        return myFirebaseRef;
    }
}
