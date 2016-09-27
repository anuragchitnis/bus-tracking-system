package edu.unt.transportation.bustrackingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ActivityUtil
{
    public static final int ACTIVITY_REQUEST_CODE = 1;


    public static int convertDIPtoPX(Context context, int i)
    {
        Resources r = context.getResources();
        float scale = r.getDisplayMetrics().density;
        int paddingDPasPixels = (int) (i * scale + 0.5f); //sets 5dp value in pixels
        return paddingDPasPixels;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
        {
            result += line;
        }

        inputStream.close();
        return result;
    }


    public static View getViewByPosition(int position, ListView listView)
    {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition)
        {
            return listView.getAdapter().getView(position, null, listView);
        }
        else
        {
            final int childIndex = position + listView.getHeaderViewsCount() -
                    firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static void hideKeyboard(Activity activity)
    {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try //in case the first method didn't do the trick, lets try an alternate way of hiding
        // the keyboard
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null)
            {
                view = new View(activity);
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e)
        {
            //do nothing
        }

    }

    public static boolean isEnterKeyPressed(KeyEvent event, int actionID)
    {
        return (actionID == EditorInfo.IME_ACTION_DONE || (event != null && event.getAction() ==
                KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER));
    }

    public static void savePreference(Context context, String key, String value)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void showScreen(Activity activity, Class child)
    {
        showScreen(activity, child, new Bundle());
    }

    public static void showScreen(Activity activity, Class child, Bundle bundle)
    {
        Intent intent = new Intent(activity, child);
        intent.putExtras(bundle);

        activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);

    }
}
