package ppl.unity.cubeslots.androidnotifs;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.prime31.EtceteraPlugin;

import java.io.InputStream;

public class AlarmManagerReceiver extends BroadcastReceiver {

    static final String TAG = "AlarmManagerReceiver";

    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();

        if (EtceteraPlugin.instance().receivedNotification(bundle.getString("data")))
        {
            Log.i(TAG, "got notification while running");
        }
        else
        {
            Log.i(TAG, "got notification while we are NOT running so posting now");
            try
            {
                sendNotification(context, bundle);
            }
            catch (Exception e)
            {
                Log.i(TAG, "Exception creating and sending notification: " + e);
            }
        }
    }

    @SuppressLint({"NewApi"})
    private void sendNotification(Context context, Bundle bundle)
    {
        int requestCodeAndNotificationId = bundle.getInt("requestCode");

        String launchClassName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();

        ComponentName comp = new ComponentName(context.getPackageName(), launchClassName);
        Intent notificationIntent = new Intent().setComponent(comp);
        notificationIntent.putExtra("notificationData", bundle.getString("data"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCodeAndNotificationId, notificationIntent, 268435456);

        NotificationManager noteManager = (NotificationManager)context.getSystemService("notification");
        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(context);

        noteBuilder.setContentIntent(pendingIntent);
        noteBuilder.setAutoCancel(true);
        noteBuilder.setSmallIcon(context.getApplicationInfo().icon);

        if (bundle.containsKey("title"))
            noteBuilder.setContentTitle(bundle.getString("title"));
        else {
            noteBuilder.setContentTitle("Default title (title parameter not sent with notification)");
        }

        if (bundle.containsKey("subtitle"))
            noteBuilder.setContentText(bundle.getString("subtitle"));
        else {
            noteBuilder.setContentText("Default subtitle (subtitle parameter not sent with notification)");
        }

        if (bundle.containsKey("smallIcon"))
        {
            String smallIconPath = bundle.getString("smallIcon");
            try
            {
                Log.i(TAG, "attempting to find smallIcon resource ID dynamically...");
                int iconId = context.getResources().getIdentifier(smallIconPath, "drawable", context.getPackageName());
                if (iconId == 0)
                {
                    Log.i(TAG, "could not find small icon resource ID in main package. Checking com.prime31.Etcetera package...");
                    iconId = context.getResources().getIdentifier(smallIconPath, "drawable", "com.prime31.Etcetera");
                }

                Log.i(TAG, "smallIcon resource ID: " + iconId);
                noteBuilder.setSmallIcon(iconId);
            }
            catch (Exception e)
            {
                Log.i(TAG, "Exception loading largeIcon via asset ID: " + e);
            }

        }

        if (bundle.containsKey("largeIcon"))
        {
            boolean didLoadIcon = false;
            String largeIconPath = bundle.getString("largeIcon");
            Log.i(TAG, "found largeIcon path: " + largeIconPath);
            try
            {
                Log.i(TAG, "attempting to find largeIcon resource ID dynamically...");
                int iconId = context.getResources().getIdentifier(largeIconPath, "drawable", context.getPackageName());
                if (iconId == 0)
                {
                    Log.i(TAG, "could not find large icon resource ID in main package. Checking com.prime31.Etcetera package...");
                    iconId = context.getResources().getIdentifier(largeIconPath, "drawable", "com.prime31.Etcetera");
                }

                Log.i(TAG, "resource ID: " + iconId);
                if (iconId > 0)
                {
                    Bitmap icon = BitmapFactory.decodeResource(context.getResources(), iconId);
                    Log.i(TAG, "found large icon: " + icon);
                    noteBuilder.setLargeIcon(icon);
                    didLoadIcon = true;
                }
            }
            catch (Exception e)
            {
                Log.i(TAG, "Exception loading largeIcon via asset ID: " + e);
            }

            if (!didLoadIcon)
            {
                Log.i(TAG, "attempting to load icon via path...");
                AssetManager assetManager = context.getAssets();

                Bitmap bitmap = null;
                try
                {
                    InputStream inputStream = assetManager.open(largeIconPath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    noteBuilder.setLargeIcon(bitmap);
                }
                catch (Exception e)
                {
                    Log.i(TAG, "Exception loading largeIcon via InputStream: " + e);
                }

            }

        }

        String tickerText = "Push Notification Received (default tickerText)";

        int defaults = -1;
        boolean hasSound = true;
        if ((bundle.containsKey("vibrate")) || (bundle.containsKey("sound")))
        {
            try
            {
                if ((bundle.containsKey("vibrate")) && (!bundle.containsKey("sound")))
                {
                    defaults = 2;
                    hasSound = false;
                }
                else if ((bundle.containsKey("sound")) && (!bundle.containsKey("vibrate")))
                {
                    defaults = 1;
                }
            }
            catch (Exception e)
            {
                Log.e(TAG, "Error fetching 'defaults' from the bundle: " + e);
            }
        }

        Log.i(TAG, "using notification defaults value: " + defaults);
        noteBuilder.setDefaults(defaults);
        try
        {
            if (hasSound)
            {
                Uri soundUri = RingtoneManager.getDefaultUri(2);
                if (soundUri == null)
                {
                    soundUri = RingtoneManager.getDefaultUri(4);
                    if (soundUri == null) {
                        soundUri = RingtoneManager.getDefaultUri(1);
                    }
                }

                if (soundUri != null)
                {
                    Log.i(TAG, "notification Uri: " + soundUri);
                    noteBuilder.setSound(soundUri);
                }
            }
        }
        catch (Exception e)
        {
            Log.i(TAG, "couldn't find Uri for a sound: " + e);
        }

        if (bundle.containsKey("tickerText")) {
            tickerText = bundle.getString("tickerText");
        }
        noteBuilder.setTicker(tickerText);
        noteBuilder.setWhen(System.currentTimeMillis());

        Notification notification = noteBuilder.build();
        notification.contentView = makeCustomNormalLayout(context, bundle);

        noteManager.notify(requestCodeAndNotificationId, notification);
        Log.i(TAG, "notification posted with requestCode/notification Id: " + requestCodeAndNotificationId);
    }

    private RemoteViews makeCustomNormalLayout(Context context, Bundle bundle) {

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_normal_layout1);
        contentView.setImageViewResource(R.id.normal_layout1_image, R.drawable.icon48x48);

        if (bundle.containsKey("title"))
            contentView.setTextViewText(R.id.normal_layout1_title, bundle.getString("title"));
        else {
            contentView.setTextViewText(R.id.normal_layout1_title, "Default title (title parameter not sent with notification)");
        }

        if (bundle.containsKey("subtitle"))
            contentView.setTextViewText(R.id.normal_layout1_text, bundle.getString("subtitle"));
        else {
            contentView.setTextViewText(R.id.normal_layout1_text, "Default subtitle (subtitle parameter not sent with notification)");
        }

        return contentView;
    }
}
