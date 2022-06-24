package com.frhnfath.storyappfix.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)

abstract class StoryDatabase: RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDAO
    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE?: synchronized(this) {
                INSTANCE?: Room.databaseBuilder(context.applicationContext, StoryDatabase::class.java, "story_database").fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}