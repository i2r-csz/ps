package com.i2r.ps.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class CfManager {
	public static final String PREF_UID = "PREF_UID";
	public static final String PREF_USER_EMAIL = "PREF_USER_EMAIL";

	private static CfManager instance;

	public static CfManager init(Context ctx) {
		if (instance == null) {
			synchronized (CfManager.class) {
				if (instance == null) {
					instance = new CfManager(ctx);
				}
			}
		}
		return instance;
	}

	public static CfManager getInstantce() {
		return instance;
	}

	private Context ctx;
	private SharedPreferences sharedPref;
	private Editor editor;

	private CfManager(Context ctx) {
		this.ctx = ctx;
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this.ctx);
		editor = sharedPref.edit();
	}

	public void setUid(int uid) {
		editor.putInt(PREF_UID, uid);
		editor.commit();
	}

	public int getUid() {
		return sharedPref.getInt(PREF_UID, -1);
	}

	public void setUserEmail(String userEmail) {
		editor.putString(PREF_USER_EMAIL, userEmail);
		editor.commit();
	}

	public String getUserEmail() {
		return sharedPref.getString(PREF_USER_EMAIL, "");
	}
}
