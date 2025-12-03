package com.example.arduino;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import com.example.arduino.MainActivity;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "waypoint_alerts";
    private static final String CHANNEL_NAME = "Waypoint Alerts";
    private static final String CHANNEL_DESC = "Location-based travel reminders";

    private Context context;
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription(CHANNEL_DESC);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 250, 500});
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendWaypointNotification(int notificationId, String title,
                                         String message, String bigText) {
        Intent intent = new Intent(context, WaypointActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_map) // Using system icon
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText)
                        .setBigContentTitle(title))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(false)
                .setVibrate(new long[]{0, 500, 250, 500})
                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
                .setLights(Color.BLUE, 1000, 1000)
                .setColor(Color.parseColor("#2196F3"))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // Add action button
        builder.addAction(
                android.R.drawable.ic_menu_info_details, // Using system icon
                "Got it",
                pendingIntent
        );

        notificationManager.notify(notificationId, builder.build());
    }

    // Fixed method - you need to pass proper waypoint data
    public void sendWaypointAlert(String waypointTitle, String waypointMessage,
                                  int waypointRadius, int waypointIndex, float distance) {
        String title = "Waypoint Alert: " + waypointTitle;
        String message = waypointMessage;
        String bigText = waypointMessage +
                "\nYou are " + (int) distance + " meters away." +
                "\nAlert radius: " + waypointRadius + "m";

        sendWaypointNotification(
                2000 + waypointIndex,
                title,
                message,
                bigText
        );
    }

    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }

    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }

    public static boolean areNotificationsEnabled(Context context) {
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
            return channel != null &&
                    channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
        }
        return true;
    }
}