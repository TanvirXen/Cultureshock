package com.cultureshock.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FestivalScreen extends AppCompatActivity {

    private TextView tv_name, tv_para;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    //private InterstitialAd mInterstitialAd2;

  //  private AdView adView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_festival_screen);
        progressDialog = new ProgressDialog(FestivalScreen.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);



        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("fest_name");
        tv_name = (TextView) findViewById(R.id.tv_festname);
        tv_para = (TextView) findViewById(R.id.tv_para2);
        tv_name.setText(s);
        databaseReference = FirebaseDatabase.getInstance().getReference("Festivals").child(s);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String para = dataSnapshot.child("description").getValue().toString();
                para = para.replaceAll("(\\\\n\\\\n)","\n\n");
                tv_para.setText(para);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                tv_para.setText(databaseError.getMessage());
                progressDialog.dismiss();
            }
        });


    }
}
