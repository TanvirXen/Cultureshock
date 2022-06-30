package com.cultureshock.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView tv_sample;
    private Spinner spinner;

    private ImageView btn_exp;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog2;
    private ArrayList <String> countrylist;
   // private AdView mAdView2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressDialog2 = new ProgressDialog(getContext());
        progressDialog2.show();
        progressDialog2.setContentView(R.layout.progress_dialog);
        progressDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //return view;




        spinner = (Spinner) view.findViewById(R.id.sp_country);
        btn_exp = (ImageView) view.findViewById(R.id.bt_exp);

       // progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference("Countries");
        countrylist = new ArrayList<String>();
       // countrylist.add("sexy");
        countrylist.add("England");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String s = snapshot.getValue(String.class);
                    countrylist.add(s);
                    progressDialog2.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog2.dismiss();
            }
        });
        Collections.sort(countrylist);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, countrylist);
        spinner.setAdapter(spinnerAdapter);
       // progressDialog2.dismiss();
       // progressDialog2.dismiss();
        btn_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String country = spinner.getSelectedItem().toString();
                Intent intent = new Intent(getActivity(), OptionsActivity.class);
                intent.putExtra("getcountry", country);

                startActivity(intent);
            }
        });

        return view;
    }


}
