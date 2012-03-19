package com.svenkapudija.best.hr.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.svenkapudija.best.hr.database.DatabaseHelper;
import com.svenkapudija.best.hr.utils.TypeCaster;

public class Member implements DatabaseInterface {
	
	// Used for serializing/deserializing
	private static final String NAME = "name";
	private static final String ROLE = "role";
	private static final String EMAIL = "email";
	private static final String PHONE = "phone";
	
	private String name;
	private String role;
	private String type;
	private String email;
	private String phone;
	private SQLiteDatabase database;
	
	public Member() {
		
	}
	
	public Member(SQLiteDatabase database) {
		this.database = database;
	}
	
	public boolean exists() {
		if(this.database == null)
			return false;
		
		try {
			Cursor result = this.database.rawQuery("SELECT name FROM " + DatabaseHelper.MEMBERS_TABLE_NAME + " WHERE name = '" + URLEncoder.encode(this.getName(), "utf-8") + "'", null);
			if (result.getCount() > 0) {
				result.close();
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	public boolean read() {
		if(this.database == null)
			return false;
		
		try {
			Cursor result = this.database.rawQuery("SELECT role, type, email, phone FROM " + DatabaseHelper.MEMBERS_TABLE_NAME + " WHERE name = '" + URLEncoder.encode(this.getName(), "utf-8") + "'", null);
			if (result.getCount() > 0) {
				result.moveToFirst();
				
				this.setRole(URLDecoder.decode(result.getString(0), "utf-8"));
				this.setType(result.getString(1));
				this.setEmail(result.getString(2));
				this.setPhone(result.getString(3));
				
				result.close();
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<Member> readAll(SQLiteDatabase database) {
		ArrayList<Member> personList = new ArrayList<Member>();
		
		try {
			Cursor result = database.rawQuery("SELECT name FROM " + DatabaseHelper.MEMBERS_TABLE_NAME + "", null);
			while(result.moveToNext()) {
				Member person = new Member(database);
				person.setName(URLDecoder.decode(result.getString(0), "utf-8"));
				person.read();
				personList.add(person);
			}
			result.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return personList;
	}
	
	public static ArrayList<Member> readAll(SQLiteDatabase database, String type) {
		ArrayList<Member> personList = new ArrayList<Member>();
		
		try {
			Cursor result = database.rawQuery("SELECT name FROM " + DatabaseHelper.MEMBERS_TABLE_NAME + " WHERE type = '" + type + "'", null);
			while(result.moveToNext()) {
				Member person = new Member(database);
				person.setName(URLDecoder.decode(result.getString(0), "utf-8"));
				person.read();
				personList.add(person);
			}
			result.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return personList;
	}
	
	public boolean insertOrUpdate() {
		if(this.database == null)
			return false;
		
		try {
			this.database.execSQL("INSERT OR REPLACE INTO " + DatabaseHelper.MEMBERS_TABLE_NAME + " (name, role, type, email, phone) VALUES" +
					"('" +
					URLEncoder.encode(this.getName(), "utf-8") + "','" +
					URLEncoder.encode(this.getRole(), "utf-8") + "','" +
					this.getType() + "','" +
					this.getEmail() + "','" +
					this.getPhone() + 
					"')"
				);
			
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete() {
		if(this.database == null)
			return false;

		try {
			this.database.execSQL("DELETE FROM " + DatabaseHelper.MEMBERS_TABLE_NAME + " WHERE name = '" + URLEncoder.encode(this.getName(), "utf-8") + "'");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deserialize(String jsonString) {
		try {
			JSONObject object = new JSONObject(jsonString);
			
			this.setName(TypeCaster.toString(object.getString(Member.NAME)));
			this.setRole(TypeCaster.toString(object.optString(Member.ROLE)));
			this.setEmail(TypeCaster.toString(object.optString(Member.EMAIL)));
			this.setPhone(TypeCaster.toString(object.optString(Member.PHONE)));
			
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String serialize() {
		try {
			JSONObject object = new JSONObject();
			object.put(Member.NAME, this.getName());
			object.put(Member.ROLE, this.getRole());
			object.put(Member.EMAIL, this.getEmail());
			object.put(Member.PHONE, this.getPhone());
			
			return object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", role=" + role + ", type=" + type
				+ ", email=" + email + ", phone=" + phone + ", database="
				+ database + "]";
	}
}
