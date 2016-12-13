package ppl.unity.cubeslots.androidnotifs;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.prime31.EtceteraPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CustomNotifs {

    protected static String TAG = "CustomNotifs";
    protected static CustomNotifs _instance;
    private static String KEY_FLAG_USE_CUSTOM_LAYOUT = "KEY_FLAG_USE_CUSTOM_LAYOUT";

    protected Class<?> _unityPlayerClass;
    protected Field _unityPlayerActivityField;
    private Method _unitySendMessageMethod;
    public static Activity _activity;

    public static CustomNotifs instance() {

        if (_instance == null)
        {
            _instance = new CustomNotifs();
        }
        return _instance;
    }

    public CustomNotifs() {

        try
        {
            this._unityPlayerClass = Class.forName("com.unity3d.player.UnityPlayer");
            this._unityPlayerActivityField = this._unityPlayerClass.getField("currentActivity");
            this._unitySendMessageMethod = this._unityPlayerClass.getMethod("UnitySendMessage", new Class[] { String.class, String.class, String.class });
        }
        catch (ClassNotFoundException e)
        {
            Log.i(TAG, "could not find UnityPlayer class: " + e.getMessage());
        }
        catch (NoSuchFieldException e)
        {
            Log.i(TAG, "could not find currentActivity field: " + e.getMessage());
        }
        catch (Exception e)
        {
            Log.i(TAG, "unkown exception occurred locating getActivity(): " + e.getMessage());
        }
    }

    protected Activity getActivity() {

        if (this._unityPlayerActivityField != null) {
            try {
                Activity activity = (Activity) this._unityPlayerActivityField.get(this._unityPlayerClass);
                if (activity == null) {
                    Log.e(TAG, "Something has gone terribly wrong. The Unity Activity does not exist. This could be due to a low memory situation");
                }
                return activity;
            } catch (Exception e) {
                Log.i(TAG, "error getting currentActivity: " + e.getMessage());
            }
        }

        return _activity;
    }

    public int scheduleNotification(long secondsFromNow, String title, String subtitle, String tickerText, String data, String smallIcon, String largeIcon, int requestCode)  {

        // From Etcetera Android
        Activity context = getActivity();
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("subtitle", subtitle);
        intent.putExtra("tickerText", tickerText);
        intent.putExtra("data", data);

        if ((smallIcon != null) && (smallIcon != "")) {
            intent.putExtra("smallIcon", smallIcon);
        }
        if ((largeIcon != null) && (largeIcon != "")) {
            intent.putExtra("largeIcon", largeIcon);
        }
        intent.putExtra("contextClassName", getActivity().getClass().getName());
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("vibrate", 1);
        intent.putExtra("sound", 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 134217728);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        alarmManager.set(1, System.currentTimeMillis() + secondsFromNow * 1000L, pendingIntent);

        return requestCode;
    }

    public void setCustomLayoutPrefs(boolean useCustomLayout) {

        Activity context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences("PCSPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FLAG_USE_CUSTOM_LAYOUT, useCustomLayout);
        editor.apply();
    }

    public boolean getCustomLayoutPrefs(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("PCSPrefs", Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_FLAG_USE_CUSTOM_LAYOUT, false);
    }
}
