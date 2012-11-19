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
import com.ipeirotis.readability.model.Text;
import com.ipeirotis.readability.model.User;
import com.ipeirotis.readability.service.TextService;
import com.ipeirotis.readability.service.UserService;
import com.ipeirotis.readability.test.DatastoreRule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ContextConfig.class)
public class TextServiceTest {
	@Autowired
	TextService textService;

	@Autowired
	UserService userService;

	@Rule
	public DatastoreRule datastoreRule = new DatastoreRule();

	Text text;

	User user;

	@Before
	public void setup() {
		user = new User();
		
		user.setId("o8m0zyetzixyml6g8buhv8vydpag3i");
		
		userService.save(user);
		
		this.text = new Text(user, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
	}

	@Test
	public void testNonNull() {
		assertNotNull(textService);
	}

	@Test
	public void testSave() {
		textService.save(text);
	}

	@Test
	public void testLookup() {
		testSave();
		for (Text t : textService.findAll()) {
			System.err.println(t);
		}
		// for (Text t :
		// textService.findAllByParentKey(text.getOwner().getString()))
		// System.err.println(t.getId());
	}

}
