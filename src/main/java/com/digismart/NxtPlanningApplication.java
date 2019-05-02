package com.digismart;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import com.digismart.config.ApplicationProperties;

@EnableAspectJAutoProxy
@EnableAsync
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class NxtPlanningApplication {

	@Autowired
	ApplicationProperties applicationProperties;

	public static void main(String[] args) {
		SpringApplication.run(NxtPlanningApplication.class, args);

		// System.out.println(1 - 100);
	}

	@Bean("apiAsyncPool")
	public Executor apiAsyncPool() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}

	@Bean("applyFiltersPool")
	public Executor applyFiltersPool() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}

	@Bean("saveToDbPool")
	public Executor saveToDbPool() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}

	@Bean("updateUserEmailsPool")
	public Executor updateUserEmailsPool() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}

	@Bean("executeRESTConn")
	public Executor executeRESTConn() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}

	@Bean("getMailerHtmlContent")
	public Executor mailComposer() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}

	@Bean("saveSchedulePlan")
	public Executor saveSchedulePlan() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}

	@Bean("removePlan")
	public Executor removePlan() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}

	@Bean("saveScheduleNew")
	public Executor saveScheduleNew() {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(applicationProperties.getThread_size()));
	}
	
}
