package worskhop.iot.symdroid.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import worskhop.iot.symdroid.R;

public class SettingsFragment extends PreferenceFragment {

    private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            boolean result = false;
            switch (preference.getKey()) {
                case SettingsConstants.P_CORE_AAM: {
                    String oldValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), SettingsConstants.P_CORE_AAM);
                    preference.setSummary(preference.getContext().getString(R.string.p_core_aam_summary, stringValue));
                    result = !stringValue.equalsIgnoreCase(oldValue);
                    break;
                }
                case SettingsConstants.RESOURCE_MAX_DATA_ITEMS: {
                    String oldValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), SettingsConstants.RESOURCE_MAX_DATA_ITEMS);
                    preference.setSummary(preference.getContext().getString(R.string.resource_max_data_items, stringValue));
                    result = !stringValue.equalsIgnoreCase(oldValue);
                    break;
                }
                case SettingsConstants.QUERY_MAX_DATA_ITEMS: {
                    String oldValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), SettingsConstants.QUERY_MAX_DATA_ITEMS);
                    preference.setSummary(preference.getContext().getString(R.string.query_max_data_items, stringValue));
                    result = !stringValue.equalsIgnoreCase(oldValue);
                    break;
                }
                case SettingsConstants.MAX_READ_TIMEOUT: {
                    String oldValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), SettingsConstants.MAX_READ_TIMEOUT);
                    preference.setSummary(preference.getContext().getString(R.string.max_timeout, stringValue));
                    result = !stringValue.equalsIgnoreCase(oldValue);
                    break;
                }
                default:
            }
            return result;
        }


    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's current value.
        try {
            //first try to handle the pref as string
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        } catch (ClassCastException e) {
            //maybe it's a boolean
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), false));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference(SettingsConstants.P_CORE_AAM));
        bindPreferenceSummaryToValue(findPreference(SettingsConstants.RESOURCE_MAX_DATA_ITEMS));
        bindPreferenceSummaryToValue(findPreference(SettingsConstants.QUERY_MAX_DATA_ITEMS));
        bindPreferenceSummaryToValue(findPreference(SettingsConstants.MAX_READ_TIMEOUT));
    }

}

