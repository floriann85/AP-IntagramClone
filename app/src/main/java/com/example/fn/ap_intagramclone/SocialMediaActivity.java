package com.example.fn.ap_intagramclone;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class SocialMediaActivity extends AppCompatActivity {
    // Ui Components
    // globale Toolbar anlegen
    private Toolbar toolbar;

    // globalen ViewPager anlegen
    private ViewPager viewPager;

    // globalen TabLayout anlegen
    private TabLayout tabLayout;

    // globalen TabAdapter anlegen
    private TabAdapter tabAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        // den Title für die Activity setzen
        setTitle("Instagram - Social Media App");

        // initialisieren
        toolbar = findViewById(R.id.myToolbar);
        // die Toolbar als ActionBar für die Activity setzen
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager, false);

    }
}
