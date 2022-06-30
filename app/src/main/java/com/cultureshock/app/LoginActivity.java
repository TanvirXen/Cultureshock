package com.cultureshock.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_pass;
    private ImageButton btn_login;
    private TextView tv_forgot, tv_show;
    FirebaseAuth firebaseAuth;
    private ImageButton btn_reg;
    private ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        
        btn_login = (ImageButton) findViewById(R.id.btn_login);
        et_email = (EditText) findViewById(R.id.ed_loginemail);
        et_pass = (EditText) findViewById(R.id.ed_loginpass);
        tv_forgot = (TextView) findViewById(R.id.tv_fpass);
        tv_show = (TextView) findViewById(R.id.tv_sent);
        btn_reg = (ImageButton) findViewById(R.id.btn_register);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkfield()){
                    execute();
                }
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(myIntent);
            }
        });

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()){
                    et_email.setError("Please enter a Valid Email Address!");
                }
                else{
                    passreset();
                }
            }
        });

    }
    private void passreset(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        firebaseAuth.sendPasswordResetEmail(et_email.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    tv_show.setText("A password reset email has been sent.");
                }
                else{
                    if(task.getException() instanceof FirebaseAuthInvalidUserException){
                        et_email.setError("Email isn't Registered");
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Something went wrong",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void execute(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        String email = et_email.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser.isEmailVerified()){
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(myIntent);
                    }
                    else{
                        et_email.setError("Please verify your Email");
                    }

                }
                else{
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        et_pass.setError("Invalid Password");
                    }
                    else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                        et_email.setError("Email is not registered");
                    }
                }
            }
        });

    }
    private boolean checkfield(){
        boolean check = true;

        if(TextUtils.isEmpty(et_email.getText().toString().trim()) ||
                TextUtils.isEmpty(et_pass.getText().toString().trim())
        ){
            Toast.makeText(this, "Please fill up all the fields",
                    Toast.LENGTH_SHORT).show();
            check = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()){
            et_email.setError("Please enter a Valid Email Address!");
            check = false;
        }

        return check;
    }
}
