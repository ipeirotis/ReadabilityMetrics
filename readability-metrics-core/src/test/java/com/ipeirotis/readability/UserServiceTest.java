package com.ipeirotis.readability;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.ipeirotis.readability.di.ContextConfig;
import com.ipeirotis.readability.model.User;
import com.ipeirotis.readability.service.UserService;
import com.ipeirotis.readability.test.DatastoreRule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ContextConfig.class)
public class UserServiceTest {
	@Autowired
	UserService userService;
	
	@Rule
	public DatastoreRule datastoreRule = new DatastoreRule();

	User user;

	@Before
	public void setup() {
		user = new User();
		
		user.setId("o8m0zyetzixyml6g8buhv8vydpag3i");
	}

	@Test
	public void testNonNull() {
		assertNotNull(userService);
	}

	@Test
	public void testSave() {
		userService.save(user);
	}

	@Test
	public void testLookup() {
		testSave();
		for (User u : userService.findAll())
			System.err.println(u.getId());
	}

}
