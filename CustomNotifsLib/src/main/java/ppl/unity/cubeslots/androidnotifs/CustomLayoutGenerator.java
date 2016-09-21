package ppl.unity.cubeslots.androidnotifs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Visibility;
import android.widget.RemoteViews;

import com.swrve.unity.gcm.SwrveGcmDeviceRegistration;


public class CustomLayoutGenerator {
    private static CustomLayoutGenerator ourInstance = new CustomLayoutGenerator();

    public static CustomLayoutGenerator getInstance() {
        return ourInstance;
    }


    public RemoteViews makeCustomNormalLayout(Context context, Bundle bundle) {

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

    public RemoteViews makeCustomNormalLayoutFromPush(Context context, Bundle bundle) {

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_normal_layout2);
        contentView.setImageViewResource(R.id.normal_layout2_image, R.drawable.icon48x48);

        if (bundle.containsKey("title"))
            contentView.setTextViewText(R.id.normal_layout2_title, bundle.getString("title"));
        else {
            SharedPreferences prefs = SwrveGcmDeviceRegistration.getGCMPreferences(context);
            String appTitle = prefs.getString("app_title", "Configure your app title");
            contentView.setTextViewText(R.id.normal_layout2_title, appTitle);
        }

        if (bundle.containsKey("text"))
            contentView.setTextViewText(R.id.normal_layout2_text, bundle.getString("text"));
        else {
            SharedPreferences prefs = SwrveGcmDeviceRegistration.getGCMPreferences(context);
            String appTitle = prefs.getString("app_title", "Configure your app title");
            contentView.setTextViewText(R.id.normal_layout2_text, appTitle);
        }

        return contentView;
    }

    public RemoteViews makeCustomBigLayoutFromPush(Context context, Bundle bundle) {

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_normal_layout2);
        contentView.setImageViewResource(R.id.normal_layout2_image, R.drawable.icon48x48);

        if (bundle.containsKey("title"))
            contentView.setTextViewText(R.id.normal_layout2_title, bundle.getString("title"));
        else {
            SharedPreferences prefs = SwrveGcmDeviceRegistration.getGCMPreferences(context);
            String appTitle = prefs.getString("app_title", "Configure your app title");
            contentView.setTextViewText(R.id.normal_layout2_title, appTitle);
        }

        if (bundle.containsKey("text"))
            contentView.setTextViewText(R.id.normal_layout2_text, bundle.getString("text"));
        else {
            SharedPreferences prefs = SwrveGcmDeviceRegistration.getGCMPreferences(context);
            String appTitle = prefs.getString("app_title", "Configure your app title");
            contentView.setTextViewText(R.id.normal_layout2_text, appTitle);
        }

        return contentView;
    }
}
