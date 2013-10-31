package com.eric.common.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.ImageOptions;
import com.eric.common.R;
import com.eric.common.activity.dialog.FeedbackDialog;
import com.eric.common.constants.CommonConstants;

public class GroupAppsActivity extends Activity {

	private static final String CACHE_KEY = "recommend_aaps";

	private static final String COLUMN_AID = "aid";
	private static final String COLUMN_PACKAGE = "package";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_THUMB = "thumb";

	private static final String RECOMMEND_APPS = "Recommend Apps";

	private ListView listView;
	private ArrayAdapter<JSONObject> adapter;

	private AQuery aq;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
				&& getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		setTitle(RECOMMEND_APPS);

		aq = new AQuery(this);

		listView = new ListView(this);
		adapter = new ArrayAdapter<JSONObject>(this, R.layout.activity_sg_apps) {
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				View v = convertView;
				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.activity_sg_apps, null);
				}

				v.setPadding(10, 10, 10, 10);

				JSONObject json = adapter.getItem(position);
				TextView nameTv = (TextView) v.findViewById(R.id.app_name);
				TextView descriptionTv = (TextView) v
						.findViewById(R.id.app_description);
				ImageView iv = (ImageView) v.findViewById(R.id.app_thumb);
				try {
					nameTv.setText(json.getString(COLUMN_NAME));
					descriptionTv.setText(json.getString(COLUMN_DESCRIPTION));

					ImageOptions options = new ImageOptions();
					options.memCache = false;
					options.fileCache = true;
					String url = json.getString(COLUMN_THUMB);

					aq.id(iv).image(url);
				} catch (JSONException e) {
					Log.e(GroupAppsActivity.class.getName(), e.getMessage());
				}

				return v;
			}
		};
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jo = adapter.getItem(position);
				try {
					String packageName = jo.getString(COLUMN_PACKAGE);
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse("market://details?id=" + packageName)));
					} catch (android.content.ActivityNotFoundException anfe) {
						startActivity(new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("http://play.google.com/store/apps/details?id="
										+ packageName)));
					}

					// send to server, update click count.
					String aid = jo.getString(COLUMN_AID);
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(CommonConstants.PARAM_AID, aid);
					aq.ajax(CommonConstants.URL_BASE
							+ CommonConstants.PAHT_CLICK_APP, params,
							String.class, new AjaxCallback<String>() {
								@Override
								public void callback(String url, String ret,
										AjaxStatus status) {
									Log.i(FeedbackDialog.class.getName(), ret);
								}
							});

				} catch (JSONException e) {
					Log.e(GroupAppsActivity.class.getName(), e.getMessage());
				}
			}
		});

		LinearLayout baseLayout = new LinearLayout(this);
		baseLayout.addView(listView);

		setContentView(baseLayout);

		// read from cache.
		JSONArray ja = fromCache();
		update(ja);

		// call host.
		final StringBuilder about = new StringBuilder();
		PackageInfo packageInfo = getPackageInfo();
		about.append(packageInfo.packageName);
		about.append(":");
		about.append(packageInfo.versionName);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(CommonConstants.PARAM_PACKAGE, about.toString());
		int gid = getResources().getInteger(R.integer.group_id);
		params.put(CommonConstants.PARAM_GID, gid);

		aq.ajax(CommonConstants.URL_BASE + CommonConstants.PATH_GET_APPS,
				params, JSONArray.class, new AjaxCallback<JSONArray>() {
					@Override
					public void callback(String url, JSONArray ret,
							AjaxStatus status) {
						if (ret != null) {
							cache(ret);
							update(ret);
						}
					}
				});
	}

	private void update(JSONArray ja) {
		if (ja != null) {
			adapter.clear();
			PackageInfo packageInfo = this.getPackageInfo();
			String packageName = packageInfo.packageName;
			try {
				JSONObject jo;
				for (int i = 0; i < ja.length(); i++) {
					jo = ja.getJSONObject(i);
					if (jo != null
							&& !packageName
									.equals(jo.getString(COLUMN_PACKAGE))) {
						adapter.add(jo);
					}
				}
			} catch (JSONException e) {
				Log.e(GroupAppsActivity.class.getName(), e.getMessage());
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return true;
	}

	private JSONArray fromCache() {
		String fileName = getCacheDir().getPath() + CACHE_KEY;
		String jsonStr = "";
		JSONArray ja = null;
		try {
			jsonStr = read(fileName);
			ja = new JSONArray(jsonStr);
		} catch (Exception e) {
			Log.e(GroupAppsActivity.class.getName(), e.getMessage());
		}
		return ja;
	}

	private void cache(JSONArray ret) {
		String fileName = getCacheDir().getPath() + CACHE_KEY;
		String jsonStr = ret.toString();
		try {
			save(fileName, jsonStr);
		} catch (Exception e) {
			Log.e(GroupAppsActivity.class.getName(), e.getMessage());
		}
	}

	private void save(String fileName, String content) throws Exception {
		byte[] buf = fileName.getBytes(HTTP.UTF_8);

		fileName = new String(buf, HTTP.UTF_8);

		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(content.getBytes());
		fos.close();
	}

	private String read(String fileName) {
		FileInputStream fis;
		ByteArrayOutputStream baos = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				fis = new FileInputStream(file);

				baos = new ByteArrayOutputStream();

				byte[] buf = new byte[2048];
				int len = 0;

				while ((len = fis.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}

				fis.close();
				baos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String ret = "";
		if (baos != null) {
			ret = baos.toString();
		}
		return ret;

	}

	private PackageInfo getPackageInfo() {
		PackageManager pm = getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = pm.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			packageInfo = new PackageInfo();
			packageInfo.packageName = "";
		}
		return packageInfo;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
}
