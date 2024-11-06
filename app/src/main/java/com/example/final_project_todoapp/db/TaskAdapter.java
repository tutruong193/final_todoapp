package com.example.final_project_todoapp.db;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_todoapp.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    private final List<Task> taskList; // Danh sách nhiệm vụ

    // Constructor
    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
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
        holder.mcheckbox.setText(task.getName());
        // Nếu có thêm thông tin về trạng thái, bạn có thể thêm vào đây
    }

    @Override
    public int getItemCount() {
        return taskList.size(); // Trả về kích thước của danh sách nhiệm vụ
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mcheckbox; // Tham chiếu đến TextView cho tên nhiệm vụ

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mcheckbox = itemView.findViewById(R.id.mcheckbox); // Thay đổi ID này cho phù hợp với layout của bạn
        }
    }
}
