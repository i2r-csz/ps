package com.i2r.ps.activity.adapter;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.i2r.ps.util.Constants;

public class DateDeserializer implements JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonElement ele, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		// "2013-06-19 11:05:55"
		Date date = null;
		try {
			date = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH)
					.parse(ele.getAsString());
		} catch (ParseException e) {
			Log.e(DateDeserializer.class.getName(), e.getMessage());
		}
		return date;
	}
}
