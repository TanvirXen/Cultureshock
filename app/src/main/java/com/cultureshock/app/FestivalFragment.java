package com.cultureshock.app;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FestivalFragment extends Fragment {


    @Nullable

    private View FestivalView;
    EditText et_search;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> festival;
    SearchAdapter searchAdapter;
    public FestivalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rview);
        et_search = (EditText) view.findViewById(R.id.ed_search);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        festival = new ArrayList<>();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                  //  Toast.makeText(getContext(), editable.toString(), Toast.LENGTH_LONG).show();
                    setAdapter(editable.toString());
                }
            }
        });
        return view;
    }

    private void setAdapter(final String searchedString) {


       databaseReference.child("Festivals").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                festival.clear();
                recyclerView.removeAllViews();
                int counter = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String name = snapshot.child("name").getValue(String.class);

                    if(name.toLowerCase().contains(searchedString.toLowerCase())){

                        festival.add(name);
                        counter++;
                    }

                    if(counter == 6){
                        break;
                    }
                }
                searchAdapter = new SearchAdapter(getContext(), festival);
                recyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
