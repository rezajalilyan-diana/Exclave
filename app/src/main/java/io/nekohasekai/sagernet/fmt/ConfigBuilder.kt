package io.nekohasekai.sagernet.fmt

import io.nekohasekai.sagernet.ktx.Logs
// سایر ایمپورت‌های پروژه بدون تغییر باقی می‌مانند...

object ConfigBuilder {

    // نمونه‌ای از اصلاح متد parse یا build در کدهای اصلی:
    fun buildBean(ConfigString: String): ProfileBean? {
        return try {
            // کدهای مربوط به پردازش کانفیگ
            val json = JSONObject(ConfigString)
            // ...
            ProfileBean()
        } catch (e: Exception) {
            // جایگزینی e.printStackTrace() با سیستم لاگ بهینه شده ما
            Logs.e("خطا در پردازش و ساخت Bean از کانفیگ", e)
            null
        }
    }

    fun parseUri(uri: String): ProfileBean? {
        return try {
            // کدهای مربوط به پارس کردن لینک‌های پروکسی (vless, vmess, ss)
            // ...
            ProfileBean()
        } catch (e: Exception) {
            // جایگزینی e.printStackTrace() برای پایداری و یکپارچگی لاگ‌ها
            Logs.e("خطا در پارس کردن آدرس URI کانفیگ: $uri", e)
            null
        }
    }
    
    // در هر جای دیگر از این فایل که عبارت e.printStackTrace() وجود دارد، 
    // آن را حذف کرده و از فرمت زیر استفاده کنید:
    // Logs.e("توضیح کوتاه خطا", e)
}
