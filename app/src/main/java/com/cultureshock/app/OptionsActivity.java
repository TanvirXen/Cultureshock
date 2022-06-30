package com.cultureshock.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.ads.*;

import java.util.Arrays;
import java.util.List;

public class OptionsActivity extends AppCompatActivity {

    private Spinner spinner;
    private ImageView btn_go;
    private TextView txt_country;
    private String country;
    //private AdView adView1;
    //private InterstitialAd mInterstitialAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        spinner = (Spinner) findViewById(R.id.sp_explore);
        Bundle bundle = getIntent().getExtras();
        country = bundle.getString("getcountry");
        txt_country = (TextView) findViewById(R.id.tvv_country);
        txt_country.setText(country);
        btn_go = (ImageView) findViewById(R.id.bt_go);

        adView = new AdView(this, "IMG_16_9_APP_INSTALL#268761464240395_268801607569714", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container2);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Toast.makeText(OptionsActivity.this, "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });


        // Request an ad
        adView.loadAd();



        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkOption();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void checkOption(){



        //AdRequest adRequest = new AdRequest.Builder().build();



        String option = spinner.getSelectedItem().toString();
        //Toast.makeText(OptionsActivity.this, option, Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(OptionsActivity.this, TextActivity.class);
            myIntent.putExtra("getCountry", country);
            myIntent.putExtra("getOption", option);
            startActivity(myIntent);


    }
}
