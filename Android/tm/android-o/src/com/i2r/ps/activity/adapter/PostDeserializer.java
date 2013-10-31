package com.i2r.ps.activity.adapter;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.i2r.ps.model.Post;

public class PostDeserializer implements JsonDeserializer<Post> {

	@Override
	public Post deserialize(JsonElement ele, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		Post post = new Post();

		JsonObject jsonObject = ele.getAsJsonObject();
		post.setDescription(jsonObject.get("description").getAsString());

		post.setId(jsonObject.get("id").getAsLong());
		post.setUid(jsonObject.get("uid").getAsInt());
		post.setImage_file(jsonObject.get("image_file").getAsString());

		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
			post.setCreated_on(format.parse(jsonObject.get("created_on")
					.getAsString()));
		} catch (ParseException e) {
			Log.e(DateDeserializer.class.getName(), e.getMessage());
		}
		return post;
	}

}
