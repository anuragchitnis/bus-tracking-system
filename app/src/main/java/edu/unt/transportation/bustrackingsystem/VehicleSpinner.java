package edu.unt.transportation.bustrackingsystem;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Satyanarayana on 11/8/2016.
 */
public class VehicleSpinner implements AdapterView.OnItemSelectedListener
{
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        SignInActivity.vehicleId = parent.getItemAtPosition(position).toString();
        Log.d("VehicleSpinner: [", SignInActivity.vehicleId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
