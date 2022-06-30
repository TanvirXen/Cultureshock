package com.cultureshock.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Credits extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView tv_credits;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(Credits.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_credits);
        tv_credits = (TextView) findViewById(R.id.tv_research);
        databaseReference = FirebaseDatabase.getInstance().getReference("Researcher");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Toast.makeText(TextActivity.this, country + ":"+option, Toast.LENGTH_LONG).show();
                String r = dataSnapshot.child("names").getValue().toString();

                // r  = r.replaceAll("Drinking", "<br />");
                r = r.replaceAll("(\\\\n)","\n");
                //Toast.makeText(TextActivity.this, r, Toast.LENGTH_LONG).show();
                tv_credits.setText(r);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                tv_credits.setText(databaseError.getMessage());
                progressDialog.dismiss();
            }
        });
    }
}
