package com.jp.test.util;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class SessionHelper {
	private final static String hasAppIDKey = "com.jp.test.hasAppIDKey";
	private final static String strAppIDKey = "com.jp.test.strAppIDKey";
	
	public static boolean hasAppID(final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(hasAppIDKey, false);
	}
	public static void setAppID(final Context context){
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
				sp.edit().putBoolean(hasAppIDKey, true).commit();
				sp.edit().putString(strAppIDKey, UUID.randomUUID().toString()).commit();
				return null;
			}
		}.execute();
	}
	
	public static String getAppIDKey(final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(strAppIDKey, "noAppIDKey");
	}
}
