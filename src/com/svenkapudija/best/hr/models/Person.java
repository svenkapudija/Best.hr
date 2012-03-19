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

import com.svenkapudija.best.hr.utils.TypeCaster;

public class Person implements DatabaseInterface {
	
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
	
	public Person() {
		
	}
	
	public Person(SQLiteDatabase database) {
		this.database = database;
	}
	
	public boolean exists() {
		if(this.database == null)
			return false;
		
		try {
			Cursor result = this.database.rawQuery("SELECT name FROM best_members WHERE name = '" + URLEncoder.encode(this.getName(), "utf-8") + "'", null);
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
			Cursor result = this.database.rawQuery("SELECT role, type, email, phone FROM best_members WHERE name = '" + URLEncoder.encode(this.getName(), "utf-8") + "'", null);
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
	
	public static ArrayList<Person> readAll(SQLiteDatabase database) {
		ArrayList<Person> personList = new ArrayList<Person>();
		
		try {
			Cursor result = database.rawQuery("SELECT name FROM best_members", null);
			while(result.moveToNext()) {
				Person person = new Person(database);
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
	
	public static ArrayList<Person> readAll(SQLiteDatabase database, String type) {
		ArrayList<Person> personList = new ArrayList<Person>();
		
		try {
			Cursor result = database.rawQuery("SELECT name FROM best_members WHERE type = '" + type + "'", null);
			while(result.moveToNext()) {
				Person person = new Person(database);
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
			this.database.execSQL("INSERT OR REPLACE INTO best_members (name, role, type, email, phone) VALUES" +
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
			this.database.execSQL("DELETE FROM best_members WHERE name = '" + URLEncoder.encode(this.getName(), "utf-8") + "'");
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
			
			this.setName(TypeCaster.toString(object.getString(Person.NAME)));
			this.setRole(TypeCaster.toString(object.optString(Person.ROLE)));
			this.setEmail(TypeCaster.toString(object.optString(Person.EMAIL)));
			this.setPhone(TypeCaster.toString(object.optString(Person.PHONE)));
			
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String serialize() {
		try {
			JSONObject object = new JSONObject();
			object.put(Person.NAME, this.getName());
			object.put(Person.ROLE, this.getRole());
			object.put(Person.EMAIL, this.getEmail());
			object.put(Person.PHONE, this.getPhone());
			
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
