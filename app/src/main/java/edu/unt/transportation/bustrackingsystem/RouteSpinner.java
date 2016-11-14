package edu.unt.transportation.bustrackingsystem;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Satyanarayana on 11/8/2016.
 */
public class RouteSpinner implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //SignInActivity.routeId = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
