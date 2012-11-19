package com.ipeirotis.readability;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipeirotis.readability.di.ContextConfig;
import com.ipeirotis.readability.model.Text;
import com.ipeirotis.readability.model.TextMetric;
import com.ipeirotis.readability.model.User;
import com.ipeirotis.readability.service.ReadabilityService;
import com.ipeirotis.readability.service.TextMetricService;
import com.ipeirotis.readability.service.TextService;
import com.ipeirotis.readability.service.UserService;
import com.ipeirotis.readability.test.DatastoreRule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ContextConfig.class)
public class MetricServiceTest {
	@Rule
	public DatastoreRule datastoreRule = new DatastoreRule();

	@Autowired
	TextService textService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TextMetricService textMetricService;
	
	@Autowired
	ReadabilityService readabilityService;

	Text text;
	
	User user;

	@Before
	public void setup() {
		user = new User();
		
		user.setId("o8m0zyetzixyml6g8buhv8vydpag3i");
		
		userService.save(user);
		
		this.text = new Text(user, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		
		textService.save(text);
		
		Object o = textService.findAllByParentKey(user.getId());
	}

	@Test
	public void testNonNull() {
		assertNotNull(textService);
		assertNotNull(textMetricService);
	}

	@Test
	public void testSave() throws JsonGenerationException, JsonMappingException, IOException {
		Collection<TextMetric> metrics = readabilityService.getMetrics(text);
		
		textMetricService.save(metrics);
		
		metrics = new ArrayList<TextMetric>();
		
		for (TextMetric m : textMetricService.findAllByParentKey(text.getId()))
			metrics.add(m);
		
		System.out.println(new ObjectMapper().writeValueAsString(metrics));
	}

}
