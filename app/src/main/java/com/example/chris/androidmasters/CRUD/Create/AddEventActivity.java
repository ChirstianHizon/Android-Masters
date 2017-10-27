package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.chris.androidmasters.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    private int selectedHour;
    private int selectedMinute;
    private Activity context = this;
    private int selectedyear;
    private int selectedmonth;
    private int selectedday;
    private String id;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //        -----------  add back arrow to toolbar ------------
        getSupportActionBar().setTitle("Add Images");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        final EditText name = (EditText)findViewById(R.id.edt_name);
        final EditText desc = (EditText)findViewById(R.id.edt_description);
        final EditText location =(EditText)findViewById(R.id.edt_location);
        final EditText date = (EditText)findViewById(R.id.edt_date);
        final EditText time = (EditText)findViewById(R.id.edt_time);
        final CheckBox volunteer = (CheckBox)findViewById(R.id.cb_volunteer);
        Button btnadd = (Button)findViewById(R.id.btn_add);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int xselectedHour, int xselectedMinute) {

                        selectedHour = xselectedHour;
                        selectedMinute = xselectedMinute;
                        DecimalFormat formatter = new DecimalFormat("00");

                        time.setText(formatter.format(selectedHour) + ":" + formatter.format(selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int xselectedyear, int xselectedmonth, int xselectedday) {

                        selectedyear = xselectedyear;
                        selectedmonth = xselectedmonth;
                        selectedday = xselectedday;
                        DecimalFormat formatter = new DecimalFormat("00");

                        date.setText(formatter.format(selectedday) + " / " + formatter.format(selectedmonth + 1) + " / "
                                + selectedyear);

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 2000);
                mDatePicker.show();

            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO: Validate Fields, Check date is set as today
                addtoEvents(name,desc,location,date,time,volunteer);
            }
        });


    }

    private void addtoEvents(EditText ename,EditText edesc,EditText elocation,EditText edate,EditText etime,CheckBox evolunteer){

        String name = ename.getText().toString();
        String desc = edesc.getText().toString();
        String location = elocation.getText().toString();
        String date = edate.getText().toString();
        String time = etime.getText().toString();
        Boolean volunteer = evolunteer.isActivated();

        Date fbdate = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            fbdate = sdf.parse(date+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Map<String, Object> event = new HashMap<>();
        event.put("date_added", new Date());
        event.put("date_event", fbdate);
        event.put("description", desc);
        event.put("location", location);
        event.put("name", name);
        event.put("project", id);
        event.put("volunteers", volunteer);

        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Uploading data to Server... ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        db.collection("Events").add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable to add Event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
