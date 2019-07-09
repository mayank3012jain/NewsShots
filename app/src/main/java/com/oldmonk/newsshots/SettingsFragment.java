package com.oldmonk.newsshots;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.preference.ListPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.preference.PreferenceFragmentCompat;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences.OnSharedPreferenceChangeListener mListener;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);

        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            AppDatabase database;
            String mUserID;

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                mUserID = getContext().getSharedPreferences(Utils.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
                        .getString(Utils.SP_LOGGED_IN_ID, "NoUserpassed");
                database = Room.databaseBuilder(getContext(), AppDatabase.class, Utils.DATABASE_NAME_USER)
                        .allowMainThreadQueries()
                        .build();
                UserInfoDAO userDAO = database.getUserInfoDAO();
                UserInfo currentUser = userDAO.getUserWithID(mUserID);

                if (key.equals(getString(R.string.preference_key_primary_location))) {
                    String value = sharedPreferences.getString(
                            getString(R.string.preference_key_primary_location), "in");
                    currentUser.setPrimaryLocation(value);
                    ListPreference lp = (ListPreference)findPreference(getString(R.string.preference_key_primary_location));
                    lp.setSummary("Selected location is "+ value);

                } else if (key.equals(getString(R.string.multiple_location_allowed))) {
                    currentUser.setHasSecondaryLocation(
                            sharedPreferences.getBoolean(key, false)
                    );
                    if (sharedPreferences.getBoolean(key, false)) {
                        currentUser.setSecondaryLocation(
                                sharedPreferences.getString(getString(R.string.preference_key_secondary_location), "us")
                        );
                    }
                } else if (key.equals(getString(R.string.preference_key_secondary_location))) {
                    String value = sharedPreferences.getString(getString(R.string.preference_key_secondary_location), "us");
                    currentUser.setSecondaryLocation(value);
                    ListPreference lp = (ListPreference)findPreference(getString(R.string.preference_key_primary_location));
                    lp.setSummary("Selected location is "+ value);
                }
                userDAO.update(currentUser);
//                Intent startMainActivity = new Intent(getContext(), MainActivity.class);
//                startActivity(startMainActivity);


/*
                String secLoc = sharedPreferences.getString(
                        getString(R.string.preference_key_secondary_location), "us"
                );
                String primLoc = sharedPreferences.getString(
                        getString(R.string.preference_key_primary_location), "in"
                );
                String rIdSecLoc = "item_location_" + secLoc;
                String rIdPrimLoc = "item_location_" + primLoc;

                navigationView.getMenu().findItem(R.id.item_location_in).setVisible(false);
                navigationView.getMenu().findItem(R.id.item_location_us).setVisible(false);
                navigationView.getMenu().findItem(R.id.item_location_fr).setVisible(false);
                navigationView.getMenu().findItem(R.id.item_location_jp).setVisible(false);

                navigationView.getMenu().findItem(getResources().getIdentifier(rIdPrimLoc, "id", getPackageName())).setVisible(false);
                if (sharedPreferences.getBoolean(key, false)) {
                    navigationView.getMenu().findItem(getResources().getIdentifier(rIdSecLoc, "id", getPackageName())).setVisible(false);
                }
*/


            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
        Intent startMainActivity = new Intent(getContext(), MainActivity.class);
        startActivity(startMainActivity);
    }
}
