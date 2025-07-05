package com.example.phygen_java.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.phygen_java.model.User;
import com.google.gson.Gson;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private static final String PREF_NAME = "phygen_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_USER = "user";

    private SharedPreferences prefs;
    private Gson gson;

    private SharedPrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context.getApplicationContext());
        }
        return instance;
    }

    // Token
    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    // Language
    public void setLanguage(String langCode) {
        prefs.edit().putString(KEY_LANGUAGE, langCode).apply();
    }

    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, "en");
    }

    // User
    public void saveUser(User user) {
        prefs.edit().putString(KEY_USER, gson.toJson(user)).apply();
    }

    public User getUser() {
        String json = prefs.getString(KEY_USER, null);
        return json != null ? gson.fromJson(json, User.class) : null;
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}