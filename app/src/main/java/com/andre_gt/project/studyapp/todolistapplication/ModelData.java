package com.andre_gt.project.studyapp.todolistapplication;

public class ModelData {

    int id;
    private String title;
    private String date;
    private String time;

    ModelData(int id, String title, String date, String time) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    int getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getDate() {
        return date;
    }

    String getTime() {
        return time;
    }

}
