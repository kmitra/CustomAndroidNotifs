package ppl.unity.cubeslots.androidnotifs;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

/**
 * Created by Ginma on 18/09/16.
 */
public class CustomNotifs {

    public int scheduleNotification(long secondsFromNow, String title, String subtitle, String tickerText, String data, String smallIcon, String largeIcon, int requestCode)
    {
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
}
