package com.example.tutorial.customandroidnotifs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    Button buttonNormalLayout;
    Button buttonExpandedLayout;

    private static MainActivity instance = null;
    public static MainActivity GetInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.instance = this;

        initializeButtons();
    }

    private void initializeButtons() {

        this.buttonNormalLayout = (Button) findViewById(R.id.buttonNormal1);
        this.buttonExpandedLayout = (Button) findViewById(R.id.buttonExpanded1);

        this.buttonNormalLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                MainActivity.GetInstance().makeNormalLayoutNotification();
            }
        });

        this.buttonExpandedLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                MainActivity.GetInstance().makeExpandedLayoutNotification();
            }
        });
    }

    public void makeNormalLayoutNotification() {

        int icon = R.drawable.icon48x48;
        long when = System.currentTimeMillis();

        Notification notification = new NotificationCompat.Builder(this.getApplicationContext())
                .setSmallIcon(icon)
                .setContentText("Custom Notification")
                .setWhen(when)
                .build();

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_normal_layout1);
        contentView.setImageViewResource(R.id.normal_layout1_image, R.drawable.icon48x48);
        contentView.setTextViewText(R.id.normal_layout1_title, "Custom Notification");
        contentView.setTextViewText(R.id.normal_layout1_text, "This is a custom layout");

        notification.contentView = contentView;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;

        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound

        mNotificationManager.notify(1, notification);
    }

    public void makeExpandedLayoutNotification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.icon48x48);
        mBuilder.setContentTitle("Expanded Notification");
        mBuilder.setContentText("Pull Me Down!");

        NotificationCompat.InboxStyle inboxStyle =new NotificationCompat.InboxStyle();

        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_normal_layout1);
        contentView.setImageViewResource(R.id.normal_layout1_image, R.drawable.icon48x48);
        contentView.setTextViewText(R.id.normal_layout1_title, "Expanded Notification");
        contentView.setTextViewText(R.id.normal_layout1_text, "Pull Me Down!");

        RemoteViews bigContentView = new RemoteViews(getPackageName(), R.layout.custom_expanded_layout1);
        bigContentView.setImageViewResource(R.id.expanded_layout1_image, R.drawable.icon48x48);
        bigContentView.setTextViewText(R.id.expanded_layout1_title, "Expanded Notification");
        bigContentView.setTextViewText(R.id.expanded_layout1_text, "This is an expanded layout");

        Notification notification = mBuilder.build();
        notification.contentView = contentView;
        notification.bigContentView = bigContentView;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;

        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
    }
}
