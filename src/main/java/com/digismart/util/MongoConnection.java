package com.digismart.util;

public enum MongoConnection {

	MAIN_DB("main"), PLANNING_DB("plan");

	private String currentDb;

	MongoConnection(String currentDb) {
		this.currentDb = currentDb;
	}

	public String getValue() {
		return currentDb;
	}
}
