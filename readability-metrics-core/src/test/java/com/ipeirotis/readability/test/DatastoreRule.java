package com.ipeirotis.readability.test;

import org.junit.rules.ExternalResource;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFilter;

public class DatastoreRule extends ExternalResource {
	private LocalServiceTestHelper helper;

	@Override
	protected void before() throws Throwable {
		LocalDatastoreServiceTestConfig testConfig = new LocalDatastoreServiceTestConfig();
		
		testConfig.setNoStorage(false);
		
		this.helper = new LocalServiceTestHelper(testConfig);
		
		helper.setUp();
	}
	
	@Override
	protected void after() {
		ObjectifyFilter.complete();
		
		helper.tearDown();
	}

}
