package com.i2r.ps.activity.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class ShareDialog {

	public static void popupAboutDialog(Context ctx) {
		PackageInfo packageInfo = Utils.getInstantce().getPackageInfo();
		String url = "https://play.google.com/store/apps/details?id="
				+ packageInfo.packageName;
		String name = Utils.getInstantce().getAppName();
		String content = name + " " + url;

		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType(Constants.MIME_PLAIN_TYPE);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, name);
		ctx.startActivity(intent);
	}
}
