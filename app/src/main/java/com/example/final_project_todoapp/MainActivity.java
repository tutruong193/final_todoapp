package com.example.final_project_todoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_todoapp.db.DatabaseHelper;
import com.example.final_project_todoapp.db.Task;
import com.example.final_project_todoapp.db.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TaskAdapter taskAdapter;
    private RecyclerView recyclerView;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Lấy danh sách nhiệm vụ từ cơ sở dữ liệu
        taskList = databaseHelper.getAllTasks();

        // Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo và thiết lập Adapter
        taskAdapter = new TaskAdapter(taskList,databaseHelper, this);
        recyclerView.setAdapter(taskAdapter);

        // FloatingActionButton để thêm nhiệm vụ mới
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddTaskDialog());
    }

    // Hiển thị hộp thoại thêm nhiệm vụ mới
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_task, null);
        builder.setView(dialogView);

        EditText inputTask = dialogView.findViewById(R.id.taskNameEditText);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String taskName = inputTask.getText().toString().trim();
            if (!taskName.isEmpty()) {
                addNewTask(taskName);
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập tên nhiệm vụ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Thêm nhiệm vụ mới vào cơ sở dữ liệu và cập nhật giao diện
    private void addNewTask(String taskName) {
        Task newTask = new Task(taskList.size() + 1, taskName, 0); // Mặc định trạng thái là 0
        databaseHelper.addTask(newTask);
        taskList.add(newTask);
        taskAdapter.notifyItemInserted(taskList.size() - 1);
    }
}
