package com.cthacademy.android

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

class ThreadUtil {

    companion object {
        private val executionService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        private val handler = Handler(Looper.getMainLooper())

        fun startThread(runnable: Runnable){
            executionService.submit(runnable)
        }

        fun startUIThread(delayMillis : Int, runnable: Runnable){
            handler.postDelayed(runnable,delayMillis.toLong())
        }
    }

    protected fun finalize(){
        if (!executionService.isShutdown){
            executionService.shutdown()
        }
    }
}