package ru.sumin.servicestest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService: Service() {

    val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val start = intent?.getIntExtra(EXTRA_START, 0)?: 0
        log("onStartCommand")
        scope.launch {
            for (i in start .. start + 100) {
                delay(300)
                log("Timer: $i")
            }
        }
        return START_REDELIVER_INTENT
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

        private const val EXTRA_START = "start"
        fun newIntent(context: Context, start: Int) =
            Intent(context, MyService::class.java).also {
                it.putExtra(EXTRA_START, start)
            }
    }
}