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
        holder.taskName.setText(task.getName());


        holder.mcheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newStatus = isChecked ? 1 : 0;
            databaseHelper.updateTaskStatusByName(task.getName(), newStatus);
            task.setStatus(newStatus);
            Toast.makeText(context, "Task status updated successfully", Toast.LENGTH_SHORT).show();
        });

        holder.taskName.setOnClickListener(v -> showEditDialog(task, position));


        holder.deleteIcon.setOnClickListener(v -> showDeleteConfirmationDialog(task.getName(), position));

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    private void showDeleteConfirmationDialog(String taskName, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            databaseHelper.deleteTask(taskName);
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
            Toast.makeText(context, "Task Deleted Successfully", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        builder.show();
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
                boolean nameExists = false;
                for (Task t : taskList) {
                    if (t.getName().equals(newName) && t.getId() != task.getId()) {
                        nameExists = true;
                        break;
                    }
                }

                if (nameExists) {
                    Toast.makeText(context, "Task name already exists. Please choose another name.", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.updateTaskName(task.getName(), newName);
                    task.setName(newName);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show();
                }
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
            taskName = itemView.findViewById(R.id.task_name);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }
}
