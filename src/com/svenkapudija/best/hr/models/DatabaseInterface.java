package com.svenkapudija.best.hr.models;

public interface DatabaseInterface {
	
	/**
	 * Checks if object with current ID already exists in database, if it does, pull all the parameters from database into object.
	 * 
	 * @return <code>TRUE</code> if object with that ID exists in database, <code>FALSE</code> if it doesn't
	 */
	public boolean exists();
	
	/**
	 * Retrieves the object from database if it exists and deserializes it.
	 * 
	 * @return <code>TRUE</code> if object with that ID exists in database, <code>FALSE</code> if it doesn't or there was a problem with deserializing
	 */
	public boolean read();
	
	/**
	 * Insert object into database. If exists, replace it.
	 * 
	 * @return <code>TRUE</code> if operation succeeded, else <code>FALSE</code>
	 */
	public boolean insertOrUpdate();
	
	/**
	 * Deletes object from database based on object.id.
	 * 
	 * @return <code>TRUE</code> if operation succeeded, else <code>FALSE</code>
	 */
	public boolean delete();
	
	/**
	 * Used when you need to create object from JSONObject.
	 * 
	 * @param jsonString JSON representation of object
	 * @return <code>TRUE</code> if operation succeeded, else <code>FALSE</code>
	 */
	public boolean deserialize(String jsonString);
	
	/**
	 * Used when you need to serialize object into JSONObject.
	 * 
	 * @param report Object you want to serialize.
	 * @return JSONObject in String representation, null in case of exception
	 */
	public String serialize();
}
