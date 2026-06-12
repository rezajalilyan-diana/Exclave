package io.nekohasekai.sagernet.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.nekohasekai.sagernet.SagerNet
import java.util.concurrent.Executors

@Database(
    entities = [
        ProxyEntity::class,
        GroupEntity::class,
        ProfileEntity::class,
        RoutingEntity::class,
        RuleEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SagerDatabase : RoomDatabase() {

    abstract fun proxyDao(): ProxyDao
    abstract fun groupDao(): GroupDao
    abstract fun profileDao(): ProfileDao
    abstract fun routingDao(): RoutingDao
    abstract fun ruleDao(): RuleDao

    companion object {
        // تردپول مدیریت‌شده و بهینه ما برای جلوگیری از نشت حافظه
        val queryExecutor = Executors.newCachedThreadPool()

        val instance by lazy {
            Room.databaseBuilder(
                SagerNet.application,
                SagerDatabase::class.java,
                "sager"
            )
                .setQueryExecutor(queryExecutor)
                .allowMainThreadQueries()
                .addMigrations(*MIGRATIONS) // ارجاع مستقیم به آرایه میگریشن‌ها
                .build()
        }

        val proxyDao get() = instance.proxyDao()
        val groupDao get() = instance.groupDao()
        val profileDao get() = instance.profileDao()
        val routingDao get() = instance.routingDao()
        val ruleDao get() = instance.ruleDao()

        // تعریف دقیق آرایه میگریشن که KSP برای کامپایل به آن نیاز دارد
        val MIGRATIONS = arrayOf<androidx.room.migration.Migration>()
    }
}
