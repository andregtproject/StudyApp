package com.andre_gt.project.studyapp.catatan.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "todo")
public class Todo implements Serializable {

    @PrimaryKey(autoGenerate = true) private int id;

    @ColumnInfo(name = "title") private String tittle;
    @ColumnInfo(name = "date_time") private String dateTime;
    @ColumnInfo(name = "subtitle") private String subtitle;
    @ColumnInfo(name = "text_desc") private String desc;
    @ColumnInfo(name = "img_path") private String imgPath;
    @ColumnInfo(name = "color") private String color;
    @ColumnInfo(name = "web_link") private String webLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }


    @NonNull
    @Override
    public String toString() {
        return tittle + " : " + dateTime ;
    }
}
