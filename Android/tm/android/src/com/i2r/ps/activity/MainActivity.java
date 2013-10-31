package com.i2r.ps.activity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.i2r.ps.R;
import com.i2r.ps.util.Utils;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	protected TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Utils.init(this);

		init();
	}

	private void init() {
		setupTabHost();

		Intent intent = new Intent().setClass(this, MapActivity.class);
		setupTab(new TextView(this), getText(R.string.map), R.drawable.nearby,
				intent);

		intent = new Intent().setClass(this, ListActivity.class);
		setupTab(new TextView(this), getText(R.string.all_posts),
				R.drawable.list, intent);

		intent = new Intent().setClass(this, SubmitActivity.class);
		setupTab(new TextView(this), getText(R.string.submit),
				R.drawable.camera, intent);

		intent = new Intent().setClass(this, LeaderBoardActivity.class);
		setupTab(new TextView(this), getText(R.string.leader_board),
				R.drawable.list, intent);

		intent = new Intent().setClass(this, SettingsActivtiy.class);
		setupTab(new TextView(this), getText(R.string.settings),
				R.drawable.settings, intent);
	}

	protected void setupTab(final View view, final CharSequence tag,
			int drawable, Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag, drawable);
		TabSpec setContent = mTabHost.newTabSpec(tag.toString())
				.setIndicator(tabview).setContent(intent);
		mTabHost.addTab(setContent);
	}

	protected void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
	}

	private View createTabView(final Context context, final CharSequence text,
			int drawable) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		ImageView iv = (ImageView) view.findViewById(R.id.icon);
		iv.setImageResource(drawable);
		return view;
	}

	@Override
	public void onChildTitleChanged(Activity childActivity, CharSequence title) {
		setTitle(title);
	}
}
