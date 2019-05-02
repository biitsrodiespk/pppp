package com.digismart.util;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

@Component
public class MongoIndexesUtil {

	public void createIndexes(MongoTemplate template, String collectionName, String... fields) {
		MongoCollection<Document> mongoCollection = template.getCollection(collectionName);
		if (mongoCollection != null) {
			for (String key : fields) {
				mongoCollection.createIndex(new BasicDBObject(key, 1));
			}
		}
	}
}
