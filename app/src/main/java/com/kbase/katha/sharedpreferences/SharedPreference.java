package com.kbase.katha.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kbase.katha.model.Story;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {
    public static final String PREFS_NAME = "story_app";
    public static final String FAVORITES = "story_favourite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Story> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Story product) {
        List<Story> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Story>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Story product) {
        ArrayList<Story> favorites = getFavorites(context);
        System.out.println(favorites.size());
        if (favorites != null) {
            for (int i = 0; i < favorites.size(); i++) {
                if (favorites.get(i).getStoryId().equalsIgnoreCase(product.getStoryId())) {
                    favorites.remove(i);
                }
            }
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Story> getFavorites(Context context) {
        SharedPreferences settings;
        List<Story> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Story[] favoriteItems = gson.fromJson(jsonFavorites,
                    Story[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Story>(favorites);
        } else
            return null;

        return (ArrayList<Story>) favorites;
    }
}
