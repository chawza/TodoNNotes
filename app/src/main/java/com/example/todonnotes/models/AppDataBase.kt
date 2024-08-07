package com.example.todonnotes.models

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskItem::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun taskDao(): TaskItemDao
}