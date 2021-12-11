package com.example.iamhere.utils.locationService

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker
import androidx.work.Worker
import android.app.NotificationManager

import android.app.NotificationChannel
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.iamhere.R


class UploadWorker(
    context: Context?,
    params: WorkerParameters?
) : Worker(context!!, params!!) {
    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.
        Log.i("Work Manager Worker", "Working....")
        displayNotification("I'am here", "Waiting until time start");

        // Indicate whether the work finished successfully with the Result
        return Result.success(Data.Builder().putString("WorkObserver", "Success").build())
    }

    private fun displayNotification(title: String, task: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "simplifiedcoding",
                "simplifiedcoding",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "simplifiedcoding")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }
}