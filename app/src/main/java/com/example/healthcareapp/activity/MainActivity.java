package com.example.healthcareapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.R;
import com.example.healthcareapp.helper.DBHelper;
import com.example.healthcareapp.helper.SessionData;

public class MainActivity extends AppCompatActivity {

    EditText et_phoneno,et_password;
    Button btn_login;
    DBHelper dbHelper;
    TextView tv_signUp;
    String phoneno,password;
    SessionData sessionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        dbHelper = new DBHelper(this);
        sessionData = new SessionData(getApplicationContext());

    }

    private void init(){
        et_phoneno = findViewById(R.id.et_phoneno);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_signUp = findViewById(R.id.tv_signUp);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneno = et_phoneno.getText().toString();
                password = et_password.getText().toString();

                if (phoneno.isEmpty()){
                    et_phoneno.requestFocus();
                }else if (password.isEmpty()){
                    et_password.requestFocus();
                }else {
                    Boolean checkphonepass = dbHelper.checkPhoneNoPassword(phoneno,password);
                    if (checkphonepass == true){
                        Toast.makeText(MainActivity.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        sessionData.setString("phoneno",phoneno);
                        sessionData.setString("password",password);
                        sessionData.setObjectAsString("login", "true");

                        Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit the page?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.finishAffinity(MainActivity.this);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}