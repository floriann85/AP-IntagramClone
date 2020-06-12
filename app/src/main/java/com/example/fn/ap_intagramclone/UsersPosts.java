package com.example.fn.ap_intagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {
    // Ui Components
    // globales LinearLayout anlegen
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        // initialisieren
        linearLayout = findViewById(R.id.linearLayout);

        // Intent Variable anlegen und den Wert der Methode (object of type intent) zuweisen
        Intent receivedIntentObject = getIntent();
        final String receivedUserName = receivedIntentObject.getStringExtra("username");

        FancyToast.makeText(this, receivedUserName,
                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                true).show();

        // den Title für die Activity setzen
        setTitle(receivedUserName + "'s posts");

        // bestehenden Post (der Klasse Photo) von der DB auf dem Server abfragen
        // ParseObjekt erstellen
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username", receivedUserName);
        parseQuery.orderByDescending("createdAt");

        // ProgressDialog anlegen
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                // Abfrage ob Objekte vorhanden und ob kein Error
                if (objects.size() > 0 && e == null) {

                    for (ParseObject post : objects) {

                        // TextView anlegen
                        final TextView postDescription = new TextView(UsersPosts.this);
                        // den Text für postDescription setzen, Abfrage der DB Server Klasse "Photo"
                        postDescription.setText(post.get("image_des") + "");
                        // ParseFile anlegen zum erhalten des Bildes von der DB Server Klasse "Photo"
                        ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                // Abfrage ob Daten vorhanden sind und ob kein Error
                                if (data != null && e == null) {
                                    // Bitmap Variable anlegen und konvertieren
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    // Ui Components anlegen in der Codierung der Klasse
                                    ImageView postImageView = new ImageView(UsersPosts.this);
                                    // Layout für das Bild
                                    LinearLayout.LayoutParams imageView_params =
                                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(5, 5, 5, 5);
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    // Layout für die Beschreibung setzen
                                    LinearLayout.LayoutParams des_params = new LinearLayout.LayoutParams
                                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    des_params.setMargins(5, 5, 5, 15);
                                    postDescription.setLayoutParams(des_params);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(Color.RED);
                                    postDescription.setTextColor(Color.WHITE);
                                    postDescription.setTextSize(30f);

                                    // den Views dem linearLayout hinzufügen
                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);

                                }
                            }
                        });
                    }

                } else {
                    // die Activtiy wird geschlossen, da kein Post vorhanden
                    finish();

                    FancyToast.makeText(UsersPosts.this,
                            receivedUserName + " doesn't have any posts!",
                            FancyToast.LENGTH_SHORT, FancyToast.INFO,
                            true).show();
                }
                // progressDialog schließen
                dialog.dismiss();
            }
        });

    }
}

