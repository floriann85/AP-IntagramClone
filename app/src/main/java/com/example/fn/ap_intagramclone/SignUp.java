package com.example.fn.ap_intagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    // globalen Button anlegen
    private Button btnSave, btnGetAllData, btnTransition;

    // globalen EditText anlegen
    private EditText edtName, edtPunchSpeed, edtPunchPower, edtKickSpeed, edtKickPower;

    // globale TextView anlegen
    private TextView txtGetData;

    // globale StringVar anlegen
    private String allKickBoxers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // initialisieren
        btnSave = findViewById(R.id.btnSave);
        btnGetAllData = findViewById(R.id.btnGetAllData);
        btnTransition = findViewById(R.id.btnNextActivity);
        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPunchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);
        txtGetData = findViewById(R.id.txtGetData);

        // OnClickListener erstellen mit Verweis implents der Klasse
        btnSave.setOnClickListener(SignUp.this);

        // OnClickListener für Funktion der TextView anlegen
        txtGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abfrage für Datenabfrage Server erstellen
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
                parseQuery.getInBackground("kp12BOBanT", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        // Abfrage ob der Eintag auf dem Server nicht leer ist
                        if (object != null && e == null) {
                            // den Text in der TextView setzen
                            txtGetData.setText(object.get("name") + " - " + "Punch Power: " + object.get("punchPower"));
                        }
                    }
                });
            }
        });

        // OnClickListener für Funktion des Buttons anlegen
        btnGetAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // die Var initialisieren
                allKickBoxers = "";

                // Abfrage für Datenabfrage Server erstellen
                ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("KickBoxer");

                // Server-Abfrage mit Differenzierung
                // queryAll.whereGreaterThan("punchPower", 3000);
                queryAll.whereGreaterThanOrEqualTo("punchPower", 3000);
                queryAll.setLimit(1);

                // Server-Abfrage
                queryAll.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {

                            if (objects.size() > 0) {

                                for (ParseObject kickBoxer : objects) {
                                    allKickBoxers = allKickBoxers + kickBoxer.get("name") + "\n";
                                }
                                FancyToast.makeText(SignUp.this, allKickBoxers,
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            } else {
                                FancyToast.makeText(SignUp.this, e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                        }
                    }
                });
            }
        });

        // OnClickListener für Funktion des Buttons anlegen
        btnTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent anlegen mit Zuordnung der Klasse
                Intent intent = new Intent(SignUp.this,
                        SignUpLoginActivity.class);
                // die Activity starten
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View v) {
        try {
            // User der DB auf dem Server hinzufügen
            // ParseObjekt erstellen
            final ParseObject kickBoxer = new ParseObject("KickBoxer");
            kickBoxer.put("name", edtName.getText().toString());
            kickBoxer.put("punchSpeed", Integer.parseInt(edtPunchSpeed.getText().toString()));
            kickBoxer.put("punchPower", Integer.parseInt(edtPunchPower.getText().toString()));
            kickBoxer.put("kickSpeed", Integer.parseInt(edtKickSpeed.getText().toString()));
            kickBoxer.put("kickPower", Integer.parseInt(edtKickPower.getText().toString()));
            // Objekt auf dem Server speichern
            kickBoxer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // Abfrage ob die Speicherung erfolgreich war
                    if (e == null) {
                        FancyToast.makeText(SignUp.this, kickBoxer.get("name") + " is saved to server", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    } else {
                        FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
        }
    }
}
