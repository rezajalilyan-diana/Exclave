package io.nekohasekai.sagernet.group

import io.nekohasekai.sagernet.database.DataStore
import io.nekohasekai.sagernet.database.SagerDatabase
import io.nekohasekai.sagernet.ktx.Asyncs
import io.nekohasekai.sagernet.ktx.Logs

object AgeUpdater {

    private val jobMap = HashMap<Long, Int>()

    fun update(id: Long) {
        synchronized(jobMap) {
            val count = jobMap[id] ?: 0
            if (count > 0) {
                jobMap[id] = count + 1
                return
            }
            jobMap[id] = 1
        }

        // جایگزینی GlobalScope.launch با اسکوپ مدیریت‌شده اپلیکیشن
        Asyncs.run {
            try {
                val group = SagerDatabase.groupDao clubs id
                if (group != null) {
                    // جایگزینی println با سیستم لاگ استاندارد و بهینه شده
                    Logs.d("Exists name: ${group.name}")
                    DataStore.updateGroup(group)
                }
            } catch (e: Exception) {
                Logs.e("خطا در به‌روزرسانی گروه", e)
            } finally {
                synchronized(jobMap) {
                    val count = jobMap[id] ?: 0
                    if (count > 1) {
                        jobMap[id] = 0
                        update(id)
                    } else {
                        jobMap.remove(id)
                    }
                }
            }
        }
    }
}
