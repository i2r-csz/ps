package com.i2r.ps.model;

import java.io.Serializable;
import java.util.Date;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "post")
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ID = "id";

	@DatabaseField(id = true)
	private long id;
	@DatabaseField
	private int uid;
	@DatabaseField
	private String username;

	@DatabaseField
	private String image_file;

	@DatabaseField
	private long clusterid;
	@DatabaseField
	private long verdictid;
	@DatabaseField
	private int user_cp;
	@DatabaseField
	private int user_ip;

	@DatabaseField
	private double lat;
	@DatabaseField
	private double lng;
	@DatabaseField
	private double mlat;
	@DatabaseField
	private double mlng;

	@DatabaseField
	private String address;
	@DatabaseField
	private int category;

	@DatabaseField
	private double accuracy;

	@DatabaseField
	private String subject;
	@DatabaseField
	private String description;
	@DatabaseField
	private String tag;

	@DatabaseField
	private int severity;

	@DatabaseField
	private int vote_up;
	@DatabaseField
	private int vote_down;

	@DatabaseField
	private Date start_date;
	@DatabaseField
	private Date end_date;
	@DatabaseField
	private Date modified_on;
	@DatabaseField
	private Date created_on;

	@DatabaseField
	private boolean complete;
	@DatabaseField
	private String state;
	@DatabaseField
	private int trust;

	private Bitmap image;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getClusterid() {
		return clusterid;
	}

	public void setClusterid(long clusterid) {
		this.clusterid = clusterid;
	}

	public long getVerdictid() {
		return verdictid;
	}

	public void setVerdictid(long verdictid) {
		this.verdictid = verdictid;
	}

	public int getUser_cp() {
		return user_cp;
	}

	public void setUser_cp(int user_cp) {
		this.user_cp = user_cp;
	}

	public int getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(int user_ip) {
		this.user_ip = user_ip;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getMlat() {
		return mlat;
	}

	public void setMlat(double mlat) {
		this.mlat = mlat;
	}

	public double getMlng() {
		return mlng;
	}

	public void setMlng(double mlng) {
		this.mlng = mlng;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public int getVote_up() {
		return vote_up;
	}

	public void setVote_up(int vote_up) {
		this.vote_up = vote_up;
	}

	public int getVote_down() {
		return vote_down;
	}

	public void setVote_down(int vote_down) {
		this.vote_down = vote_down;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getModified_on() {
		return modified_on;
	}

	public void setModified_on(Date modified_on) {
		this.modified_on = modified_on;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getTrust() {
		return trust;
	}

	public void setTrust(int trust) {
		this.trust = trust;
	}

	public String getImage_file() {
		return image_file;
	}

	public void setImage_file(String image_file) {
		this.image_file = image_file;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

}
