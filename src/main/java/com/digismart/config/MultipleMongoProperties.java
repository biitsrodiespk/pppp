package com.digismart.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mongodb")
public class MultipleMongoProperties {

	private MongoProperties primary = new MongoProperties();
	private MongoProperties planning = new MongoProperties();

	public MongoProperties getPrimary() {
		return primary;
	}

	public MongoProperties getPlanning() {
		return planning;
	}
}