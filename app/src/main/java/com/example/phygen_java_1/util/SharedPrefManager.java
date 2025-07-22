package com.example.phygen_java_1.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.phygen_java_1.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class SharedPrefManager {

    private static final String TAG = "SharedPrefManager";
    private static SharedPrefManager instance;
    private static final String PREF_NAME = "phygen_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";

    private SharedPreferences prefs;
    private Gson gson;

    private SharedPrefManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                Log.e(TAG, "Context is null, cannot initialize SharedPrefManager");
                return null;
            }
            instance = new SharedPrefManager(context.getApplicationContext());
        }
        return instance;
    }

    public boolean saveToken(String token) {
        if (token == null) {
            Log.w(TAG, "Attempted to save null token");
            return false;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN, token);
        boolean success = editor.commit();
        if (!success) {
            Log.e(TAG, "Failed to save token");
        } else {
            Log.d(TAG, "Token saved successfully");
        }
        return success;
    }

    public String getToken() {
        String token = prefs.getString(KEY_TOKEN, null);
        if (token == null) {
            Log.w(TAG, "No token found");
        } else {
            Log.d(TAG, "Token retrieved: " + token.substring(0, Math.min(token.length(), 20)) + "...");
        }
        return token;
    }

    public boolean saveUser(User user) {
        if (user == null) {
            Log.w(TAG, "Attempted to save null user");
            return false;
        }
        String json = gson.toJson(user);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER, json);
        boolean success = editor.commit();
        if (!success) {
            Log.e(TAG, "Failed to save user");
        } else {
            Log.d(TAG, "User saved successfully");
        }
        return success;
    }

    public User getUser() {
        String json = prefs.getString(KEY_USER, null);
        if (json == null) {
            Log.w(TAG, "No user data found");
            return null;
        }
        try {
            User user = gson.fromJson(json, User.class);
            Log.d(TAG, "User retrieved: " + (user != null ? user.getEmail() : "null"));
            return user;
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Failed to parse user JSON: " + e.getMessage());
            return null;
        }
    }

    public int getUserId() {
        return prefs.getInt("user_id", -1);
    }
    public boolean saveUserId(int userId) {
        if (userId < 0) {
            Log.w(TAG, "Attempted to save invalid userId: " + userId);
            return false;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", userId);
        boolean success = editor.commit();
        if (!success) {
            Log.e(TAG, "Failed to save userId");
        } else {
            Log.d(TAG, "UserId saved successfully: " + userId);
        }
        return success;
    }
    public void clear() {
        prefs.edit().clear().commit();
        Log.d(TAG, "SharedPreferences cleared");
    }
}