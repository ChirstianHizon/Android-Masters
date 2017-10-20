package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chris.androidmasters.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddProjectObjectivesActivity extends AppCompatActivity {
    private Activity context = this;
    private ArrayList<String> objectives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_objectives);

        getSupportActionBar().setTitle("Add Objectives");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String desc = intent.getStringExtra("desc");
        final String org =  intent.getStringExtra("org");
        final String date = intent.getStringExtra("date");
        final String goal = intent.getStringExtra("goal");
        final String image =  intent.getStringExtra("image");

        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();

        ListView lvobj = (ListView)findViewById(R.id.lv_obj);

        final ArrayList<String> values = new ArrayList<>();
        objectives = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        lvobj.setAdapter(adapter);


        Button btnadd = (Button)findViewById(R.id.btn_add);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText objective = (EditText)findViewById(R.id.edt_objectives);
                if(!objective.getText().toString().equalsIgnoreCase("")){
                    adapter.add("\u2022"+ "  "+objective.getText().toString());
                    objectives.add(objective.getText().toString());
                    objective.setText("");
                    Toast.makeText(context, "Objective Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnclear = (Button)findViewById(R.id.btn_clear);

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                objectives = new ArrayList<>();
            }
        });

        Button btnnext = (Button)findViewById(R.id.btn_next);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(context,AddProjectObjectivesActivity.class);
//                intent.putExtra("name",  name);
//                intent.putExtra("desc",  desc);
//                intent.putExtra("org",  org);
//                intent.putExtra("date",  date);
//                intent.putExtra("goal",  goal);
//                intent.putExtra("image",  image);
//                intent.putStringArrayListExtra("objectives",  values);
//                startActivity(intent);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> devices = new HashMap<>();
                devices.put("objectives", objectives);

                db.collection("Sample").add(devices);

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
