package com.example.todonnotes.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import androidx.room.PrimaryKey

@Entity("task_items")
data class TaskItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
)

@Dao
interface TaskItemDao {
    @Insert
    suspend fun insertOne(vararg task: TaskItem)

    @Delete
    fun deleteOne(task: TaskItem)

    @Query("SELECT * FROM task_items")
    suspend fun getAll(): List<TaskItem>
}
