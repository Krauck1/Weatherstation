package kpmm.htl.weatherstation.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import kpmm.htl.weatherstation.R;
import kpmm.htl.weatherstation.activities.MainActivity;
import kpmm.htl.weatherstation.model.Measurement;
import kpmm.htl.weatherstation.model.Model;

/**
 * Created by ak47tehaxor on 24.05.17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar now = GregorianCalendar.getInstance();
        int dayOfWeek = now.get(Calendar.DATE);
        Model model = Model.getInstance();
        if (dayOfWeek != 1 && dayOfWeek != 7&&model.isNotifications()) {
            Measurement lastMeasurement = model.getLastMeasurement();
            if(lastMeasurement==null)
                lastMeasurement = new Measurement(5,10,10,1,10,5,5,10, new Timestamp(System.currentTimeMillis()));
            NotificationCompat.Builder mBuilder;
            if (lastMeasurement.getRainfall() > 1&&lastMeasurement.getAmbientTemperature() > 0&&lastMeasurement.getAmbientTemperature() < 30) {
                mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_inside)
                                .setContentTitle(context.getResources().getString(R.string.app_name))
                                .setContentText(context.getResources().getString(R.string.notification_text_stay_inside));
            } else {
                mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_outside)
                                .setContentTitle(context.getResources().getString(R.string.app_name))
                                .setContentText(context.getResources().getString(R.string.notification_text_going_outside));
            }


            Intent resultIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
