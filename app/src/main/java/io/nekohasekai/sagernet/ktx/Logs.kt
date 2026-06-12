/******************************************************************************
 *                                                                            *
 * Copyright (C) 2021 by nekohasekai <contact-sagernet@sekai.icu>             *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify       *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation, either version 3 of the License, or          *
 *  (at your option) any later version.                                       *
 *                                                                            *
 * This program is distributed in the hope that it will be useful,            *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU General Public License for more details.                               *
 *                                                                            *
 * You should have received a copy of the GNU General Public License          *
 * along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *                                                                            *
 ******************************************************************************/

package io.nekohasekai.sagernet.ktx

import android.util.Log
import io.nekohasekai.sagernet.BuildConfig
import java.io.InputStream
import java.io.OutputStream

object Logs {

    // ─────────────────────────────────────────────────────────────────────────
    // اصلاح ۱: حذف Thread.currentThread().stackTrace از هر فراخوانی لاگ
    //
    // مشکل قبلی: mkTag() در هر لاگ یک stackTrace کامل می‌گرفت که عملیات
    // بسیار گران‌قیمتی است (allocation آرایه + پیمایش call stack).
    // در یک VPN service که لاگ‌های مکرر دارد، این overhead محسوس بود.
    //
    // راه‌حل: استفاده از یک TAG ثابت برای کل object.
    // اگر نیاز به نام caller دارید، می‌توانید TAG را به صورت پارامتر پاس دهید.
    // ─────────────────────────────────────────────────────────────────────────
    private const val TAG = "Exclave"

    // ─────────────────────────────────────────────────────────────────────────
    // اصلاح ۲: فعال‌سازی گارد BuildConfig.DEBUG برای سطوح v و d
    //
    // مشکل قبلی: گاردهای if (BuildConfig.DEBUG) کامنت شده بودند،
    // یعنی لاگ‌های verbose و debug در build های release هم چاپ می‌شدند.
    //
    // راه‌حل: فعال‌سازی گارد. در release build این توابع هیچ کاری نمی‌کنند
    // و کامپایلر R8 کل بدنه آن‌ها را inline و حذف می‌کند.
    // ─────────────────────────────────────────────────────────────────────────

    fun v(message: String) {
        if (BuildConfig.DEBUG) Log.v(TAG, message)
    }

    fun v(message: String, exception: Throwable) {
        if (BuildConfig.DEBUG) Log.v(TAG, message, exception)
    }

    fun d(message: String) {
        if (BuildConfig.DEBUG) Log.d(TAG, message)
    }

    fun d(message: String, exception: Throwable) {
        if (BuildConfig.DEBUG) Log.d(TAG, message, exception)
    }

    // سطوح i، w، e همیشه فعالند (حتی در release) چون اطلاعات مهم دارند
    fun i(message: String) = Log.i(TAG, message)

    fun i(message: String, exception: Throwable) = Log.i(TAG, message, exception)

    fun w(message: String) = Log.w(TAG, message)

    fun w(message: String, exception: Throwable) = Log.w(TAG, message, exception)

    fun w(exception: Throwable) = Log.w(TAG, exception)

    fun e(message: String) = Log.e(TAG, message)

    fun e(message: String, exception: Throwable) = Log.e(TAG, message, exception)

}

fun InputStream.use(out: OutputStream) {
    use { input ->
        out.use { output ->
            input.copyTo(output)
        }
    }
}
