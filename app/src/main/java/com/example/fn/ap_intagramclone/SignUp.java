package com.example.fn.ap_intagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    // Ui Components
    // globalen Button anlegen
    private Button btnSignUp, btnLogIn;

    // globalen EditText anlegen
    private EditText edtEnterEmail, edtUsername, edtEnterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // den Title für die Activity setzen
        setTitle("Instagram - Sign up");

        // initialisieren
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);
        edtEnterEmail = findViewById(R.id.edtEnterEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtEnterPassword = findViewById(R.id.edtEnterPassword);

        // OnKeyListener erstellen für die Funktion SignUp-Button oder Enter gedrückt
        edtEnterPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                // überprüfen ob der User den SignUp-Button oder Enter gedrückt hat
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    // Methode aufrufen
                    onClick(btnSignUp);
                }
                return false;
            }
        });

        // OnClickListener erstellen mit Verweis implents der Klasse
        btnSignUp.setOnClickListener(SignUp.this);
        btnLogIn.setOnClickListener(SignUp.this);

        // Abfrage ob ein User eingeloggt ist
        if (ParseUser.getCurrentUser() != null) {
            // den bereits angemeldeten User ausloggen
            // ParseUser.getCurrentUser().logOut();

            // Methode aufrufen
            transitionToSocialMediaActivity();
        }
    }

    @Override
    public void onClick(View view) {
        // Switch-Case für Auswahl/ Funktion Button
        switch (view.getId()) {
            case R.id.btnSignUp:
                // Abfrage ob die Werte bei der Registrierung gefüllt sind
                if (edtEnterEmail.getText().toString().equals("") ||
                        edtUsername.getText().toString().equals("") ||
                        edtEnterPassword.getText().toString().equals("")) {

                    FancyToast.makeText(SignUp.this,
                            "Email, Username, Password is required!",
                            FancyToast.LENGTH_LONG, FancyToast.INFO,
                            true).show();
                } else {
                    // neuen User der DB auf dem Server hinzufügen
                    // ParseObjekt erstellen
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEnterEmail.getText().toString());
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtEnterPassword.getText().toString());

                    // ProgressDialog anlegen und anzeigen
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up " + edtUsername.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            // Abfrage ob kein Error
                            if (e == null) {
                                FancyToast.makeText(SignUp.this,
                                        appUser.get("username") + " is signed up",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS,
                                        true).show();

                                // Methode aufrufen
                                transitionToSocialMediaActivity();
                            } else {
                                FancyToast.makeText(SignUp.this,
                                        "There was an error " + e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR,
                                        true).show();
                            }

                            // progressDialog nachdem User anlegen (Sign up) schließen
                            progressDialog.dismiss();
                        }
                    });
                }

                break;

            case R.id.btnLogIn:
                // lokalen Intent anlegen mit Zuordnung der Klasse für Activity wechseln
                Intent intent = new Intent(SignUp.this,
                        LoginActivity.class);
                // die Activity starten
                startActivity(intent);

                break;
        }
    }

    // Methode erstellen für Funktion wenn User in dem leeren Bildschirmbereich drückt
    // damit die App nicht abstürzt
    public void rootLayoutTapped(View view) {
        try {
            // lokalen InputMethodManager erstellen
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Methode erstellen für die Funktion aufrufen SocialMediaActivity
    private void transitionToSocialMediaActivity() {

        // lokalen Intent anlegen mit Zuordnung der Klasse für Activity wechseln
        Intent intent = new Intent(SignUp.this,
                SocialMediaActivity.class);
        // die Activity starten
        startActivity(intent);
    }
}
