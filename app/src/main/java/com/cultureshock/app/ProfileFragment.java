package com.cultureshock.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.facebook.ads.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String  senderUserID ;

    private TextView userProfileName,  userEmail, userLocation ,userNumber, txt_credits;
    private Button ChangePassword, support;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private View ProfileView;
    private ImageView support_btn, changepass_btn, signout_btn;
    private ProgressDialog progressDialog;
    private AdView adView;


    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ProfileView = inflater.inflate(R.layout.fragment_profile, container, false);
        AudienceNetworkAds.initialize(getContext());
        adView = new AdView(getContext(), "IMG_16_9_APP_INSTALL#268761464240395_268778207572054", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) ProfileView.findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();



        userProfileName = (TextView) ProfileView.findViewById(R.id.name);
        userEmail = (TextView) ProfileView.findViewById(R.id.email);
        userNumber = (TextView) ProfileView.findViewById(R.id.number);
        support_btn = (ImageView) ProfileView.findViewById(R.id.btn_csupport);
        changepass_btn = (ImageView) ProfileView.findViewById(R.id.btn_passchange);
        signout_btn = (ImageView) ProfileView.findViewById(R.id.signoutbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        txt_credits = (TextView) ProfileView.findViewById(R.id.tv_credits);
        txt_credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Credits.class);
                startActivity(intent);
            }
        });
      //  userProfileName.setText(firebaseUser.getUid().toString());

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid().toString());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userProfileName.setText(dataSnapshot.child("name").getValue().toString());
                userNumber.setText("Contact Number: " + dataSnapshot.child("contact").getValue().toString());
                userEmail.setText("Email: " + dataSnapshot.child("email").getValue().toString());
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                userProfileName.setText(databaseError.getMessage());
                progressDialog.dismiss();
            }
        });

        support_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactSupport();
            }
        });
        changepass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass();
            }
        });

        return ProfileView;
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void changePass(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        firebaseAuth.sendPasswordResetEmail(firebaseAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Password Reset Email Sent", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void contactSupport(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","support@culturesshock.com", null));
        startActivity(Intent.createChooser(intent, "Choose Email App"));
    }
}
