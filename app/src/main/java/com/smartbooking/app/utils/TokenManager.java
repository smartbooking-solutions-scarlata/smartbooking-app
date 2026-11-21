package com.smartbooking.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREFS = "smartbooking_session";
    private final SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void guardarSesion(String access, String refresh, String rol, String nombre) {
        prefs.edit()
                .putString("access", access)
                .putString("refresh", refresh)
                .putString("rol", rol)
                .putString("nombre", nombre)
                .apply();
    }

    public String getAccess() { return prefs.getString("access", null); }
    public String getRefresh() { return prefs.getString("refresh", null); }
    public String getRol() { return prefs.getString("rol", null); }
    public String getNombre() { return prefs.getString("nombre", null); }

    public void borrarSesion() { prefs.edit().clear().apply(); }
}