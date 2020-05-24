package com.example.fn.ap_intagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Ui Components
    // globalen Button anlegen
    private Button btnLoginActivity, btnSignUpLoginActivity;

    // globalen EditText anlegen
    private EditText edtLoginEmail, edtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // den Title für die Activity setzen
        setTitle("Instagram - Log in");

        // initialisieren
        btnLoginActivity = findViewById(R.id.btnLoginActivity);
        btnSignUpLoginActivity = findViewById(R.id.btnSignUpLoginActivity);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        // OnClickListener erstellen mit Verweis implents der Klasse
        btnLoginActivity.setOnClickListener(LoginActivity.this);
        btnSignUpLoginActivity.setOnClickListener(LoginActivity.this);

        // Abfrage ob ein User eingeloggt ist
        if (ParseUser.getCurrentUser() != null) {
            // den bereits angemeldeten User ausloggen
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View view) {
        // Switch-Case für Auswahl/ Funktion Button
        switch (view.getId()) {
            case R.id.btnLoginActivity:
                // Abfrage ob die Werte bei dem Login gefüllt sind
                if (edtLoginEmail.getText().toString().equals("") ||
                        edtLoginPassword.getText().toString().equals("")) {

                    FancyToast.makeText(LoginActivity.this,
                            "Email, Password is required!",
                            FancyToast.LENGTH_LONG, FancyToast.INFO,
                            true).show();
                } else {

                    // bestehenden User von der DB auf dem Server abfragen
                    // ParseObjekt erstellen
                    ParseUser.logInInBackground(edtLoginEmail.getText()
                                    .toString(), edtLoginPassword.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    // Abfrage ob User vorhanden und ob kein Error
                                    if (user != null && e == null) {
                                        FancyToast.makeText(LoginActivity.this,
                                                user.get("username") + " is logged in successfully",
                                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                        // Methode aufrufen
                                        transitionToSocialMediaActivity();
                                    } else {
                                        FancyToast.makeText(LoginActivity.this,
                                                e.getMessage(), FancyToast.LENGTH_LONG,
                                                FancyToast.ERROR, true).show();
                                    }
                                }
                            });
                }

                break;

            case R.id.btnSignUpLoginActivity:
                // Intent anlegen mit Zuordnung der Klasse für Activity wechseln
                Intent intent = new Intent(LoginActivity.this,
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
        Intent intent = new Intent(LoginActivity.this,
                SocialMediaActivity.class);
        // die Activity starten
        startActivity(intent);
    }
}
