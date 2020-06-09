package com.example.fn.ap_intagramclone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {
    // Ui Components
    // globalen EditText anlegen
    private EditText edtProfileName, edtProfileBio, edtProfileProfession,
            edtProfileHobbies, edtProfileFavSport;

    // globalen Button anlegen
    private Button btnUpdateInfo;

    public ProfileTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container,
                false);

        // view auf die UI Components referenzieren und initialisieren
        edtProfileName = view.findViewById(R.id.edtProfileName);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileFavSport = view.findViewById(R.id.edtProfileFavSport);

        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);

        // Referenz zum angemeldeten User erstellen
        final ParseUser parseUser = ParseUser.getCurrentUser();

        // überprüfen ob der Wert für profileName gefüllt ist oder nicht
        if (parseUser.get("profileName") == null) {
            // wenn nicht, leeres Feld mit hint anzeigen
            edtProfileName.setText("");
        } else {
            // für die Profilsicht die eingetragenen Daten laden
            edtProfileName.setText(parseUser.get("profileName").toString());
        }

        // überprüfen ob der Wert für profileBio gefüllt ist oder nicht
        if (parseUser.get("profileBio") == null) {
            // wenn nicht, leeres Feld mit hint anzeigen
            edtProfileBio.setText("");
        } else {
            // für die Profilsicht die eingetragenen Daten laden
            edtProfileBio.setText(parseUser.get("profileBio").toString());
        }

        // überprüfen ob der Wert für profileProfession gefüllt ist oder nicht
        if (parseUser.get("profileProfession") == null) {
            // wenn nicht, leeres Feld mit hint anzeigen
            edtProfileProfession.setText("");
        } else {
            // für die Profilsicht die eingetragenen Daten laden
            edtProfileProfession.setText(parseUser.get("profileProfession").toString());
        }

        // überprüfen ob der Wert für profileHobbies gefüllt ist oder nicht
        if (parseUser.get("profileHobbies") == null) {
            // wenn nicht, leeres Feld mit hint anzeigen
            edtProfileHobbies.setText("");
        } else {
            // für die Profilsicht die eingetragenen Daten laden
            edtProfileHobbies.setText(parseUser.get("profileHobbies").toString());
        }

        // überprüfen ob der Wert für profileFavSport gefüllt ist oder nicht
        if (parseUser.get("profileFavSport") == null) {
            // wenn nicht, leeres Feld mit hint anzeigen
            edtProfileFavSport.setText("");
        } else {
            // für die Profilsicht die eingetragenen Daten laden
            edtProfileFavSport.setText(parseUser.get("profileFavSport").toString());
        }

        // OnClickListener erstellen für die Funktion
        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseUser.put("profileName", edtProfileName.getText().toString());
                parseUser.put("profileBio", edtProfileBio.getText().toString());
                parseUser.put("profileProfession", edtProfileProfession.getText().toString());
                parseUser.put("profileHobbies", edtProfileHobbies.getText().toString());
                parseUser.put("profileFavSport", edtProfileFavSport.getText().toString());

                // eingegebene Daten zu dem User auf dem Server speichern
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        // Abfrage ob kein Error
                        if (e == null) {

                            FancyToast.makeText(getContext(),
                                    "Info Updated",
                                    FancyToast.LENGTH_LONG, FancyToast.INFO,
                                    true).show();
                        } else {
                            FancyToast.makeText(getContext(), e.getMessage(),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR,
                                    true).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}
