package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.chris.androidmasters.Objects.Project;
import com.example.chris.androidmasters.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

public class AddProjectDetailsActivity extends AppCompatActivity{

    private Activity context = this;
    private Uri imageUri;
    public static final int PICK_IMAGE = 1;
    private Project project;
    private int selectedyear, selectedmonth, selectedday;
    private int selectedHour, selectedMinute;

    public static Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_details);

        act = this;

        getSupportActionBar().setTitle("Add New Project");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


//        --------------------- Set Deadline -------------------
        final EditText edtDate = (EditText)findViewById(R.id.edt_date);
        edtDate.setOnClickListener(new View.OnClickListener() {
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

                        edtDate.setText(formatter.format(selectedday) + " / " + formatter.format(selectedmonth + 1) + " / "
                                + selectedyear);

                    }
                },mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();


            }
        });

        final EditText edttime = (EditText)findViewById(R.id.edt_time);
        edttime.setOnClickListener(new View.OnClickListener() {
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

                        edttime.setText( formatter.format(selectedHour) + ":" + formatter.format(selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

//        ----------------- SELECT IMAGE --------------------------
        Button btnimage = (Button)findViewById(R.id.btn_image);
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        Button bntnnext = (Button)findViewById(R.id.btn_next);
        bntnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //public Project (String name,String desc,String org,Date date,String goal,Uri img)

                TextView name = (TextView)findViewById(R.id.edt_name);
                TextView desc = (TextView)findViewById(R.id.edt_description);
                TextView org = (TextView)findViewById(R.id.edt_org);
                TextView goal = (TextView)findViewById(R.id.edt_goal);
                TextView date = (TextView)findViewById(R.id.edt_date);
                TextView time = (TextView)findViewById(R.id.edt_time);



                DecimalFormat formatter = new DecimalFormat("00");

                String year = String.valueOf(selectedyear);
                String month = formatter.format(selectedmonth + 1);
                String day = formatter.format(selectedday);

                String hour =formatter.format(selectedHour);
                String min = formatter.format(selectedMinute);

                String datef = year+month+day+hour+min;

//                Log.d("DATE_SELECT", year+month+day+hour+min);
//
//                Date newdate = new Date();
//                try {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
//
//                    newdate = sdf.parse(datef);
//                    Log.d("DATE_SELECT", newdate.toString());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                if((name.getText().toString().replace(" ", "")).equals("")){
                    Toast.makeText(context, "Fill in Name", Toast.LENGTH_SHORT).show();

                }else if ((desc.getText().toString().replace(" ", "")).equals("")){
                    Toast.makeText(context, "Fill in Description", Toast.LENGTH_SHORT).show();

                }else if ((org.getText().toString().replace(" ", "")).equals("")){
                    Toast.makeText(context, "Organization", Toast.LENGTH_SHORT).show();
                }else if (goal.getText().toString().equals("")){
                    Toast.makeText(context, "Goal", Toast.LENGTH_SHORT).show();
                }else if (date.getText().toString().equals("")){
                    Toast.makeText(context, "Set a Target Date", Toast.LENGTH_SHORT).show();
                }else if (time.getText().toString().equals("")){
                    Toast.makeText(context, "Set a Target Time", Toast.LENGTH_SHORT).show();
                }else {

                    if(imageUri != null){
                        Intent intent = new Intent(context,AddProjectObjectivesActivity.class);
                        intent.putExtra("name",  name.getText().toString());
                        intent.putExtra("desc",  desc.getText().toString());
                        intent.putExtra("org",  org.getText().toString());
                        intent.putExtra("date",  datef);
                        intent.putExtra("goal",  goal.getText().toString());
                        intent.putExtra("image",  imageUri.toString());
                        startActivity(intent);
                    }else{
                        Toast.makeText(context, "Set an Image", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            if (data != null){

                imageUri = data.getData();
                final ImageView ivdisplay = (ImageView)findViewById(R.id.iv_display);

                if(imageUri != null){
                    Picasso.with(context)
                            .load(imageUri)
                            .placeholder(getRandomDrawbleColor())
                            .error(getRandomDrawbleColor())
                            .into(ivdisplay);
                }
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private ColorDrawable getRandomDrawbleColor() {

        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    private ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#9ACCCD")), new ColorDrawable(Color.parseColor("#8FD8A0")),
                    new ColorDrawable(Color.parseColor("#CBD890")), new ColorDrawable(Color.parseColor("#DACC8F")),
                    new ColorDrawable(Color.parseColor("#D9A790")), new ColorDrawable(Color.parseColor("#D18FD9")),
                    new ColorDrawable(Color.parseColor("#FF6772")), new ColorDrawable(Color.parseColor("#DDFB5C"))
            };

}


//public Project (String name,String desc,String org,Date date,String goal,Uri img)
//FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    Map<String, Object> devices = new HashMap<>();
//                    devices.put("date", newdate);
//
//                            db.collection("Sample").add(devices);