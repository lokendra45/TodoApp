package com.tbcmad.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class LoginActivity extends AppCompatActivity  {

    Button btnLogin;
    EditText userName;
    EditText password;
    CheckBox savecheckBox;
    Boolean authentication;
    @SuppressLint("StaticFieldLeak")
    static LoginActivity lg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.login_activity_btn_login);
        userName = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        savecheckBox = findViewById(R.id.checkBox);

        String regexPassword = ".{8,}";
        AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.editTextTextEmailAddress, Patterns.EMAIL_ADDRESS, R.string.err_email);
        mAwesomeValidation.addValidation(this, R.id.editTextTextPassword, RegexTemplate.NOT_EMPTY, R.string.password_error);
        mAwesomeValidation.addValidation(this, R.id.editTextTextPassword, regexPassword, R.string.invalid_password);

        SharedPreferences preference = getApplicationContext().getSharedPreferences("todo_pref", 0);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAwesomeValidation.validate()) {
                    saveLogin();

                }else{
                    DialogForInvalid();
                }
            }

        });
       authentication= preference.getBoolean("authentication",true);
       if(authentication==true){

           userName.setText(preference.getString("username",null));
           password.setText(preference.getString("password",null));




       }

        }
        private  void saveLogin(){

            String username = userName.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            if (username.equals("Admin@todo.com") && userPassword.equals("admin12345")||(username.equals("lok@todo.com")&&userPassword.equals("lok123456"))) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                if(savecheckBox.isChecked()){
                    SharedPreferences preference = getApplicationContext().getSharedPreferences("todo_pref", 0);
                   SharedPreferences.Editor editor = preference.edit();
                    editor.putBoolean("authentication", true);
                    editor.putString("username",username);
                    editor.putString("password",userPassword);
                    editor.apply();

                }


            }else{
                DialogForInvalid();

            }

        }
    private void DialogForInvalid(){
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Invalid User");
        alertDialog.setMessage("Username or Password Invalid");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Enter Again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

}