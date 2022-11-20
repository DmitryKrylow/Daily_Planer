package com.dk.daily_planer.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.dk.daily_planer.R;
import com.dk.daily_planer.models.Task;
import com.google.gson.Gson;

public class TaskFull extends AppCompatActivity {
    TextView tw;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_full);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        Task task = (Task) new Gson().fromJson((String) getIntent().getSerializableExtra("task"),Task.class);
        tw = this.findViewById(R.id.textAsdView);
        tw.setText(task.showFullInfo());
    }
}