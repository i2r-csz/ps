package com.i2r.ps.activity;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.i2r.ps.R;
import com.i2r.ps.activity.adapter.DateDeserializer;
import com.i2r.ps.activity.adapter.PostItemAdapter;
import com.i2r.ps.db.DbManager;
import com.i2r.ps.model.Post;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.HttpService;
import com.i2r.ps.util.Utils;

public class ListActivity extends Activity {

	private PullToRefreshListView listView;
	private PostItemAdapter adapter;

	private List<Post> posts;

	private int pageNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		Utils.init(this);

		listView = (PullToRefreshListView) findViewById(R.id.post_list);
		adapter = new PostItemAdapter(this, R.layout.item_post);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(ListActivity.this,
						PostActivity.class);
				Post post = adapter.getItem(position - 1);
				intent.putExtra(Post.ID, post);
				startActivity(intent);
			}
		});
		// Set a listener to be invoked when the list should be refreshed.
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				//
				new UpdateTask().execute(new Void[] {});

			}
		});

		// Add an end-of-list listener
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(ListActivity.this, "End of List!",
						Toast.LENGTH_SHORT).show();
			}
		});

		posts = DbManager.getInstance().getPosts(pageNum);
		updateList();
	}

	private void updateList() {
		adapter.clear();

		for (Post post : posts) {
			adapter.add(post);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitle(R.string.title_listview);

		updateList();
	}

	class UpdateTask extends AsyncTask<Void, Void, Void> {
		boolean isError;

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			String url = Constants.URL_BASE + Constants.PATH_POST_LIST;
			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put(Constants.PARAM_PAGE_NUM, 0 + "");
			try {
				String postsStr = HttpService.doPost(url, kvPairs, HTTP.UTF_8);

				GsonBuilder gsonB = new GsonBuilder();
				gsonB.registerTypeAdapter(Date.class, new DateDeserializer());
				Gson gson = gsonB.create();
				Type type = new TypeToken<List<Post>>() {
				}.getType();
				List<Post> posts = gson.fromJson(postsStr, type);

				// save
				DbManager.getInstance().savePosts(posts);
			} catch (Exception e) {
				isError = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			listView.onRefreshComplete();

			if (isError) {
				Toast.makeText(ListActivity.this, R.string.error_to_load_posts,
						Toast.LENGTH_SHORT).show();
			} else {
				posts = DbManager.getInstance().getPosts(0);

				updateList();
			}
		}

	}
}
