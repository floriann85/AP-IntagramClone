package com.example.fn.ap_intagramclone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

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

    @Override
    // Methode anlegen für Funktion Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // Methode anlegen für Funktion
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Abfrage ob der User den Camera Icon gewählt hat
        if (item.getItemId() == R.id.postImageItem) {
            // Abfrage ob der User erlaubt auf den Speicher des Geräts zuzugreifen
            // ab API 23 wird die Zugriffberechtigung erfragt
            if (android.os.Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                // Abfrage Zugriff erlauben
                requestPermissions(new String[]
                                {Manifest.permission.READ_EXTERNAL_STORAGE},
                        3000);
            } else {
                // Aufruf der Methode
                captureImage();
            }

        } else if (item.getItemId() == R.id.logoutUserItem) {
            // den angemeldeten User ausloggen
            ParseUser.getCurrentUser().logOut();
            // die aktuelle Activity nach LogOut schließen
            finish();
            // Intent anlegen mit Zuordnung der Klasse für Activity wechseln
            Intent intent = new Intent(SocialMediaActivity.this,
                    SignUp.class);
            // die Activity starten
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // Result Zugriffberechtigung
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3000) {
            // Abfrage ob der User die Berechtigung erteilt hat
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // Aufruf der Methode
                captureImage();
            }
        }
    }

    // Methode anlegen für die Funktion
    private void captureImage() {
        // Intent erstellen für Funktion ausgewählte Bilddatei durch Kamera erhalten
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // die Activity starten
        startActivityForResult(intent, 4000);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4000 && resultCode == RESULT_OK && data != null) {

            if (resultCode == Activity.RESULT_OK) {

                // Do something with your captured image.
                try {
                    // URI-Var anlegen und Wert zuweisen
                    Uri capturedImage = data.getData();

                    // Bitmap Variable anlgen
                    Bitmap bitmap = MediaStore.Images.Media.
                            getBitmap(this.getContentResolver(),
                                    capturedImage);

                    // damit ein Bild auf dem Server hochgeladen werden kann,
                    // muss dieses in einen ByteArray umgewandelt werden
                    // ByteArrayOutputStream Variable anlegen
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    // Bild komprimieren
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    // byteArray anlegen, den byteArrayOutputStream konvertieren und zuweisen
                    byte[] bytes = byteArrayOutputStream.toByteArray();

                    ParseFile parseFile = new ParseFile("img.png", bytes);
                    // ParseObject mit neuer zugewiesener Klasse anlegen
                    ParseObject parseObject = new ParseObject("Photo");
                    // Objekte der Klasse Photo hinzufügen
                    parseObject.put("picture", parseFile);
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    // ProgressDialog anlegen
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Loading...");
                    dialog.show();

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SocialMediaActivity.this,
                                        "Pictured Uploaded!",
                                        FancyToast.LENGTH_SHORT, FancyToast.INFO,
                                        true).show();
                            } else {
                                FancyToast.makeText(SocialMediaActivity.this,
                                        "Unknown error: " + e.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                        true).show();
                            }
                            // progressDialog schließen
                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
