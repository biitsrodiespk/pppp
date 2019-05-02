package com.digismart.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableConfigurationProperties(MultipleMongoProperties.class)
public class MultipleMongoConfig {

	@Autowired
	private MultipleMongoProperties mongoProperties;

	@Primary
	@Bean(name = "primaryMongoTemplate")
	public MongoTemplate primaryMongoTemplate() throws Exception {
		return new MongoTemplate(primaryFactory(this.mongoProperties.getPrimary()));
	}

	@Bean(name = "planningMongoTemplate")
	public MongoTemplate secondaryMongoTemplate() throws Exception {
		return new MongoTemplate(planningFactory(this.mongoProperties.getPlanning()));
	}

	@Bean
	@Primary
	public MongoDbFactory primaryFactory(final MongoProperties mongo) throws Exception {
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		seeds.add(new ServerAddress(mongo.getHost(), mongo.getPort()));
		MongoCredential credentials = MongoCredential.createScramSha1Credential(mongo.getUsername(),
				mongo.getAuthenticationDatabase(), mongo.getPassword());
		MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
		MongoClient mongoClient = new MongoClient(seeds, credentials, mongoClientOptions);
		return new SimpleMongoDbFactory(mongoClient, mongo.getDatabase());
	}

	@Bean
	public MongoDbFactory planningFactory(final MongoProperties mongo) throws Exception {
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		seeds.add(new ServerAddress(mongo.getHost(), mongo.getPort()));
		MongoCredential credentials = MongoCredential.createScramSha1Credential(mongo.getUsername(),
				mongo.getAuthenticationDatabase(), mongo.getPassword());
		MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
		MongoClient mongoClient = new MongoClient(seeds, credentials, mongoClientOptions);
		return new SimpleMongoDbFactory(mongoClient, mongo.getDatabase());
	}

	/*@Bean
	@Primary
	public MongoDbFactory primaryFactory(final MongoProperties mongo) throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()), mongo.getDatabase());
	}

	@Bean
	public MongoDbFactory planningFactory(final MongoProperties mongo) throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()), mongo.getDatabase());
	}*/

}