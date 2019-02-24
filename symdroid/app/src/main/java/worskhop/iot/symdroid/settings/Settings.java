package worskhop.iot.symdroid.settings;

import android.content.Context;
import android.preference.PreferenceManager;

public class Settings {

    public static String getCoreAAm(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(SettingsConstants.P_CORE_AAM, SettingsConstants.CORE_AAM_SERVER_URL_DEFAULT);
    }

    public static Integer getMaxDataItemsPerReq(Context ctx) {
        String value = PreferenceManager.getDefaultSharedPreferences(ctx).getString(SettingsConstants.RESOURCE_MAX_DATA_ITEMS, SettingsConstants.RESOURCE_MAX_DATA_ITEMS_DEFAULT);
        return Integer.valueOf(value);
    }

    public static Integer getMaxQueryItemsPerReq(Context ctx) {
        String value = PreferenceManager.getDefaultSharedPreferences(ctx).getString(SettingsConstants.QUERY_MAX_DATA_ITEMS, SettingsConstants.QUERY_MAX_DATA_ITEMS_DEFAULT);
        return Integer.valueOf(value);
    }

    public static Integer getMaxTimeout(Context ctx) {
        String value = PreferenceManager.getDefaultSharedPreferences(ctx).getString(SettingsConstants.MAX_READ_TIMEOUT, SettingsConstants.MAX_READ_TIMEOUT_DEFAULT);
        return Integer.valueOf(value);
    }
}
