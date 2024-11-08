package com.example.final_project_todoapp.db;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_todoapp.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    private final List<Task> taskList;
    private final DatabaseHelper databaseHelper;
    private final Context context;

    // Constructor
    public TaskAdapter(List<Task> taskList, DatabaseHelper databaseHelper, Context context) {
        this.taskList = taskList;
        this.databaseHelper = databaseHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.mcheckbox.setChecked(task.getStatus() == 1);
        holder.taskName.setText(task.getName()); // Hiển thị tên nhiệm vụ trong TextView

        // Xử lý sự kiện khi tích vào checkbox
        holder.mcheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newStatus = isChecked ? 1 : 0;
            databaseHelper.updateTaskStatus(task.getId(), newStatus);
            task.setStatus(newStatus);
        });

        // Xử lý sự kiện khi nhấn vào TextView để chỉnh sửa tên
        holder.taskName.setOnClickListener(v -> showEditDialog(task, position));

        // Xử lý sự kiện khi nhấn vào icon xóa
        holder.deleteIcon.setOnClickListener(v -> {
            int taskId = task.getId();
            databaseHelper.deleteTask(taskId); // Xóa nhiệm vụ khỏi cơ sở dữ liệu
            taskList.remove(position); // Xóa nhiệm vụ khỏi danh sách
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Hiển thị dialog chỉnh sửa tên nhiệm vụ
    private void showEditDialog(Task task, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Task");

        final EditText input = new EditText(context);
        input.setText(task.getName());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                databaseHelper.updateTaskName(task.getId(), newName); // Cập nhật tên nhiệm vụ trong cơ sở dữ liệu
                task.setName(newName); // Cập nhật tên trong danh sách
                notifyItemChanged(position); // Thông báo adapter cập nhật
                Toast.makeText(context, "Task Update Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Task name cannot be blank", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mcheckbox;
        TextView taskName;
        ImageView deleteIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mcheckbox = itemView.findViewById(R.id.mcheckbox);
            taskName = itemView.findViewById(R.id.task_name); // Tham chiếu đến TextView cho tên nhiệm vụ
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }
}
