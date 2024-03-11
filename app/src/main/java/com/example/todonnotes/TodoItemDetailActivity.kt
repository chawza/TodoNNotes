package com.example.todonnotes

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.todonnotes.models.AppDataBase
import com.example.todonnotes.models.TaskItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset

class TodoItemDetailActivity : ComponentActivity() {
    private lateinit var db: AppDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(this, AppDataBase::class.java, "appdatabase").build()

        setContent {
            EditTaskLayout(
                handleAddTask = {newTask: TaskItem -> handleAddTask(newTask)}
            )
        }
    }

    private fun handleAddTask(newTask: TaskItem) {
        GlobalScope.launch {
            Log.d("TESTING", "INSERTING ${newTask.title}")
            db.taskDao().insertOne(newTask)
            Log.d("TESTING", "INSERTED ${newTask.title}")
            Toast.makeText(this@TodoItemDetailActivity, "Task Added!", Toast.LENGTH_LONG)
            this@TodoItemDetailActivity.finish()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskLayout(handleAddTask: (newTask: TaskItem) -> Unit) {
    val titleState = remember { mutableStateOf("") }
    val descriptionState  = remember { mutableStateOf("") }
    val isShowDatePicker = remember { mutableStateOf(false) }

    val now = LocalDateTime.now()
    val dateState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = now.toEpochSecond(ZoneOffset.MIN)
    )

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),

            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = titleState.value,
                    onValueChange = {value -> titleState.value = value},
                    label = {
                        Text(text = "Title")
                    }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = descriptionState.value,
                    onValueChange = {value -> descriptionState.value = value},
                    label = {
                        Text(text = "Description")
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = isShowDatePicker.value,
                        onCheckedChange = { isShowDatePicker.value = !isShowDatePicker.value},
                    )
                    Text(text = "Target Date")
                }
                if (isShowDatePicker.value) {
                    DatePicker(state = dateState)
                }
                Button(onClick = { handleAddTask(TaskItem(0, titleState.value)) }) {
                    Text(text = "Add Note")
                }
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EditTaskLayout(
        handleAddTask = {}
    )
}