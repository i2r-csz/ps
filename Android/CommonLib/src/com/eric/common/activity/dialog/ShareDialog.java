package com.eric.common.activity.dialog;

import com.eric.common.constants.CommonConstants;
import com.eric.common.utils.CommonUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

public class ShareDialog {

	public static void popupShareDialog(Context ctx) {
		PackageInfo packageInfo = CommonUtils.getPackageInfo(ctx);
		String url = "https://play.google.com/store/apps/details?id="
				+ packageInfo.packageName;
		String name = CommonUtils.getAppName(ctx);
		String content = name + " " + url;

		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType(CommonConstants.MIME_PLAIN_TYPE);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, name);
		ctx.startActivity(intent);
	}
}
