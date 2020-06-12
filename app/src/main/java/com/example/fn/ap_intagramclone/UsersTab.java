package com.example.fn.ap_intagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    // Ui Components
    // globale ListView anlegen
    private ListView listView;

    // globale ArrayList anlegen
    private ArrayList<String> arrayList;

    // globalen ArrayAdapter anlegen
    private ArrayAdapter arrayAdapter;

    public UsersTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        // view auf die UI Components referenzieren und initialisieren
        listView = view.findViewById(R.id.listView);

        // anlegen und initialisieren
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);

        // OnItemClickListener erstellen für die Funktion
        listView.setOnItemClickListener(UsersTab.this);

        // OnItemLongClickListener erstellen für die Funktion
        listView.setOnItemLongClickListener(UsersTab.this);

        final TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);

        // bestehende User von der DB auf dem Server abfragen
        // ParseObjekt erstellen
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        // den eingeloggten User nicht mit abfragen für Anzeige
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                // Überprüfung ob keine Fehler aufgetreten ist
                if (e == null) {
                    // Überprüfung ob User vorhanden sind
                    if (users.size() > 0) {

                        for (ParseUser user : users) {
                            arrayList.add(user.getUsername());
                        }
                        // die ListView zu dem Adapter setzen
                        listView.setAdapter(arrayAdapter);
                        // die Animation für den TextView setzten
                        txtLoadingUsers.animate().alpha(0).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // den eingeloggten User erhalten wenn dieser einen der Posts anklickt
        // und diesen der Aktivität des Usersposts übergeben

        // lokalen Intent anlegen mit Zuordnung der Klasse
        Intent intent = new Intent(getContext(), UsersPosts.class);
        // den Usernamen Key und Wert übergeben
        intent.putExtra("username", arrayList.get(position));
        // die Activity starten
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // bestehende User von der DB auf dem Server abfragen
        // für Anzeige bei LongClick auf User
        // ParseObjekt erstellen
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(position));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // Abfrage ob User vorhanden und ob kein Error
                if (user != null && e == null) {
                    /*
                    FancyToast.makeText(getContext(), user.get("profileProfession") + " ",
                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                            true).show();
                     */

                    // PrettyDialog anlegen
                    // der PrettyDialog Klasse muss extra über dependencies implementiert werden,
                    // siehe build.gradle(Module:app)
                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());

                    prettyDialog.setTitle(user.getUsername() + " 's Info")
                            .setMessage(user.get("profileBio") + "\n"
                                    + user.get("profileProfession") + "\n"
                                    + user.get("profileHobbies") + "\n"
                                    + user.get("profileFavSport"))
                            .setIcon(R.drawable.person)
                            .addButton(
                                    "OK",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.pdlg_color_green,  // button background color
                                    new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {
                                            // progressDialog schließen
                                            prettyDialog.dismiss();
                                        }
                                    }
                            )
                            .show();
                }
            }
        });

        return true;
    }
}
