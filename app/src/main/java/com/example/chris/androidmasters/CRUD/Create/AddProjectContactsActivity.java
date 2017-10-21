package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chris.androidmasters.Adapters.ProjectContactsAdapter;
import com.example.chris.androidmasters.Objects.Contacts;
import com.example.chris.androidmasters.R;

import java.util.ArrayList;
import java.util.List;

public class AddProjectContactsActivity extends AppCompatActivity {
    private Activity context = this;
    private List<Contacts> contactList;
    private ProjectContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_contacts);

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
        final String objectives =  intent.getStringExtra("objectives");


        RecyclerView recmain = (RecyclerView)findViewById(R.id.rec_main);
        contactList = new ArrayList<Contacts>();
        adapter = new ProjectContactsAdapter(context, contactList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recmain.setHasFixedSize(true);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setNestedScrollingEnabled(false);
        recmain.setAdapter(adapter);


        showChangeLangDialog();






    }
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText name = (EditText) dialogView.findViewById(R.id.edt_name);
        final EditText position = (EditText) dialogView.findViewById(R.id.edt_position);
        final EditText contacts = (EditText) dialogView.findViewById(R.id.edt_contact);

        dialogBuilder.setTitle("Add Contact Person");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if( !name.getText().toString().equals("") && !position.getText().equals("") && !contacts.getText().toString().equals("")){
                    contactList.add(
                            new Contacts(
                                    name.getText().toString(),
                                    position.getText().toString(),
                                    contacts.getText().toString()
                            )
                    );
                    adapter.notifyDataSetChanged();
                }else{
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
