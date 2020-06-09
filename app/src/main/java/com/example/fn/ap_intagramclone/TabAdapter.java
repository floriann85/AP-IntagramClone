package com.example.fn.ap_intagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int tabPosition) {

        // Switch-Case für Auswahl/ Funktion Tab
        switch (tabPosition) {

            case 0:
                // Var zur Klasse anlegen
                ProfileTab profileTab = new ProfileTab();
                return profileTab;

            case 1:
                // ein neues Objekt der Klasse anlegen und zurückgeben
                return new UsersTab();

            case 2:
                // ein neues Objekt der Klasse anlegen und zurückgeben
                return new SharePictureTab();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // definieren wieviele Tab zurückgegeben werden müssen
        return 3;
    }

    @Nullable
    @Override
    // die Benennung der Tabs festlegen
    public CharSequence getPageTitle(int position) {
        // Switch-Case für Benennung
        switch (position) {

            case 0:
                return "Profile";

            case 1:
                return "Users";

            case 2:
                return "Share Picture";

            default:
                return null;
        }
    }
}
