package com.razeditor.app;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class FinishActivity extends AppCompatActivity {

    private InterstitialAd interstitial;

    private ImageView fb, twitter, youtube,my_ads_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        fb = findViewById(R.id.fb);
        twitter = findViewById(R.id.twitter);
        youtube = findViewById(R.id.youtube);

        my_ads_img = findViewById(R.id.my_ads);


       Button about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });






        fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/groups/190749245706199"));
                startActivity(intent);

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ZeroplusLTD"));
                startActivity(intent);

            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/ZeroplusLTD"));
                startActivity(intent);
            }
        });


        Button ExitBTN = findViewById(R.id.exit);
        ExitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdRequest adIRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad Activity
        interstitial = new InterstitialAd(FinishActivity.this);

        // Insert the Ad Unit ID
        interstitial.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        // Interstitial Ad load Request
        interstitial.loadAd(adIRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener()
        {
            public void onAdLoaded()
            {
                // Call displayInterstitial() function when the Ad loads
                displayInterstitial();
            }
        });
    }
    public void displayInterstitial()
    {
        // If Interstitial Ads are loaded then show else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }

    }
    @Override
    public void onBackPressed(){

    }
}
