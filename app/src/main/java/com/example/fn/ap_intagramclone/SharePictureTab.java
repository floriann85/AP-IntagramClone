package com.example.fn.ap_intagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener {
    // Ui Components
    // globalen ImageView anlegen
    private ImageView imgShare;

    // globalen EditText anlegen
    private EditText edtDescription;

    // globalen Button anlegen
    private Button btnShareImage;

    // globale Bitmap Variable anlegen
    Bitmap receivedImageBitmap;

    public SharePictureTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        // view auf die UI Components referenzieren und initialisieren
        imgShare = view.findViewById(R.id.imgShare);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnShareImage = view.findViewById(R.id.btnShareImage);

        // OnClickListener erstellen mit Verweis implents der Klasse
        imgShare.setOnClickListener(SharePictureTab.this);
        btnShareImage.setOnClickListener(SharePictureTab.this);

        return view;
    }

    @Override
    public void onClick(View view) {
        // Switch-Case für Auswahl/ Funktion Button
        switch (view.getId()) {

            case R.id.imgShare:
                // Abfrage ob der User erlaubt auf den Speicher des Geräts zuzugreifen
                // ab API 23 wird die Zugriffberechtigung erfragt
                if (android.os.Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                    // Abfrage Zugriff erlauben
                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                } else {
                    // der User hat die Berechtigung bereits erteilt
                    // oder ein niedrigeres System ohne Abfrage
                    getChosenImage();
                }

                break;

            case R.id.btnShareImage:
                // Abfrage ob ein Bild gewählt wurde
                if (receivedImageBitmap != null) {
                    // Abfrage ob eine Beschreibung zu dem Bild hinterlegt wurde
                    if (edtDescription.getText().toString().equals("")) {
                        FancyToast.makeText(getContext(),
                                "Error: You must specify a description.",
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                true).show();
                    } else {
                        // wenn eine Beschreibung zu dem Bild hinterlegt wurde

                        // damit ein Bild auf dem Server hochgeladen werden kann,
                        // muss dieses in einen ByteArray umgewandelt werden
                        // ByteArrayOutputStream Variable anlegen
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        // Bild komprimieren
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        // byteArray anlegen, den byteArrayOutputStream konvertieren und zuweisen
                        byte[] bytes = byteArrayOutputStream.toByteArray();

                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        // ParseObject mit neuer zugewiesener Klasse anlegen
                        ParseObject parseObject = new ParseObject("Photo");
                        // Objekte der Klasse Photo hinzufügen
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", edtDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        // ProgressDialog anlegen
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                // Überprüfung ob keine Fehler aufgetreten ist
                                if (e == null) {
                                    FancyToast.makeText(getContext(),
                                            "Done!!!",
                                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                                            true).show();
                                } else {
                                    FancyToast.makeText(getContext(),
                                            "Unknown error: " + e.getMessage(),
                                            FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                            true).show();
                                }
                                // progressDialog schließen
                                dialog.dismiss();
                            }
                        });
                    }

                } else {
                    // wenn kein Bild gewählt wurde
                    FancyToast.makeText(getContext(),
                            "Error: You must select an image.",
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                            true).show();
                }
                break;
        }
    }

    // Methode anlegen für die Funktion
    private void getChosenImage() {
        /*FancyToast.makeText(getContext(),
                "Now we can access the images",
                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                true).show(); */

        // Intent erstellen für Funktion ausgewählte Bilddatei erhalten
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // die Activity starten
        startActivityForResult(intent, 2000);
    }

    @Override
    // Result Zugriffberechtigung
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            // Abfrage ob der User die Berechtigung erteilt hat
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // Aufruf der Methode
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {

            if (resultCode == Activity.RESULT_OK) {

                //Do something with your captured image.
                try {
                    // URI-Var anlegen und Wert zuweisen
                    Uri selectedImage = data.getData();
                    // StringArray anlegen zum speichern des Datepfads
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Cursor anlegen für den Activity um den Inhalt zu erhalten
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // den Cursor auf das erste Objekt setzen
                    cursor.moveToFirst();
                    // IntVar für ColumnIndex anlegen und den Wert des ersten Objekts setzen
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    // StringVar anlegen für den Dateipfad
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // Bitmap Variable verwenden für Funktion Bilder umwandeln
                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                    // das Bild in dem ImageView setzen
                    imgShare.setImageBitmap(receivedImageBitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
