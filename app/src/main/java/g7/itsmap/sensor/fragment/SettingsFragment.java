package g7.itsmap.sensor.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.Patterns;

import g7.itsmap.sensor.R;
import g7.itsmap.sensor.notification.ReminderManager;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {

            Preference preference = getPreferenceScreen().getPreference(i);

            if (preference instanceof PreferenceGroup) {

                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {

                    updatePreference(preferenceGroup.getPreference(j));
                }

            } else {

                updatePreference(preference);
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference pref = findPreference(key);

        if (pref instanceof EditTextPreference) {

            EditTextPreference textPref = (EditTextPreference) pref;

            if (textPref.getKey().equals("pref_sensor_ip_address")) {

                if (this.correctIPAddress(textPref.getText())) {

                    pref.setSummary(textPref.getText());

                } else {

                    this.resetTextPreference(textPref, "pref_sensor_ip_address", null);
                }

            } else if (textPref.getKey().equals("pref_sensor_port")) {

                if (this.correctPort(textPref.getText())) {

                    pref.setSummary(textPref.getText());

                } else {

                    this.resetTextPreference(textPref, "pref_sensor_port", null);
                }

            } else if (textPref.getKey().equals("pref_doctor_email")) {

                if (this.correctEmail(textPref.getText())) {

                    pref.setSummary(textPref.getText());

                } else {

                    this.resetTextPreference(textPref, "pref_doctor_email", null);
                }

            }  else if (textPref.getKey().equals("pref_doctor_max_temperature")) {

                if (this.correctTemperature(textPref.getText())) {

                    pref.setSummary(textPref.getText());

                } else {

                    this.resetTextPreference(textPref, "pref_doctor_max_temperature", "37");
                }

            }   else if (textPref.getKey().equals("pref_doctor_max_humidity")) {

                if (this.correctHumidity(textPref.getText())) {

                    pref.setSummary(textPref.getText());

                } else {

                    this.resetTextPreference(textPref, "pref_doctor_max_humidity", "50");
                }

            } else {

                pref.setSummary(textPref.getText());
            }

        } else if (pref instanceof CheckBoxPreference) {

            CheckBoxPreference checkPref = (CheckBoxPreference) pref;
            if (checkPref.getKey().equals("pref_plus_notification_forgot")) {

                if(checkPref.isChecked()) {

                    ReminderManager.getInstance(getActivity().getApplicationContext()).startReminded();

                } else {

                    ReminderManager.getInstance(getActivity().getApplicationContext()).cancelReminder();
                }
            }
        }
    }

    private void resetTextPreference(EditTextPreference pref, String key, String value) {

        pref.setText(value);
        pref.setSummary(value);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void updatePreference(Preference pref) {

        if (pref instanceof EditTextPreference) {

            EditTextPreference textPref = (EditTextPreference) pref;
            pref.setSummary(textPref.getText());
        }
    }

    private boolean correctIPAddress(String ip) {

        try {

            return Patterns.IP_ADDRESS.matcher(ip).matches();
        }

        catch (RuntimeException ex) {

            ex.printStackTrace();
        }

        return false;
    }

    private boolean correctPort(String port) {

        try
        {
            if (Integer.valueOf(port) < 65536) {

                return true;
            }
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
        }

        return false;
    }

    private boolean correctEmail(String email) {

        try {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        catch (RuntimeException ex) {

            ex.printStackTrace();
        }

        return false;
    }

    private boolean correctTemperature(String temp) {

        try
        {
            if (Integer.valueOf(temp) != null) {

                return true;
            }
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
        }

        return false;
    }

    private boolean correctHumidity(String humidity) {

        try
        {
            if (Integer.valueOf(humidity) != null) {

                return true;
            }
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
        }

        return false;
    }
}