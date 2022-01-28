package com.andre_gt.project.studyapp.catatan.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andre_gt.project.studyapp.R;
import com.andre_gt.project.studyapp.catatan.entities.Todo;
import com.andre_gt.project.studyapp.catatan.listener.TodoListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder>{

    private List<Todo> todos;
    private TodoListener todoListener;

    public TodoAdapter(List<Todo> todos, TodoListener todoListener){
        this.todos = todos;
        this.todoListener = todoListener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.txtDeadline.setText(todos.get(position).getDateTime());
        holder.setTodo(todos.get(position));
        holder.layoutTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoListener.onTodoClicked(todos.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
    public void filterList(ArrayList<Todo>filteredList){
        todos = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtSubt, txtDeadline;
        LinearLayout layoutTodo;
        RoundedImageView imgTodoItem;

        TodoViewHolder(@NonNull View itemView){
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitleView);
            txtSubt = itemView.findViewById(R.id.txtSub);
            txtDeadline = itemView.findViewById(R.id.tvDateStatus);
            layoutTodo = itemView.findViewById(R.id.todo_item_layout);
            imgTodoItem = itemView.findViewById(R.id.imgTodoItem);
        }

        void setTodo(Todo todo){
            txtTitle.setText(todo.getTittle());
            if (todo.getSubtitle().trim().isEmpty()){
                txtSubt.setVisibility(View.GONE);
            } else {
                txtSubt.setText(todo.getSubtitle());
            }

            GradientDrawable gradientDrawable = (GradientDrawable)layoutTodo.getBackground();
            if (todo.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(todo.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if (todo.getImgPath() != null){
                imgTodoItem.setImageBitmap(BitmapFactory.decodeFile(todo.getImgPath()));
                imgTodoItem.setVisibility(View.VISIBLE);
            } else {
                imgTodoItem.setVisibility(View.GONE);
            }
        }
    }
}
