package com.i2r.ps.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.i2r.ps.model.Post;
import com.i2r.ps.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DbHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "ps.sql";
	private static final int DB_VERSION = 1;
	//
	private Dao<Post, Integer> postsDao = null;
	private Dao<User, Integer> usersDao=null;

	public DbHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, Post.class);
			TableUtils.createTableIfNotExists(connectionSource, User.class);
		} catch (SQLException e) {
			Log.e(DbHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
	}

	public Dao<Post, Integer> getPostsDao() {
		if (null == postsDao) {
			try {
				postsDao = getDao(Post.class);
			} catch (java.sql.SQLException e) {
				Log.e(DbHelper.class.getName(), e.getMessage());
			}
		}
		return postsDao;
	}
	
	public Dao<User, Integer> getUsersDao() {
		if (null == usersDao) {
			try {
				usersDao = getDao(User.class);
			} catch (java.sql.SQLException e) {
				Log.e(DbHelper.class.getName(), e.getMessage());
			}
		}
		return usersDao;
	}

}
