package io.nekohasekai.sagernet.ktx

import android.util.Log
import io.nekohasekai.sagernet.BuildConfig
import java.util.concurrent.ConcurrentHashMap

object Logs {

    private const val PREFIX = "SagerNet:"
    
    // فعال یا غیرفعال بودن لاگ‌های دباگ بر اساس وضعیت گریدل پروژه
    private val IS_DEBUG = BuildConfig.DEBUG

    // یک کش سریع برای ذخیره نام کلاس‌ها تا نیازی نباشد هر بار استک‌تریس کاوش شود
    private val tagCache = ConcurrentHashMap<String, String>()

    private fun getTag(): String {
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size < 3) return "${PREFIX}Unknown"
        
        // استفاده از نام کامل متد به عنوان کلید کش
        val callerClassName = stackTrace[2].className
        
        return tagCache.getOrPut(callerClassName) {
            val simpleName = callerClassName.substringAfterLast('.')
            val cleanName = when {
                simpleName.contains('$') -> simpleName.substringBefore('$')
                else -> simpleName
            }
            "$PREFIX$cleanName"
        }
    }

    fun v(msg: String) {
        if (IS_DEBUG) {
            Log.v(getTag(), msg)
        }
    }

    fun v(msg: String, tr: Throwable) {
        if (IS_DEBUG) {
            Log.v(getTag(), msg, tr)
        }
    }

    fun d(msg: String) {
        if (IS_DEBUG) {
            Log.d(getTag(), msg)
        }
    }

    fun d(msg: String, tr: Throwable) {
        if (IS_DEBUG) {
            Log.d(getTag(), msg, tr)
        }
    }

    fun i(msg: String) {
        Log.i(getTag(), msg)
    }

    fun i(msg: String, tr: Throwable) {
        Log.i(getTag(), msg, tr)
    }

    fun w(msg: String) {
        Log.w(getTag(), msg)
    }

    fun w(msg: String, tr: Throwable) {
        Log.w(getTag(), msg, tr)
    }

    fun w(tr: Throwable) {
        Log.w(getTag(), tr)
    }

    fun e(msg: String) {
        Log.e(getTag(), msg)
    }

    fun e(msg: String, tr: Throwable) {
        Log.e(getTag(), msg, tr)
    }
}
