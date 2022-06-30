package com.cultureshock.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TextActivity extends AppCompatActivity {
    private String country, option;
    private TextView tv_country, tv_option, tv_text;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        progressDialog = new ProgressDialog(TextActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tv_country = (TextView) findViewById(R.id.tv_textcountry);
        tv_option = (TextView) findViewById(R.id.tv_fieldtext);
        tv_text = (TextView) findViewById(R.id.tv_para);
        Bundle bundle = getIntent().getExtras();
        country = bundle.getString("getCountry");
        option = bundle.getString("getOption");
        tv_country.setText(country);
        String s = option.toUpperCase();
        tv_option.setText(s);
    
        databaseReference = FirebaseDatabase.getInstance().getReference("Culture").child("Country").child(country).child(option);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               // Toast.makeText(TextActivity.this, country + ":"+option, Toast.LENGTH_LONG).show();
                String r = dataSnapshot.getValue().toString();

               // r  = r.replaceAll("Drinking", "<br />");
                r = r.replaceAll("(\\\\n\\\\n)","\n\n");
                //Toast.makeText(TextActivity.this, r, Toast.LENGTH_LONG).show();
                tv_text.setText(r);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                tv_text.setText(databaseError.getMessage());
                progressDialog.dismiss();
            }
        });

    }
}
