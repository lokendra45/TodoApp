package com.tbcmad.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbcmad.todoapp.data.TodoDAO;
import com.tbcmad.todoapp.data.TodoRepository;
import com.tbcmad.todoapp.model.ETodo;
import com.tbcmad.todoapp.viewModel.TodoViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  Button submit;
    EditTodoFragment editTodoFragment = new EditTodoFragment();
    Button btn =  editTodoFragment.btnSave;
    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    FragmentManager fragmentManager;
    Fragment fragment;
    TodoViewModel viewModel;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragment = new ListTodoFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.list_activity_container, fragment)
                .commit();

        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_delete_all:

                Toast.makeText(getApplicationContext(),"Delete all", Toast.LENGTH_LONG).show();
                item.setOnMenuItemClickListener(this::onOptionsItemSelected);

                break;
                case R.id.mnu_delete_cpmpleted:

                    Toast.makeText(getApplicationContext(),"Delete Completed", Toast.LENGTH_LONG).show();
                    break;
            case R.id.mnu_logout:
                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Logout");
                adb.setMessage("Are you sure you want to Logout");
                adb.setIcon(R.mipmap.ic_launcher);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("todo_pref", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();


                    }});

                adb.show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }


}