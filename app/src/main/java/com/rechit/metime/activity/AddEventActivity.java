package com.rechit.metime.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rechit.metime.NotificationReceiver;
import com.rechit.metime.R;
import com.rechit.metime.model.Activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static android.provider.Settings.System.DATE_FORMAT;

public class AddEventActivity extends AppCompatActivity {
    private static final String TAG = "AddEventActivity";
    private static final String KEY_ID= "id";
    private static final String KEY_TITLE= "title";
    private static final String KEY_DATE= "date";
    private static final String KEY_TIME= "time";
    private static final String KEY_DETAILS = "description";
    private Activity activity;

    private EditText editTitle, editDesc;
    private Button btnSave, btnDate, btnTime;
    private Boolean isUpdate;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTitle = findViewById(R.id.edt_title);
        editDesc = findViewById(R.id.edt_detail);
        btnSave = findViewById(R.id.btn_save);
        btnDate = findViewById(R.id.btn_showdatepicker);
        btnTime = findViewById(R.id.btn_showtimepicker);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        isUpdate = getIntent().hasExtra("activities");
        if (isUpdate){
            activity = getIntent().getParcelableExtra("activities");
            editTitle.setText(activity.getTitle());
            editDesc.setText(activity.getDescription());
            btnDate.setText(activity.getDate());
            btnTime.setText(activity.getTime());
            btnSave.setText("Update");
        }else{
            activity = new Activity();
            btnDate.setText(getCurrentDate());
            btnTime.setText(getCurrentTime());
        }
        
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });
        
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                String title= editTitle.getText().toString();
                String desc = editDesc.getText().toString();
                String date = btnDate.getText().toString();
                String time = btnTime.getText().toString();

                if (isUpdate){
                    updateActivity(activity.getId(),title,desc, date, time);
                }else {
                    AddActivity(id, title, desc, date, time);
                }

                sendNotification(title, desc , id);
            }
        });
    }

    private void sendNotification(String title, String desc, String id){
        Intent notificationIntent= new Intent (this, NotificationReceiver.class);
        notificationIntent.putExtra("id", id);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("desc", desc);
        PendingIntent notifPendingIntent= PendingIntent.getBroadcast(AddEventActivity.this, 0 , notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        String date = btnDate.getText().toString();
        String time = btnTime.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/YY HH:mm", new Locale("in", "ID"));
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(dateFormat.parse(date+ " " + time).getTime());
        } catch (ParseException e) {
            Log.e(TAG,"error",e);
            e.printStackTrace();
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifPendingIntent);
    }

    public void AddActivity(String id, String title, String desc, String date, String time){

        if(!title.isEmpty()&&!desc.isEmpty()){
            Map<String, Object> event  = new HashMap<>();
            event.put(KEY_ID, id);
            event.put(KEY_TITLE, title);
            event.put(KEY_DETAILS, desc);
            event.put(KEY_DATE, date);
            event.put(KEY_TIME, time);

            db.collection("User").document(firebaseUser.getUid())
                    .collection("Activity").document(id).set(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddEventActivity.this, "Aktivitas Tersimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddEventActivity.this, "Aktivitas Tidak Tersimpan", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,e.toString());
                        }

                    });
        }else{
            Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateActivity(String id, String title, String desc, String date, String time){
        Activity updateActivities = new Activity(id,title,desc, date, time);

        db.collection("User").document(firebaseUser.getUid())
                .collection("Activity")
                .document(id)
                .update(objectToHashMap(updateActivities))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddEventActivity.this, "Activity Has Been Updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(getClass().getSimpleName(),"error",e);
                Toast.makeText(AddEventActivity.this, "Fail to Update The Activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String,Object> objectToHashMap(Activity activity){
        Map<String, Object> document = new HashMap<>();
        document.put("id", activity.getId());
        document.put("title", activity.getTitle());
        document.put("description",activity.getDescription());
        document.put("date", activity.getDate());
        document.put("time", activity.getTime());
        return document;
    }

    private void handleTimeButton() {
        Calendar time = Calendar.getInstance();
        int HOUR = time.get(Calendar.HOUR);
        int MINUTE = time.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", new Locale("in", "ID"));
                String timeNow= simpleDateFormat.format(calendar.getTime());
                btnTime.setText(timeNow);
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd/MM/YY", new Locale("in", "ID"));
                String dateNow= simpleDateFormat.format(calendar.getTime());
                btnDate.setText(dateNow);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/YY", new Locale("in", "ID"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String getCurrentTime(){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", new Locale("in", "ID"));
        Date time = new Date();
        return timeFormat.format(time);
    }
}