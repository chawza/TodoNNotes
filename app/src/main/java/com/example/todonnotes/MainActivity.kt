package com.example.todonnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.todonnotes.models.AppDataBase
import com.example.todonnotes.models.TaskItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(this, AppDataBase::class.java, "appdatabase").build()

        setContent {
            Layout()
        }
    }

    suspend fun getAllTasks(): List<TaskItem> {
        return db.taskDao().getAll().toList()
    }

}

@Composable
fun TodoItem(title: String) {
    Row(
        modifier = Modifier
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 30.sp,
            color = Color.Red,
            style = TextStyle(background = Color.Transparent)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Layout() {
    val tasks = remember { mutableStateListOf<TaskItem>() }
    val context = LocalContext.current as MainActivity

    GlobalScope.launch {
        tasks.addAll(context.getAllTasks())
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Your Todos")
            })
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {
                tasks.forEach { task ->
                    TodoItem(title = task.title)
                }
                Button(onClick = { context.startActivity(Intent(context, TodoItemDetailActivity::class.java)) }) {
                    Text(text = "Add Item")
                }
            }
        },

    )
}

@Composable
@Preview
fun LayoutPreview() {
    val todos = listOf(
        TaskItem(1, "LMAo"),
        TaskItem(2, "Work"),
        TaskItem(3, "Make coffee"),
    )
    Layout()
}
