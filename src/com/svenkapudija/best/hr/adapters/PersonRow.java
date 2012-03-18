package com.svenkapudija.best.hr.adapters;

public class PersonRow extends DoubleRow {
	
	private String title;
	private String subtitle;
	private String email;
	private String phone;
	private boolean header = false;
	
	public PersonRow() {
		
	}
	
	public PersonRow(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}
	
	public PersonRow(String title) {
		this.title = title;
		this.header = true;
	}
	
	public PersonRow(String title, String subtitle, String email, String phone) {
		this.title = title;
		this.subtitle = subtitle;
		this.email = email;
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
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

	@Override
	public String toString() {
		return "PersonRow [title=" + title + ", subtitle=" + subtitle
				+ ", email=" + email + ", phone=" + phone + "]";
	}

	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}
	
	
}
