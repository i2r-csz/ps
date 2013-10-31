package com.i2r.ps.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.i2r.ps.db.DbManager;

public class Utils {
	private static Utils instance;

	public static Utils init(Context ctx) {
		if (instance == null) {
			synchronized (Utils.class) {
				if (instance == null) {
					instance = new Utils(ctx);
				}
			}
		}
		CfManager.init(ctx);
		DbManager.init(ctx);
		return instance;
	}

	public static Utils getInstantce() {
		return instance;
	}

	private Utils(Context ctx) {
		this.ctx = ctx;
	}

	private Context ctx;

	@SuppressLint("SimpleDateFormat")
	public static String formatDate(Date date) {
		String dateTxt = "";
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					Constants.DATE_FORMAT);
			formatter.setTimeZone(TimeZone.getDefault());
			dateTxt = formatter.format(date);
		}
		return dateTxt;
	}

	public String readRawResource(int resourceId) {
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

	public String getAppName() {
		Resources appR = ctx.getResources();
		CharSequence appName = appR.getText(appR.getIdentifier("app_name",
				"string", ctx.getPackageName()));
		return appName.toString();
	}

	public PackageInfo getPackageInfo() {
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

	public Bitmap readBitmap(Uri selectedImage) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = ctx.getContentResolver().openAssetFileDescriptor(
					selectedImage, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bm = BitmapFactory.decodeFileDescriptor(
						fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

}
