package com.example.to_do

import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_do.ui.theme.TodoTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoTheme {
        Greeting("Android")
    }
}



@Composable
fun TodoApp() {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var taskList by remember { mutableStateOf(listOf<String>()) }
    var taskCheckedStates by remember { mutableStateOf(listOf<Boolean>()) }

    val clearCompleted: () -> Unit = {
        val indicesToKeep = taskCheckedStates.indices.filter { !taskCheckedStates[it] }
        taskList = indicesToKeep.map { taskList[it] }
        taskCheckedStates = indicesToKeep.map { taskCheckedStates[it] }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = textState,
                onValueChange = { textState = it },
                label = { Text("Enter task") },
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Button(
                    onClick = {
                        if (textState.text.isNotBlank()) {
                            taskList = taskList + textState.text
                            taskCheckedStates = taskCheckedStates + false
                            textState = TextFieldValue("")
                        }
                    },
                ) {
                    Text("Add Task")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        clearCompleted()
                    },
                ) {
                    Text("Clear Completed")
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            itemsIndexed(taskList) { index, task ->
                val isChecked = taskCheckedStates.getOrNull(index) ?: false

                val onClick: () -> Unit = {
                    taskCheckedStates = taskCheckedStates.mapIndexed { i, checked ->
                        if(index == i) !checked
                        else checked
                    }
                }

                Card (
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = if (isChecked) {
                        CardDefaults.cardColors(
                            containerColor = Color.LightGray
                        )
                    }
                    else{
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                onClick()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = task)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    MaterialTheme {
        TodoApp()
    }
}
