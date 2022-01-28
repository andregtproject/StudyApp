package com.andre_gt.project.studyapp.todolistapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.andre_gt.project.studyapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TodoActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    private static final String TAG = "TodoActivity";
    private com.andre_gt.project.studyapp.todolistapplication.DatabaseHelper databaseHelper;
    private ListView itemsListView;
    private FloatingActionButton fab;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        databaseHelper = new com.andre_gt.project.studyapp.todolistapplication.DatabaseHelper(this);
        fab = findViewById(R.id.fab);
        itemsListView = findViewById(R.id.itemsList);

        populateListView();
        onFabClick();
        hideFab();
    }

    //Mengatur notifikasi
    private void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(this, com.andre_gt.project.studyapp.todolistapplication.Notifikasi.class);
        notificationIntent.putExtra(com.andre_gt.project.studyapp.todolistapplication.Notifikasi.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(com.andre_gt.project.studyapp.todolistapplication.Notifikasi.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getLayoutInflater().getContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
        }
    }

    private Notification getNotification(String content) {

        //Saat notifikasi di klik di arahkan ke MainActivity
        Intent intent = new Intent(this, com.andre_gt.project.studyapp.todolistapplication.TodoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getLayoutInflater().getContext(), default_notification_channel_id);
        builder.setContentTitle("Pengingat");
        builder.setContentText(content);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_judul);
        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        return builder.build();
    }

    //Memasukkan data ke database
    private void insertDataToDb(String title, String date, String time) {
        boolean insertData = databaseHelper.insertData(title, date, time);
        if (insertData) {
            try {
                populateListView();
                toastMsg("Tugas di tambahkan");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            toastMsg("Opps.. terjadi kesalahan saat menyimpan!");
    }

    //Mengambil seluruh data dari database ke listview
    private void populateListView() {
        try {
            ArrayList<com.andre_gt.project.studyapp.todolistapplication.ModelData> items = databaseHelper.getAllData();
            com.andre_gt.project.studyapp.todolistapplication.ItemAdapter itemsAdopter = new com.andre_gt.project.studyapp.todolistapplication.ItemAdapter(this, items);
            itemsListView.setAdapter(itemsAdopter);
            itemsAdopter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Menyembunyikan tombol floating tambah saat listview di scroll
    private void hideFab() {
        itemsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    fab.show();
                }else{
                    fab.hide();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void onFabClick() {
        try {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    showAddDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Implementasi klik dari tombol tambah
    @SuppressLint("SimpleDateFormat")
    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getLayoutInflater().getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = inflater.inflate(R.layout.custom_dialog_todo, null);
        dialogBuilder.setView(dialogView);

        final EditText judul = dialogView.findViewById(R.id.edit_title);
        final TextView tanggal = dialogView.findViewById(R.id.date);
        final TextView waktu = dialogView.findViewById(R.id.time);

        final long date = System.currentTimeMillis();
        SimpleDateFormat dateSdf = new SimpleDateFormat("d MMMM");
        String dateString = dateSdf.format(date);
        tanggal.setText(dateString);

        SimpleDateFormat timeSdf = new SimpleDateFormat("hh : mm a");
        String timeString = timeSdf.format(date);
        waktu.setText(timeString);

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        //Set tanggal
        tanggal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getLayoutInflater().getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String newMonth = getMonth(monthOfYear + 1);
                                tanggal.setText(dayOfMonth + " " + newMonth);
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(date);
            }
        });

        //Set waktu
        waktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getLayoutInflater().getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String time;
                                @SuppressLint("DefaultLocale") String minTime = String.format("%02d", minute);
                                if (hourOfDay >= 0 && hourOfDay < 12) {
                                    time = hourOfDay + " : " + minTime + " AM";
                                } else {
                                    if (hourOfDay != 12) {
                                        hourOfDay = hourOfDay - 12;
                                    }
                                    time = hourOfDay + " : " + minTime + " PM";
                                }
                                waktu.setText(time);
                                cal.set(Calendar.HOUR, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                cal.set(Calendar.SECOND, 0);
                                Log.d(TAG, "onTimeSet: Time has been set successfully");
                            }
                        }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        dialogBuilder.setTitle("Buat tugas baru");
        dialogBuilder.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = judul.getText().toString();
                String date = tanggal.getText().toString();
                String time = waktu.getText().toString();
                if (title.length() != 0) {
                    try {
                        insertDataToDb(title, date, time);
                        scheduleNotification(getNotification(title), cal.getTimeInMillis());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    toastMsg("Oops, Gak bisa kosong tugas nya.");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    //Metode pesan toast
    private void toastMsg(String msg) {
        Toast t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0,0);
        t.show();
    }

    //Mengkonversi bulan dari huruf menjadi angka
    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }
}