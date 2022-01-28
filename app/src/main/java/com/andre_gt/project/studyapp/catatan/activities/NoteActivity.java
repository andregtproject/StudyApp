package com.andre_gt.project.studyapp.catatan.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.andre_gt.project.studyapp.R;
import com.andre_gt.project.studyapp.catatan.adapter.TodoAdapter;
import com.andre_gt.project.studyapp.catatan.database.DatabaseHelper;
import com.andre_gt.project.studyapp.catatan.entities.Todo;
import com.andre_gt.project.studyapp.catatan.listener.TodoListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements TodoListener {
    
    private RecyclerView todoRecyclerView;
    private List<Todo> todoList;
    private TodoAdapter todoAdapter;
    public static final String NOTIFICATION_CHANNEL_ID = "1";
    private final static String default_notification_channel_id = "default";

    public static final int REQUEST_CODE_ADD_TODO = 1;
    public static final int REQUEST_CODE_UPDATE_TODO = 2;
    public static final int REQUEST_CODE_SHOW_TODO = 3;

    private int todoClickedPosition = -1;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        EditText inputSearch = findViewById(R.id.input_search);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), AddTodo.class), REQUEST_CODE_ADD_TODO);
            }
        });

        todoRecyclerView = findViewById(R.id.todoRV);
        todoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoList, this);
        todoRecyclerView.setAdapter(todoAdapter);

        getTodo(REQUEST_CODE_SHOW_TODO, false);


    }


    @Override
    public void onTodoClicked(Todo todo, int position) {
        todoClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), AddTodo.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("todo", todo);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_TODO);
    }

    private void getTodo(final int requestCode, final boolean isTodoDeleted){

        @SuppressLint("StaticFieldBreak")
                class GetTodoList extends AsyncTask<Void, Void, List< Todo > >{

            @Override
            protected List< Todo > doInBackground(Void... voids) {
                return DatabaseHelper
                        .getDB(getApplicationContext())
                        .todoDao().getAllTodo();
            }

            @Override
            protected void onPostExecute(List< Todo > todos) {
                super.onPostExecute(todos);
                if (requestCode == REQUEST_CODE_SHOW_TODO){
                todoList.addAll(todos);
                todoAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_TODO){
                    todoList.add(0, todos.get(0));
                    todoAdapter.notifyItemInserted(0);
                    todoRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_TODO){
                    todoList.remove(todoClickedPosition);
                    if (isTodoDeleted){
                        todoAdapter.notifyItemRemoved(todoClickedPosition);
                    } else {
                        todoList.add(todoClickedPosition, todos.get(todoClickedPosition));
                        todoAdapter.notifyItemChanged(todoClickedPosition);
                    }
                }
            }
        }
        new GetTodoList().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TODO && resultCode == RESULT_OK){
            getTodo(REQUEST_CODE_ADD_TODO, false);
        } else if (requestCode == REQUEST_CODE_UPDATE_TODO && resultCode == RESULT_OK){
            if (data != null){
                getTodo(REQUEST_CODE_UPDATE_TODO, data.getBooleanExtra("isTodoDeleted", false));
            }
        }
    }

    private void filter(String text){
        ArrayList<Todo> filterSearch = new ArrayList<>();
        for (Todo todo : todoList) {
            if (todo.getTittle().toLowerCase().contains(text.toLowerCase())) {
                filterSearch.add(todo);
            }
        }
        todoAdapter.filterList(filterSearch);
    }
}