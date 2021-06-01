package com.rechit.metime;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.rechit.metime.activity.AddEventActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String CHANEL_ID = "ch1";
        Integer id = intent.getStringExtra("id").hashCode();
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        Intent intent1 = new Intent(context, AddEventActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent1,0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence channel_name = "Event Notification";
            int importance=NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel= new NotificationChannel(CHANEL_ID, channel_name, importance);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(id, builder.build());
    }
}
