package com.dk.daily_planer.models;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "dateStart")
    private Long dateStart;
    @ColumnInfo(name = "dateFinish")
    private Long dateFinish;
    @ColumnInfo(name = "nameTask")
    private String nameTask;
    @ColumnInfo(name = "deskTask")
    private String deskTask;
    @ColumnInfo(name = "date")
    private String date;

    public Task(){
    }


    public Task(long id, Long dateStart, Long dateFinish, String nameTask, String deskTask, String date){
        this.id = id;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.nameTask = nameTask;
        this.deskTask = deskTask;
        this.date = date;
    }

    @Override
    public String toString(){
        return String.format("%s\n%02d:%02d-%02d:%02d",nameTask, new Timestamp(dateStart).getHours(),new Timestamp(dateStart).getMinutes(),new Timestamp(dateFinish).getHours(),new Timestamp(dateFinish).getMinutes());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Spanned showFullInfo(){
        return Html.fromHtml(String.format("<h1 style=\"text-align: center;\"><span style=\"text-decoration: underline;\"><em>Задача: %s</em></span></h1>\n" +
                "<ul>\n" +
                "<li><em><strong><h3>Продолжительность: %s</h3></strong></em><em><strong></strong></em></li>\n" +
                "<li><em><strong><h3>Описание: %s<h3></strong></em></li>\n" +
                "<li><em><strong><h3>Дата: %s<h3></strong></em></li>\n" +
                "</ul>",nameTask,String.format("%02d:%02d-%02d:%02d",new Timestamp(dateStart).getHours(),new Timestamp(dateStart).getMinutes(),new Timestamp(dateFinish).getHours(),new Timestamp(dateFinish).getMinutes()),deskTask, date),Html.FROM_HTML_MODE_COMPACT);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getDateStart() {
        return dateStart;
    }

    public void setDateStart(Long dateStart) {
        this.dateStart = dateStart;
    }

    public Long getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Long dateFinish) {
        this.dateFinish = dateFinish;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDeskTask() {
        return deskTask;
    }

    public void setDeskTask(String deskTask) {
        this.deskTask = deskTask;
    }


}
