package com.millsofmn.android.schoolplanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.ui.course.AssessmentActivity;
import com.millsofmn.android.schoolplanner.ui.course.CourseDetailsActivity;
import com.millsofmn.android.schoolplanner.ui.course.CourseListActivity;
import com.millsofmn.android.schoolplanner.ui.main.MainActivity;

public class MyReceiver extends BroadcastReceiver {
    public static final String TAG = "++++ Trigg";
    public static final String ALERT_MESSAGE = "alert_message";
    public static final String ALERT_TITLE = "alert_title";

    static int notificationId;

    String channel_id="school_planner";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Receiver Triggered");
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();


        String title = intent.getStringExtra(ALERT_TITLE);
        String message = intent.getStringExtra(ALERT_MESSAGE);

        int courseId = intent.getIntExtra(CourseListActivity.COURSE_ID_EXTRA, -1);
        int termId = intent.getIntExtra(CourseListActivity.TERM_ID_EXTRA, -1);
        int assessmentId = intent.getIntExtra(AssessmentActivity.ASSMT_ID_SELECTED, -1);

        Intent notifyIntent;
        if(assessmentId > -1) {
            notifyIntent = new Intent(context, AssessmentActivity.class);
            notifyIntent.putExtra(CourseListActivity.COURSE_ID_EXTRA, courseId);
            notifyIntent.putExtra(AssessmentActivity.ASSMT_ID_SELECTED, assessmentId);
        } else if(courseId > -1){
            notifyIntent = new Intent(context, CourseDetailsActivity.class);
            notifyIntent.putExtra(CourseListActivity.TERM_ID_EXTRA, termId);
            notifyIntent.putExtra(CourseListActivity.COURSE_ID_EXTRA, courseId);
        } else {
            notifyIntent = new Intent(context, MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        createNotificationChannel(context, channel_id);

        Notification n = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_school)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, n);
    }

    private void createNotificationChannel(Context context, String channel_id) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
