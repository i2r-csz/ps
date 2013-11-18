package com.eric.common.utils;

import java.io.InputStream;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;

/**
 * Common utils
 * 
 * @author csz
 * 
 */
public class CommonUtils {
	private static CommonUtils instance;

	public static CommonUtils init(Context ctx) {
		if (instance == null) {
			synchronized (CommonUtils.class) {
				if (instance == null) {
					instance = new CommonUtils(ctx);
				}
			}
		}
		return instance;
	}

	public static CommonUtils getInstantce() {
		return instance;
	}

	private CommonUtils(Context ctx) {
		this.ctx = ctx;
	}

	private Context ctx;

	/**
	 * Get the file content inside raw folder.
	 * 
	 * @param resourceId
	 * @return
	 */
	public String readRawResource(int resourceId) {
		return readRawResource(resourceId, ctx);
	}

	/**
	 * 
	 * @param resourceId
	 * @param ctx
	 * @return
	 */
	public static String readRawResource(int resourceId, Context ctx) {
		String content = "";
		try {
			Resources res = ctx.getResources();
			InputStream inS = res.openRawResource(resourceId);

			byte[] b = new byte[inS.available()];
			inS.read(b);
			content = new String(b);
		} catch (Exception e) {
			content = "Error: can't show help.";
		}
		return content;
	}

	/**
	 * Get the package information of the app.
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		return getPackageInfo(ctx);
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = pm.getPackageInfo(ctx.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			packageInfo = new PackageInfo();
			packageInfo.packageName = "";
		}
		return packageInfo;
	}

	/**
	 * Get the app name.
	 * 
	 * @return
	 */
	public String getAppName() {
		return getAppName(ctx);
	}

	public static String getAppName(Context ctx) {
		Resources appR = ctx.getResources();
		CharSequence appName = appR.getText(appR.getIdentifier("app_name",
				"string", ctx.getPackageName()));
		return appName.toString();
	}

	//
	public static String getDeviceName() {
		String deviceName = "";
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			deviceName = model;
		} else {
			deviceName = manufacturer + " " + model;
		}
		return deviceName;
	}

	public static String getAndroidVersion() {
		String androidVersion = "";
		androidVersion = Build.VERSION.SDK_INT + "";
		return androidVersion;
	}
}
