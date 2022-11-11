package com.dk.daily_planer;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dk.daily_planer.interfaces.TaskDao;
import com.dk.daily_planer.models.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    final int DIALOG_TIME = 1;

    List<Task> tasks = new ArrayList<>();
    final Context context = this;
    private CalendarView calendarViewOnMain;
    private ListView listViewTask;
    private Button buttonAddTask;
    ArrayAdapter<Task> adapter;

    AppDatabase database;
    TaskDao taskDao;

    int myHourStart = 0;
    int myMinStart = 0;
    int myHourEnd= 0;
    int myMinEnd = 0;
    int selDay;
    int selYear;
    int selMont;

    private TextView timeStartView;
    private TextView timeEndView;

    private boolean flag = false;

    Calendar calendar;

    Task task = new Task();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        init();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void init(){
        calendar = Calendar.getInstance();

        calendarViewOnMain = findViewById(R.id.calendarView);
        listViewTask = findViewById(R.id.listViewItem);
        buttonAddTask = findViewById(R.id.addTask);

        database = App.getInstance().getDatabase();
        taskDao = database.taskDao();
        tasks.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                tasks.addAll(taskDao.getAll());
            }
        }).start();

        adapter = new ArrayAdapter<Task>(getApplicationContext(), R.layout.list_item, tasks);
        listViewTask.setAdapter(adapter);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View addTaskView = inflater.inflate(R.layout.view_dialog,null);
                final EditText name = addTaskView.findViewById(R.id.editNameTask);
                final EditText desc = addTaskView.findViewById(R.id.desc);
                timeStartView = addTaskView.findViewById(R.id.tvTimeStart);
                timeEndView = addTaskView.findViewById(R.id.tvTimeEnd);
                final AlertDialog createTaskDialog = new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setView(addTaskView)
                        .setPositiveButton(android.R.string.ok, null)
                        .setNegativeButton(android.R.string.cancel, null)
                        .create();

                createTaskDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button positiveBtn = ((AlertDialog) createTaskDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        Button setTimeStartBtn = ((AlertDialog) createTaskDialog).findViewById(R.id.setTimeStart);
                        Button setTimeEndBtn = ((AlertDialog) createTaskDialog).findViewById(R.id.setTimeEnd);
                        setTimeEndBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                flag = false;
                                showDialog(DIALOG_TIME);
                            }
                        });
                        setTimeStartBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                flag = true;
                                showDialog(DIALOG_TIME);
                            }
                        });
                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String nameTask;
                                String descTask;
                                nameTask = name.getText().toString();
                                descTask = desc.getText().toString();
                                task.setNameTask(nameTask);
                                task.setDeskTask(descTask);
                                task.setDate(calendar.toInstant().toString().split("T")[0]);
                                if(nameTask.equals("") || task.getDateFinish() == null || task.getDateStart() == null || ((myHourStart == myHourEnd) && (myMinStart > myMinEnd)) || myHourEnd < myHourStart) {
                                    Toast.makeText(getApplicationContext(),"Пустое имя задачи или неверный формат времени!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        taskDao.insert(task);
                                        tasks.clear();
                                        tasks.addAll(taskDao.getAllByDate(calendar.toInstant().toString().split("T")[0]));
                                        Log.d("testDAO", tasks.toString());
                                    }
                                }).start();
                                adapter.notifyDataSetChanged();
                                createTaskDialog.dismiss();
                            }
                        });
                    }
                });
                createTaskDialog.show();
            }
        });

        calendarViewOnMain.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                selYear = i;
                selMont = i1;
                selDay = i2;
                calendar.set(selYear,selMont,selDay);
                tasks.clear();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tasks.addAll(taskDao.getAllByDate(calendar.toInstant().toString().split("T")[0]));
                    }
                }).start();
                adapter.notifyDataSetChanged();
            }
        });
        listViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent callTaskFull = new Intent(getApplicationContext(), TaskFull.class);
                Gson gson = new Gson();
                callTaskFull.putExtra("task",(String)gson.toJson(tasks.get(i)));
                startActivity(callTaskFull);
            }
        });
    }
    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            Log.d("hour",String.format("%d %d",hourOfDay,minute));
            Log.d("calendar",String.valueOf(calendar.getTimeInMillis()));
            calendar.set(selYear,selMont,selDay,hourOfDay,minute);
            if(flag) {
                myHourStart = hourOfDay;
                myMinStart = minute;
                timeStartView.setText(String.format("%02d:%02d",myHourStart,myMinStart));
                task.setDateStart(calendar.getTimeInMillis());
            }else{
                myHourEnd = hourOfDay;
                myMinEnd = minute;
                timeEndView.setText(String.format("%02d:%02d",myHourEnd,myMinEnd));
                task.setDateFinish(calendar.getTimeInMillis());
            }

        }
    };
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, 0, 0, true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }
}