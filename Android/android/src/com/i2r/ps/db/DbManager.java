package com.i2r.ps.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.i2r.ps.model.Post;
import com.i2r.ps.model.User;
import com.i2r.ps.util.Constants;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class DbManager {

	private static DbManager instance;

	public static void init(Context ctx) {
		if (instance == null) {
			instance = new DbManager(ctx);
		}
	}

	public static DbManager getInstance() {
		return instance;
	}

	private DbHelper helper;

	private DbManager(Context ctx) {
		helper = new DbHelper(ctx);
	}

	public List<Post> getPosts(int pgaeNum) {
		List<Post> posts = null;
		try {
			QueryBuilder<Post, Integer> queryBuilder = helper.getPostsDao()
					.queryBuilder();
			queryBuilder.orderBy(Post.ID, false);
			
			queryBuilder.offset(pgaeNum * Constants.LIMIT_PER_PAGE);
			queryBuilder.limit(Constants.LIMIT_PER_PAGE);
			PreparedQuery<Post> preparedQuery = queryBuilder.prepare();
			posts = helper.getPostsDao().query(preparedQuery);
		} catch (SQLException e) {
			Log.e(DbManager.class.toString(), e.toString());
		}

		return posts;
	}

	public void savePosts(List<Post> posts) throws SQLException {
		
		if (posts != null) {
			for (Post post : posts) {
				Log.d("dateformat_DbManager",""+post.getCreated_on());
				helper.getPostsDao().createOrUpdate(post);
			}
		}
	}

	public void saveUsers(List<User> users) throws SQLException {
		if (users != null) {
			for (User user : users) {
				helper.getUsersDao().createOrUpdate(user);
			}
		}
	}

	public List<User> getUsers() {
		List<User> users = null;
		try {
			QueryBuilder<User, Integer> queryBuilder = helper.getUsersDao()
					.queryBuilder();
			
			queryBuilder.orderBy(User.CP, false);
			PreparedQuery<User> preparedQuery = queryBuilder.prepare();
			users = helper.getUsersDao().query(preparedQuery);
		} catch (SQLException e) {
			Log.e(DbManager.class.toString(), e.toString());
		}

		return users;
	}

	public void savePosts(Post dbPost) {
		try {
			helper.getPostsDao().createOrUpdate(dbPost);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
