package com.andre_gt.project.studyapp.catatan.listener;


import com.andre_gt.project.studyapp.catatan.entities.Todo;

public interface TodoListener {
    void onTodoClicked(Todo todo, int position);
}