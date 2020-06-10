package com.example.fn.ap_intagramclone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment {
    // Ui Components
    // globale ListView anlegen
    private ListView listView;

    // globale ArrayList anlegen
    private ArrayList arrayList;

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
}
