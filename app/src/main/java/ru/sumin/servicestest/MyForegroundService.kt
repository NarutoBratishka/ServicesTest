package ru.sumin.servicestest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyForegroundService: Service() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText("Text")
            .setContentTitle("Title")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }


    private fun createNotificationChannel() {
        val notMan = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notChan = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notMan.createNotificationChannel(notChan)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        scope.launch {
            for (i in 0 .. 3) {
                delay(300)
                log("Timer: $i")
            }
            stopSelf() // or stopService(MyForegroundService.newIntent(this)) from outside
        }
        return START_STICKY
    }

    override fun onDestroy() {
        log("onDestroy")
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "${this::class.java.simpleName}: $message")
    }

    companion object {
        private const val NOTIFICATION_ID = 12
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "Foreground"
        fun newIntent(context: Context) = Intent(context, MyForegroundService::class.java)
    }
}