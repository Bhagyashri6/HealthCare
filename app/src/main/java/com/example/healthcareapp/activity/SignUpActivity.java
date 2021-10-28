package com.example.healthcareapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthcareapp.R;
import com.example.healthcareapp.helper.DBHelper;
import com.example.healthcareapp.helper.SessionData;

public class SignUpActivity extends AppCompatActivity {
    EditText et_fristname,et_lastname,et_phoneno,et_email,et_password,et_confirmpassword;
    String  firstname, lastname, phonenumber, email,password,confirmpassword;
    Button btn_signup;
    DBHelper dbHelper;
    SessionData sessionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        dbHelper = new DBHelper(this);
        sessionData = new SessionData(getApplicationContext());

    }


    private void init(){
        et_fristname = findViewById(R.id.et_fristname);
        et_lastname =findViewById(R.id.et_lastname);
        et_phoneno = findViewById(R.id.et_phoneno);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_signup = findViewById(R.id.btn_signup);
        et_confirmpassword = findViewById(R.id.et_confirmpassword);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = et_fristname.getText().toString();
                lastname = et_lastname.getText().toString();
                phonenumber = et_phoneno.getText().toString();
                email = et_email.getText().toString();
                password =et_password.getText().toString();
                confirmpassword = et_confirmpassword.getText().toString();

                if (firstname.isEmpty()){
                    et_fristname.requestFocus();
                    et_phoneno.setError("Enter First Name");
                }else if (lastname.isEmpty()){
                    et_lastname.requestFocus();
                    et_phoneno.setError("Enter Last Name");
                }else if (phonenumber.isEmpty()){
                    et_phoneno.requestFocus();
                    et_phoneno.setError("Enter Phone Number");
                }else if (phonenumber.length() < 10){
                    et_phoneno.requestFocus();
                    et_phoneno.setError("Enter Valid Phone Number");
                }else if (password.isEmpty()){
                    et_password.requestFocus();
                    et_phoneno.setError("Enter Password");
                }else if (!email.isEmpty()){
                    emailValidator(et_email);
                    et_phoneno.setError("Enter Email Id");
                }else if (confirmpassword.isEmpty()){
                    et_confirmpassword.requestFocus();
                    et_phoneno.setError("Enter Same Password As Above");
                }else {
                    if (password.equals(confirmpassword)){
                        Boolean checkuser = dbHelper.checkPhoneNo(phonenumber);
                        if (checkuser == false){
                            Boolean insert = dbHelper.insertData(phonenumber,firstname,lastname,email,password);
                            if (insert == true){
                                Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                sessionData.setString("phoneno",phonenumber);
                                sessionData.setString("password",password);
                                sessionData.setObjectAsString("login", "true");
                                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intent);
                            } else{
                                Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(SignUpActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }

    public void emailValidator(EditText et_email) {
        String emailToText = et_email.getText().toString();

        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            Toast.makeText(this, "Email Verified !", Toast.LENGTH_SHORT).show();
        } else {
            et_email.requestFocus();
            Toast.makeText(this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}