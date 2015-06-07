package g7.itsmap.sensor.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import g7.itsmap.sensor.R;

/**
 * Created by benjaminclanet on 06/05/2015.
 */
public class ReminderManager {

    private static ReminderManager ourInstance;

    private PendingIntent pendingIntent;
    private static Context context;

    public static ReminderManager getInstance(Context c) {

        context = c;

        if(ourInstance == null) {

            ourInstance = new ReminderManager();
        }

        return ourInstance;
    }

    private ReminderManager() {

        this.createPendingIntent();
    }

    private void createPendingIntent() {

        Intent intent = new Intent(context, ReminderReceiver.class);
        this.pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void startReminded() {

        this.cancelReminder();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.HOUR, 24);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, this.pendingIntent);
    }

    public void cancelReminder() {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(this.pendingIntent);
    }
}
