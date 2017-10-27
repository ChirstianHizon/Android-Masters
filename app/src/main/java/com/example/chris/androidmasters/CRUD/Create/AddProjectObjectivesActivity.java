package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chris.androidmasters.R;

import java.util.ArrayList;

public class AddProjectObjectivesActivity extends AppCompatActivity {
    public static Activity act;
    private Activity context = this;
    private ArrayList<String> objectives;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_objectives);

        act = this;

        getSupportActionBar().setTitle("Add Objectives");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String desc = intent.getStringExtra("desc");
        final String org = intent.getStringExtra("org");
        final String date = intent.getStringExtra("date");
        final String goal = intent.getStringExtra("goal");
        final String image = intent.getStringExtra("image");

        ListView lvobj = (ListView) findViewById(R.id.lv_obj);

        values = new ArrayList<>();
        objectives = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        lvobj.setAdapter(adapter);


        Button btnadd = (Button) findViewById(R.id.btn_add);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLangDialog();
            }
        });

        Button btnclear = (Button) findViewById(R.id.btn_clear);

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                objectives = new ArrayList<>();
            }
        });

        Button btnnext = (Button) findViewById(R.id.btn_next);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (values.size() > 0) {
                    Intent intent = new Intent(context, AddProjectContactsActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("desc", desc);
                    intent.putExtra("org", org);
                    intent.putExtra("date", date);
                    intent.putExtra("goal", goal);
                    intent.putExtra("image", image);
                    intent.putStringArrayListExtra("objectives", objectives);
                    startActivity(intent);
                } else {

                    Toast.makeText(context, "Select atleast 1 Objective", Toast.LENGTH_SHORT).show();

                }

//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//                Map<String, Object> devices = new HashMap<>();
//                devices.put("objectives", objectives);
//
//                db.collection("Sample").add(devices);

            }
        });

    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_objective, null);
        dialogBuilder.setView(dialogView);

        final EditText objective = (EditText) dialogView.findViewById(R.id.edt_objectives);

        dialogBuilder.setTitle("Add a New Objective");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (!(objective.getText().toString().replace(" ", "")).equals("")) {
                    adapter.add("\u2022" + "  " + objective.getText().toString());
                    objectives.add(objective.getText().toString());
                    objective.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(objective.getWindowToken(), 0);
                } else {
                    Toast.makeText(context, "Missing Fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
