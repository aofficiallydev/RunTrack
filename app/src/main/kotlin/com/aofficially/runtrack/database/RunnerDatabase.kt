package com.aofficially.runtrack.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RunnerEntity::class], version = 1, exportSchema = false)
abstract class RunnerDatabase : RoomDatabase() {

    abstract fun runnerDao(): RunnerDao

    companion object {

        @Volatile
        private var INSTANCE: RunnerDatabase? = null

        private val DATABASE_NAME = "runnerDatabase"

        operator fun invoke(context: Context) =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RunnerDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
    }
}