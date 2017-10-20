package com.example.chris.androidmasters.CRUD;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.chris.androidmasters.R;

import java.util.Calendar;

public class AddProjectActivity extends AppCompatActivity{

    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);


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
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        edtDate.setText(selectedday + " / " + (selectedmonth + 1) + " / "
                                + selectedyear);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();
            }
        });
    }

}
