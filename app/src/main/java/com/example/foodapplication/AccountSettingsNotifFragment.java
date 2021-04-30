package com.example.foodapplication;

import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class AccountSettingsNotifFragment extends PreferenceFragmentCompat {
    SwitchPreferenceCompat SwitchPreferenceNotif, SwitchPreferencePromos;
    EditTextPreference txtPreferenceProvince;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.account_notif_settings, rootKey);

        SwitchPreferenceNotif = findPreference("SwitchPreferenceNotif");
        SwitchPreferencePromos = findPreference("SwitchPreferencePromos");
        txtPreferenceProvince = findPreference("txtPreferenceProvince");

        SwitchPreferenceNotif.setOnPreferenceClickListener(p -> {
            SwitchPreferencePromos.setVisible(SwitchPreferenceNotif.isChecked());
            return true;
        });

        SwitchPreferencePromos.setOnPreferenceClickListener(p -> {
            txtPreferenceProvince.setVisible(SwitchPreferencePromos.isChecked());
            return true;
        });
    }
}