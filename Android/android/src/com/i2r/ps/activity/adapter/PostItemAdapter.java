package com.i2r.ps.activity.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.i2r.ps.R;
import com.i2r.ps.model.Post;
import com.i2r.ps.util.Constants;
import com.i2r.ps.util.Utils;

public class PostItemAdapter extends ArrayAdapter<Post> {
	private int postItemLayout;

	public PostItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		postItemLayout = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout itemView;

		if (convertView == null) {
			itemView = new LinearLayout(getContext());

			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(postItemLayout, itemView, true);

		} else {
			itemView = (LinearLayout) convertView;
		}

		AQuery aq = new AQuery(itemView);

		Post post = getItem(position);

		String url = Constants.URL_BASE + Constants.PATH_POST_IMAGE
				+ post.getImage_file();
		if (!"".equals(url)) {
			ImageOptions options = new ImageOptions();
			// options.round = 15;
			options.memCache = false;
			options.fileCache = true;
			aq.id(R.id.post_thumb).image(url, options);
		} else {
			aq.id(R.id.post_thumb).image(R.drawable.empty);
		}

		Drawable drawable = getContext().getResources().getDrawable(
				R.drawable.title);
		drawable.setBounds(0, 0, 32, 32);

		TextView categoryTv = (TextView) itemView
				.findViewById(R.id.post_category);
		int CategoryID = post.getCategory();
		categoryTv.setText(post.getCategoryText(CategoryID));
		drawable = getContext().getResources().getDrawable(
				R.drawable.post_title);
		drawable.setBounds(0, 0, 32, 32);
		categoryTv.setCompoundDrawables(drawable, null, null, null);
		categoryTv.setCompoundDrawablePadding(10);

		TextView descTv = (TextView) itemView.findViewById(R.id.post_desc);
		descTv.setText(post.getDescription());
		drawable = getContext().getResources()
				.getDrawable(R.drawable.post_desc);
		drawable.setBounds(0, 0, 32, 32);
		descTv.setCompoundDrawables(drawable, null, null, null);
		descTv.setCompoundDrawablePadding(10);


		TextView unTv = (TextView) itemView.findViewById(R.id.post_username);
		unTv.setText(post.getUsername());
		Log.d("User_name", "" + post.getUsername());
		drawable = getContext().getResources()
				.getDrawable(R.drawable.post_user);
		drawable.setBounds(0, 0, 32, 32);
		unTv.setCompoundDrawables(drawable, null, null, null);
		unTv.setCompoundDrawablePadding(10);


		TextView dateTv = (TextView) itemView.findViewById(R.id.post_date);
		dateTv.setText(Utils.formatDate(post.getCreated_on()));
		drawable = getContext().getResources()
				.getDrawable(R.drawable.post_date);
		drawable.setBounds(0, 0, 32, 32);
		dateTv.setCompoundDrawables(drawable, null, null, null);
		dateTv.setCompoundDrawablePadding(10);


		return itemView;
	}

}
