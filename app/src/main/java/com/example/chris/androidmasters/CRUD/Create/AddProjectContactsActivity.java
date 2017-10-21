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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chris.androidmasters.Adapters.ProjectCRUDContactsAdapter;
import com.example.chris.androidmasters.Objects.Contacts;
import com.example.chris.androidmasters.R;

import java.util.ArrayList;
import java.util.List;

public class AddProjectContactsActivity extends AppCompatActivity {
    private Activity context = this;
    private List<Contacts> contactList;
    private ProjectCRUDContactsAdapter adapter;
    private ArrayList<String> person;
    private ArrayList<String> position;
    private ArrayList<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_contacts);

        getSupportActionBar().setTitle("Add Contact Persons");
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
        final ArrayList<String> objectives =  intent.getStringArrayListExtra("objectives");



        person = new ArrayList<>();
        position = new ArrayList<>();
        contacts = new ArrayList<>();

        RecyclerView recmain = (RecyclerView)findViewById(R.id.rec_main);
        contactList = new ArrayList<Contacts>();
        adapter = new ProjectCRUDContactsAdapter(context, contactList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recmain.setHasFixedSize(true);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setNestedScrollingEnabled(false);
        recmain.setAdapter(adapter);



        Button btnadd = (Button)findViewById(R.id.btn_add);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLangDialog();
            }
        });

        Button btnclear = (Button)findViewById(R.id.btn_clear);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                adapter.notifyDataSetChanged();

                person = new ArrayList<>();
                position = new ArrayList<>();
                contacts = new ArrayList<>();
            }
        });

        Button btnnext = (Button)findViewById(R.id.btn_next);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(contacts.size() > 0){
                    Intent intent = new Intent(context,AddProjectImagesActivity.class);
                    intent.putExtra("name",  name);
                    intent.putExtra("desc",  desc);
                    intent.putExtra("org",  org);
                    intent.putExtra("date",  date);
                    intent.putExtra("goal",  goal);
                    intent.putExtra("image",  image);
                    intent.putStringArrayListExtra("objectives",  objectives);
                    intent.putStringArrayListExtra("person",  person);
                    intent.putStringArrayListExtra("position",  position);
                    intent.putStringArrayListExtra("contact",  contacts);
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "Select atleast 1 Contact Person", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_contact, null);
        dialogBuilder.setView(dialogView);

        final EditText edtname = (EditText) dialogView.findViewById(R.id.edt_name);
        final EditText edtposition = (EditText) dialogView.findViewById(R.id.edt_position);
        final EditText edtcontacts = (EditText) dialogView.findViewById(R.id.edt_contact);

        dialogBuilder.setTitle("Add Contact Person");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if( !edtname.getText().toString().equals("") && !edtposition.getText().equals("") && !edtcontacts.getText().toString().equals("")){
                    contactList.add(
                            new Contacts(
                                    edtname.getText().toString(),
                                    edtposition.getText().toString(),
                                    edtcontacts.getText().toString()
                            )
                    );
                    adapter.notifyDataSetChanged();
                    person.add(edtname.getText().toString());
                    position.add(edtposition.getText().toString());
                    contacts.add(edtcontacts.getText().toString());
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
