package io.nekohasekai.sagernet.ktx

import android.os.Looper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object Asyncs {

    // تعریف یک اسکوپ اختصاصی و مدیریت‌شده برای کل اپلیکیشن به جای GlobalScope
    // SupervisorJob مانع از این می‌شود که خطای یک تسک، بقیه پردازش‌ها را متوقف کند
    private val appJob = SupervisorJob()
    val appScope = CoroutineScope(Dispatchers.Default + appJob)

    /**
     * اجرای یک کار در پس‌زمینه با استفاده از اسکوپ اختصاصی اپلیکیشن (جایگزین ایمن GlobalScope)
     */
    fun run(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return appScope.launch(context, block = block)
    }

    /**
     * اجرای یک کار روی ترد اصلی (UI Thread) با استفاده از کوروتین
     * استفاده از immediate باعث می‌شود اگر از قبل روی ترد اصلی باشیم، کد بدون تاخیر اجرا شود
     */
    fun runOnMainThread(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block()
        } else {
            appScope.launch(Dispatchers.Main.immediate) {
                block()
            }
        }
    }

    /**
     * متدی برای ابطال و پاک‌سازی اسکوپ در صورت بسته شدن کامل سرویس یا برنامه (برای جلوگیری از Leak)
     */
    fun destroy() {
        appJob.cancel()
    }
}
