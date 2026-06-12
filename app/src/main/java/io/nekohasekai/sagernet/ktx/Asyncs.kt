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

@file:Suppress("EXPERIMENTAL_API_USAGE")

package io.nekohasekai.sagernet.ktx

import kotlinx.coroutines.*

// ─────────────────────────────────────────────────────────────────────────────
// اصلاح ۳: جایگزینی GlobalScope با AppScope
//
// مشکل قبلی: GlobalScope.launch یعنی coroutine به هیچ lifecycle ای
// متصل نیست. اگر application در حالت خاصی باشد یا Job والد cancel شود،
// این coroutine‌ها بدون کنترل ادامه می‌دهند و می‌توانند memory leak
// یا crash های غیرمنتظره ایجاد کنند.
//
// راه‌حل: تعریف AppScope با SupervisorJob که:
//   ۱. همه coroutine‌های app زیر یک Job واحد هستند
//   ۲. با SupervisorJob، خرابی یک coroutine بقیه را cancel نمی‌کند
//   ۳. در صورت نیاز می‌توان در Application.onTerminate آن را cancel کرد
//
// نحوه استفاده در SagerNet.kt:
//   override fun onTerminate() {
//       AppScope.cancel()
//       super.onTerminate()
//   }
// ─────────────────────────────────────────────────────────────────────────────
val AppScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

fun block(block: suspend CoroutineScope.() -> Unit): suspend CoroutineScope.() -> Unit {
    return block
}

fun runOnDefaultDispatcher(block: suspend CoroutineScope.() -> Unit) =
    AppScope.launch(Dispatchers.Default, block = block)

suspend fun <T> onDefaultDispatcher(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Default, block = block)

fun runOnIoDispatcher(block: suspend CoroutineScope.() -> Unit) =
    AppScope.launch(Dispatchers.IO, block = block)

suspend fun <T> onIoDispatcher(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.IO, block = block)

fun runOnMainDispatcher(block: suspend CoroutineScope.() -> Unit) =
    AppScope.launch(Dispatchers.Main.immediate, block = block)

suspend fun <T> onMainDispatcher(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Main.immediate, block = block)
