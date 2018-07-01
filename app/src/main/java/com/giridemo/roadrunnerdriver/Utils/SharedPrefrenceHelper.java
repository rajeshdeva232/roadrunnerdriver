package com.giridemo.roadrunnerdriver.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPrefrenceHelper {

    private SharedPreferences _pref;
    private static final String PREF_FILE = "eLegal";
    private SharedPreferences.Editor _editorPref;

    public SharedPrefrenceHelper(Context context) {
        _pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        _editorPref = _pref.edit();
    }

    public Set<String> getSets(String key){
        return _pref.getStringSet(key,null);
    }
    public Boolean getBoolean(String key){
        return _pref.getBoolean(key,false);
    }

    public void saveBoolean(String key, Boolean value){
        _editorPref.putBoolean(key, value);
        _editorPref.commit();
    }
    public void saveSets(String key, boolean value){
        _editorPref.putBoolean(key, value);
        _editorPref.commit();
    }

    public void saveString(String key, String value) {
        _editorPref.putString(key, value);
        _editorPref.commit();
    }

    public String getString(String key) {
        return _pref.getString(key, "");
    }

    public void saveInt(String key, int value) {
        _editorPref.putInt(key, value);
        _editorPref.commit();
    }

    public int getInt(String key) {
        return _pref.getInt(key, 0);
    }

    public void clearSharedRefrence() {
        _pref.edit().clear().commit();
    }
}
