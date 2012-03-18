package com.svenkapudija.best.hr.adapters;

import com.svenkapudija.best.hr.models.Event;

public class EventRow {
	
	private String headerTitle;
	private Event event;
	private boolean header = false;
	
	public EventRow() {
		
	}
	
	public EventRow(String headerTitle) {
		this.headerTitle = headerTitle;
		this.header = true;
	}
	
	public EventRow(Event event) {
		this.event = event;
	}

	public String getHeaderTitle() {
		return headerTitle;
	}

	public void setHeaderTitle(String headerTitle) {
		this.headerTitle = headerTitle;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	@Override
	public String toString() {
		return "EventRow [headerTitle=" + headerTitle + ", event=" + event
				+ ", header=" + header + "]";
	}
}