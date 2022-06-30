package com.cultureshock.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegActivity extends AppCompatActivity {

    private EditText et_name;
    EditText et_pass, et_cpass, et_contact, et_email;
    private ImageButton bt_reg;
    private String s1, s2;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        et_name = (EditText) findViewById(R.id.ed_fname);
        et_email = (EditText) findViewById(R.id.ed_email);
        et_pass = (EditText) findViewById(R.id.ed_pass);
        et_cpass = (EditText) findViewById(R.id.ed_cpass);
        et_contact = (EditText) findViewById(R.id.ed_contact);
        bt_reg = (ImageButton) findViewById(R.id.btn_reg);

        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkField()){
                    execute();
                }
            }
        });
    }

    private void execute(){
        progressDialog = new ProgressDialog(RegActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final String name = et_name.getText().toString().trim();
        final String contact = et_contact.getText().toString().trim();
        final String email = et_email.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String id = firebaseAuth.getUid().toString();
                                Users users = new Users(name, email, contact);
                                databaseReference.child(id).setValue(users);
                                Intent myIntent = new Intent(RegActivity.this, EmailSent.class);
                                startActivity(myIntent);
                            } else {
                                Toast.makeText(RegActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                else {
                    progressDialog.dismiss();
                    //Toast.makeText(RegActivity.this, "entered", Toast.LENGTH_LONG).show();
                    if(task.getException() instanceof FirebaseAuthWeakPasswordException){
                        Toast.makeText(RegActivity.this, "Password too weak", Toast.LENGTH_LONG).show();
                    }
                    else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(RegActivity.this, "Email Already Registered", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private boolean checkField(){
        s1 = et_pass.getText().toString().trim();
        s2 = et_cpass.getText().toString().trim();

        boolean check = true;
        if(TextUtils.isEmpty(et_name.getText().toString().trim()) ||
                TextUtils.isEmpty(et_email.getText().toString().trim()) ||
                TextUtils.isEmpty(et_pass.getText().toString().trim()) ||
                TextUtils.isEmpty(et_cpass.getText().toString().trim()) ||
                TextUtils.isEmpty(et_contact.getText().toString().trim())
        ){
            Toast.makeText(this, "Please fill up all the fields",
                    Toast.LENGTH_SHORT).show();
            check = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()){
            et_email.setError("Please enter a Valid Email Address!");
            check = false;
        }

        else if(!s1.equals(s2)){
            et_cpass.setError("Passwords Don't Match");
            check = false;
        }

        String ph = et_contact.getText().toString().trim();

        if(ph.length() < 10){
            et_contact.setError("Please enter a valid Contact Number");

            check = false;
        }

        return check;
    }
}
