package com.i2r.ps.activity;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.i2r.ps.R;
import com.i2r.ps.activity.adapter.DateDeserializer;
import com.i2r.ps.db.DbManager;
import com.i2r.ps.model.User;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.HttpService;
import com.i2r.ps.util.Utils;

public class LeaderBoardActivity extends Activity {
	private String TAG = "LeaderBoardActivity";
	List<User> users;

	private AQuery aq;

	private PullToRefreshListView lv;
	private ArrayAdapter<User> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);
		Utils.init(this);

		aq = new AQuery(this);

		lv = (PullToRefreshListView) findViewById(R.id.user_list);
		adapter = new ArrayAdapter<User>(this, R.layout.item_user) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Log.d(TAG, "LeaderBoardActivity");
				LinearLayout view;
				if (convertView == null) {
					view = new LinearLayout(getContext());

					String inflater = Context.LAYOUT_INFLATER_SERVICE;
					LayoutInflater vi;
					vi = (LayoutInflater) getContext().getSystemService(
							inflater);
					vi.inflate(R.layout.item_user, view, true);
				} else {
					view = (LinearLayout) convertView;
				}

				User user = getItem(position);

				ImageView iv = (ImageView) view.findViewById(R.id.user_avatar);
				String url = Constants.URL_BASE + Constants.PATH_IMAGES
						+ user.getFilename();
				if (!"".equals(url)) {
					ImageOptions options = new ImageOptions();
					// options.round = 15;
					options.memCache = false;
					options.fileCache = true;
					aq.id(iv).image(url, options);
				} else {
					aq.id(iv).image(R.drawable.empty);
				}

				TextView tv1 = (TextView) view.findViewById(R.id.text1);
				tv1.setText(user.getName());
				
				TextView tv3 = (TextView) view.findViewById(R.id.text3);
				//tv3.setText("CP: " + user.getCp());
				tv3.setText("CP: " + user.getDecimalFormatCP());
				
				TextView tv4 = (TextView) view.findViewById(R.id.text4);
				//tv4.setText("EP: " + user.getEp());
				tv4.setText("EP: " + user.getDecimalFormatEP());
				return view;
			}
		};
		lv.setAdapter(adapter);
		
		// Fang-Jing: Click to see user details
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				Intent intent = new Intent(LeaderBoardActivity.this,
						UserActivity.class);
				User user = adapter.getItem(position - 1);
				intent.putExtra(User.UID, user);
				startActivity(intent);
			}
		});
		
		
		

		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new InitTask().execute(new Void[] {});
			}
		});

		lv.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(LeaderBoardActivity.this, "End of List!",
						Toast.LENGTH_SHORT).show();
			}
		});

		users = DbManager.getInstance().getUsers();

		if (users == null || users.size() == 0) {
			new InitTask().execute(new Void[] {});
		} else {
			updateList();
		}
	}

	private void updateList() {
		users = DbManager.getInstance().getUsers();

		adapter.clear();
		adapter.addAll(users);
		adapter.notifyDataSetChanged();
	}

	class InitTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean isSuccess = true;
			String url = Constants.URL_BASE + Constants.PATH_USERS;
			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put(Constants.PARAM_SORT, "CP");
			try {
				String usersStr = HttpService.doPost(url, kvPairs, HTTP.UTF_8);

				GsonBuilder gsonB = new GsonBuilder();
				gsonB.registerTypeAdapter(Date.class, new DateDeserializer());
				Gson gson = gsonB.create();
				Type type = new TypeToken<List<User>>() {
				}.getType();
				List<User> users = gson.fromJson(usersStr, type);

				// save
				DbManager.getInstance().saveUsers(users);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return isSuccess;
		}

		@Override
		protected void onPostExecute(Boolean isSuccess) {
			lv.onRefreshComplete();

			if (!isSuccess) {
				Toast.makeText(LeaderBoardActivity.this,
						R.string.error_to_load_users, Toast.LENGTH_SHORT)
						.show();
			} else {
				updateList();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitle(R.string.title_leader_board);
	}
}
