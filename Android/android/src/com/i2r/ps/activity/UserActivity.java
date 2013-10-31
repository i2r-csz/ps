package com.i2r.ps.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import com.i2r.ps.R;

import com.i2r.ps.model.User;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class UserActivity extends FragmentActivity {
	private String TAG = "UserActivity";

	private User user;


	private AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		Utils.init(this);
		
		

		//Log.d("fangjing","here1");
		getActionBar().setDisplayHomeAsUpEnabled(true);

		aq = new AQuery(this);
		
		user = (User)getIntent().getExtras().getSerializable(User.UID);
		//post = (Post) getIntent().getExtras().getSerializable(Post.ID);

		//LinearLayout view;
		//view = new LinearLayout(getContext());
		
		TextView tv_username = (TextView) findViewById(R.id.text_username);
		tv_username.setText(user.getName());
		
		
		ImageView iv_userPhoto = (ImageView) findViewById(R.id.user_photo);
		String url = Constants.URL_BASE + Constants.PATH_IMAGES
				+ user.getFilename();
		if (!"".equals(url)) {
			ImageOptions options = new ImageOptions();
			// options.round = 15;
			options.memCache = false;
			options.fileCache = true;
			aq.id(iv_userPhoto).image(url, options);
		} else {
			aq.id(iv_userPhoto).image(R.drawable.empty);
		}
		
		//TextView tv_cp_short_title = (TextView) findViewById(R.id.text_CP_short_title);
		//TextView tv_ep_short_title = (TextView) findViewById(R.id.text_EP_short_title);

		//TextView tv_cp_title = (TextView) findViewById(R.id.text_contribution_power);
		//TextView tv_ep_title = (TextView) findViewById(R.id.text_endorsement_power);

		
		TextView tv_cp = (TextView) findViewById(R.id.user_cp);
		tv_cp.setText(""+user.getCp());
		//tv_cp.setText("CP: " + user.getCp());

		TextView tv_ep = (TextView) findViewById(R.id.user_ep);
		tv_ep.setText("" + user.getEp());
		//tv_ep.setText("EP: " + user.getEp());
		
		
		//TextView tv2 = (TextView) findViewById(R.id.text2);
		//tv1.setText(user.getEmail());



	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
		}
		return true;
	}
}
