package worskhop.iot.symdroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import worskhop.iot.symdroid.models.SymbioteResource;

public class SharedPreference {
    public static final String PREFS_NAME = "SENSE";
    public static final String SUBSCRIBED = "SUBSCRIBED";
    private static SharedPreference INSTANCE = null;

    private SharedPreference() {
        super();
    }

    public static SharedPreference getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreference();
        }
        return INSTANCE;
    }

    public void saveResources(Context context, List<SymbioteResource> resources) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(resources);

        editor.putString(SUBSCRIBED, jsonFavorites);

        editor.commit();
    }

    public void addResource(Context context, SymbioteResource resource) {
        List<SymbioteResource> resources = getResources(context);
        if (resources == null)
            resources = new ArrayList<SymbioteResource>();
        resources.add(resource);
        saveResources(context, resources);
    }

    public void removeResource(Context context, SymbioteResource product) {
        ArrayList<SymbioteResource> resources = getResources(context);
        if (resources != null) {
            resources.remove(product);
            saveResources(context, resources);
        }
    }

    public ArrayList<SymbioteResource> getResources(Context context) {
        SharedPreferences settings;
        List<SymbioteResource> resources;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(SUBSCRIBED)) {
            String jsonFavorites = settings.getString(SUBSCRIBED, null);
            Gson gson = new Gson();
            SymbioteResource[] subscribedItems = gson.fromJson(jsonFavorites,
                    SymbioteResource[].class);

            resources = Arrays.asList(subscribedItems);
            resources = new ArrayList<>(resources);
        } else
            return null;

        return (ArrayList<SymbioteResource>) resources;
    }
}