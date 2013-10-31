package com.i2r.ps.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String UID = "uid";

	@DatabaseField(id = true)
	private int uid;
	@DatabaseField
	private String email;
	@DatabaseField
	private String name;
	@DatabaseField
	private String nickname;
	@DatabaseField
	private String filename;
	@DatabaseField
	private String birthday;
	@DatabaseField
	private int sex;
	@DatabaseField
	private String role;
	@DatabaseField
	private String hp_number;
	@DatabaseField
	private int avatarid;
	@DatabaseField
	private float cp;
	@DatabaseField
	private float ep;
	@DatabaseField
	private String modified_on;
	@DatabaseField
	private String created_on;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getHp_number() {
		return hp_number;
	}

	public void setHp_number(String hp_number) {
		this.hp_number = hp_number;
	}

	public int getAvatarid() {
		return avatarid;
	}

	public void setAvatarid(int avatarid) {
		this.avatarid = avatarid;
	}

	public float getCp() {
		return cp;
	}

	public void setCp(float cp) {
		this.cp = cp;
	}

	public float getEp() {
		return ep;
	}

	public void setEp(float ep) {
		this.ep = ep;
	}

	public String getModified_on() {
		return modified_on;
	}

	public void setModified_on(String modified_on) {
		this.modified_on = modified_on;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
