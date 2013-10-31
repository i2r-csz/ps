package com.eric.common.cf;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class CommonCfManager {
	private static final String PREF_IS_PAID_USER = "PREF_IS_PAID_USER";

	private static CommonCfManager instance;

	public static CommonCfManager init(Context ctx) {
		if (instance == null) {
			synchronized (CommonCfManager.class) {
				if (instance == null) {
					instance = new CommonCfManager(ctx);
				}
			}
		}
		return instance;
	}

	public static CommonCfManager getInstantce() {
		return instance;
	}

	private Context ctx;
	private SharedPreferences sharedPref;
	private Editor editor;

	private CommonCfManager(Context ctx) {
		this.ctx = ctx;
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this.ctx);
		editor = sharedPref.edit();
	}

	public boolean isPaidUser() {
		return sharedPref.getBoolean(PREF_IS_PAID_USER, false);
	}

	public void setPaidUser(boolean isPaidUser) {
		editor.putBoolean(PREF_IS_PAID_USER, isPaidUser);
		editor.commit();
	}

}
