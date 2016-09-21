package ppl.unity.cubeslots.androidnotifs;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.swrve.unity.gcm.SwrveGcmIntentService;

public class CustomSwrveGcmIntentService extends SwrveGcmIntentService{

    private static boolean customIsEmptyString(String str) {
        return (str == null) || (str.equals(""));
    }

    @Override
    public NotificationCompat.Builder createNotificationBuilder(String msgText, Bundle msg)
    {
        NotificationCompat.Builder builder = super.createNotificationBuilder(msgText, msg);
        return builder;
    }

    @Override
    public Notification createNotification(Bundle msg, PendingIntent contentIntent) {

        String msgText = msg.getString("text");

        if(!customIsEmptyString(msgText)) {
            NotificationCompat.Builder mBuilder = createNotificationBuilder(msgText, msg);
            mBuilder.setContentIntent(contentIntent);

            Notification notification = mBuilder.build();
            Context context = this.getApplicationContext();
            notification.contentView = CustomLayoutGenerator.getInstance().makeCustomNormalLayoutFromPush(context, msg);

            if (Build.VERSION.SDK_INT >= 19)
                notification = customizeBigContentView(notification, context, msg);

            return notification;
        }

        return null;
    }

    @TargetApi(19)
    public Notification customizeBigContentView(Notification notification, Context context, Bundle msg)
    {
        notification.bigContentView = CustomLayoutGenerator.getInstance().makeCustomBigLayoutFromPush(context, msg);
        return notification;
    }
}
