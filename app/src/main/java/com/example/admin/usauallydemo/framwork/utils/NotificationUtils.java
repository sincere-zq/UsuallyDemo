package com.example.admin.usauallydemo.framwork.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.admin.usauallydemo.MainActivity;
import com.example.admin.usauallydemo.R;


/**
 * Created by zengqiang on 2016/9/1 0001.
 * Description:
 */
public class NotificationUtils {

    private static NotificationUtils notificationUtils;
    private NotificationManager notificationManager;
    private Notification notification;

    private NotificationUtils() {
    }

    public static NotificationUtils getInstence() {
        if (notificationUtils == null)
            notificationUtils = new NotificationUtils();
        return notificationUtils;
    }


    public void notify(Context context, int layoutId, int notificationId) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder
//                .setContentTitle("这是一个标题")
//                .setContentText("这是一个内容")
                .setContent(new RemoteViews(context.getPackageName(), layoutId))
                .setContentIntent(getDefalutIntent(context))
                .setTicker("通知来了")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher);

        notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(notificationId, notification);
    }

    public PendingIntent getDefalutIntent(Context context) {
        Intent noticeIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, noticeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * 更新进度条
     * @param progress
     * @param notificationId
     */
    public void notificationProgress(int progress, int notificationId,int progressId) {
        notification.contentView.setProgressBar(progressId, 100, progress, false);
        notificationManager.notify(notificationId, notification);
    }

    public void cancel(int id) {
        if (notificationManager != null) {
            notificationManager.cancel(id);
        }
    }

}
