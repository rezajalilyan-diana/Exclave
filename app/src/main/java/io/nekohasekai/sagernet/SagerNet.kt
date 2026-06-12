package io.nekohasekai.sagernet

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import io.nekohasekai.sagernet.database.DataStore
import io.nekohasekai.sagernet.ktx.Asyncs
import io.nekohasekai.sagernet.ktx.Logs
import io.nekohasekai.sagernet.plugin.Plugins

class SagerNet : Application() {

    companion object {
        lateinit var application: SagerNet
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this

        // مقداردهی اولیه سیستم لاگ بهینه شده
        Logs.i("SagerNet: اپلیکیشن در حال راه‌اندازی است...")

        try {
            // تنظیمات مربوط به دارک‌مود و لود کردن دیتاستور
            AppCompatDelegate.setDefaultNightMode(DataStore.nightMode)
            
            // مقداردهی اولیه پلاگین‌های هسته v2ray / shadowsocks
            Plugins.init()
            
        } catch (e: Exception) {
            // جایگزینی printStackTrace با سیستم لاگ بهینه شده ما برای ردیابی خطاهای کرش اولیه
            Logs.e("SagerNet: خطای بحرانی در زمان مقداردهی اولیه اپلیکیشن", e)
        }
    }

    /**
     * این متد در زمان کمبود حافظه یا بسته شدن پروسس توسط سیستم‌عامل صدا زده می‌شود.
     * بهترین جا برای آزاد کردن اسکوپ‌های کوروتین است.
     */
    override fun onTerminate() {
        super.onTerminate()
        // پاک‌سازی و ابطال تمام کارهای پس‌زمینه معلق برای جلوگیری از Memory Leak
        Asyncs.destroy()
        Logs.i("SagerNet: اسکوپ‌های برنامه به صورت ایمن بسته شدند.")
    }
}
