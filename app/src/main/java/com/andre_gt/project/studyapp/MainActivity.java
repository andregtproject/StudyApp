package com.andre_gt.project.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.andre_gt.project.studyapp.catatan.activities.NoteActivity;
import com.andre_gt.project.studyapp.timer.CoutdowntimerActivity;

public class MainActivity extends AppCompatActivity {

    //Button btnTimer, btnTodo, btnNote;
    CardView cdTodo, cdNotes, cdTimer, cdTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cdTodo = findViewById(R.id.cdTodo);
        cdNotes = findViewById(R.id.cdNote);
        cdTimer = findViewById(R.id.cdTimer);
        cdTools = findViewById(R.id.cdNone);


   /*     btnTimer = findViewById(R.id.btnTimer);
        btnTodo = findViewById(R.id.btnTodo);
        btnNote = findViewById(R.id.btnNote); */

        cdTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoutdowntimerActivity.class);
                startActivity(intent);
            }
        });

        cdTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.andre_gt.project.studyapp.todolistapplication.TodoActivity.class);
                startActivity(intent);
            }
        });

        cdNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });

        cdTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.andregt.g3project");
                if(intent != null){
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Multi Tools app not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}