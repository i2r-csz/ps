package com.i2r.ps.activity.dialog;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.http.protocol.HTTP;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.webkit.WebView;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.i2r.ps.R;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class AboutDialog {

	public static void popupAboutDialog(Context ctx) {
		WebView wv = new WebView(ctx);
		String aboutTpl = Utils.getInstantce().readRawResource(R.raw.about);

		About about = new About();
		PackageInfo packageInfo = Utils.getInstantce().getPackageInfo();
		about.packageName = packageInfo.packageName;
		about.versionName = packageInfo.versionName;

		StringWriter sw = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile(new StringReader(aboutTpl), aboutTpl);
		try {
			mustache.execute(sw, about).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		wv.loadDataWithBaseURL("", sw.toString(), "text/html", HTTP.UTF_8, "");

		new AlertDialog.Builder(ctx)
				.setTitle(R.string.menu_about)
				.setView(wv)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).show();
	}

	static class About {
		String versionName = "";
		String email = Constants.CONTACT_EMAIL;
		String appName = "";
		String packageName = "";
	}
}
