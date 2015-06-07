package g7.itsmap.sensor.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import g7.itsmap.sensor.R;

/**
 * Created by benjaminclanet on 06/05/2015.
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.message_alarm_reminder))
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
