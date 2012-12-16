package com.ipeirotis.readability.web.v1;

import static com.jayway.restassured.RestAssured.basePath;
import static com.jayway.restassured.RestAssured.given;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;

public class MashapeTextResourceIT {
	private static final String MASHAPE_AUTH_HEADER_VALUE = "bWpjNGM1Y2p1bXpmaTJqc3Bha2t3NHpieW1sanFlOjJlNDg2ODNjYjQxNThkYjc1MzNiZTFkNzAxYTMwMzMwZGQyMWJjMzU=";

	private static final String MASHAPE_PUBLIC_USER_ID = "mjc4c5cjumzfi2jspakkw4zbymljqe";

	private static final String PHIL_SPACE = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

	private RequestSpecification requestSpec;

	@Before
	public void before() throws Exception {
		basePath = "https://readabilitymetrics.p.mashape.com";

		this.requestSpec = new RequestSpecBuilder().addHeader(
				"X-Mashape-Authorization", MASHAPE_AUTH_HEADER_VALUE).build();
	}

	@Test
	public void testSaveText() throws Exception {
		given().spec(requestSpec)
				.//
				body(PHIL_SPACE)
				.//
				expect()
				.statusCode(200)
				.body(StringContains
						.containsString(MASHAPE_PUBLIC_USER_ID
								+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18"))
				.when().post("/text");
	}

	@Test
	public void testFindAll() throws Exception {
		testSaveText();
		given().spec(requestSpec)
				.//
				expect()
				.statusCode(200)
				.body(StringContains
						.containsString(MASHAPE_PUBLIC_USER_ID
								+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18"))
				.when().get("/text");
	}

	@Test
	public void testGetNamed() throws Exception {
		testSaveText();
		given().spec(requestSpec)
				.//
				expect()
				.statusCode(200)
				.body(StringContains
						.containsString(MASHAPE_PUBLIC_USER_ID
								+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18"))
				.when()
				.get("/text/"
						+ MASHAPE_PUBLIC_USER_ID
						+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18");
	}

	@Test
	public void testGetMetrics() throws Exception {
		testSaveText();
		given().spec(requestSpec)
				.//
				expect()
				.statusCode(200)
				.body(StringContains
						.containsString(MASHAPE_PUBLIC_USER_ID
								+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18"))
				.body(StringContains.containsString("370.00"))
				.when()
				.get("/text/"
						+ MASHAPE_PUBLIC_USER_ID
						+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18/metrics");
	}

	@Test
	public void testGetNamedMetrics() throws Exception {
		testSaveText();
		given().spec(requestSpec)
				.//
				expect()
				.statusCode(200)
				.body(StringContains.containsString("370.00"))
				.when()
				.get("/text/"
						+ MASHAPE_PUBLIC_USER_ID
						+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18/metrics/CHARACTERS");
	}

	@Test
	public void testDelete() throws Exception {
		testSaveText();

		given().spec(requestSpec)
				.//
				expect()
				.statusCode(200)
				.body(StringContains
						.containsString(MASHAPE_PUBLIC_USER_ID
								+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18"))
				.when()
				.delete("/text/"
						+ MASHAPE_PUBLIC_USER_ID
						+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18");

		given().spec(requestSpec)
				.//
				expect()
				.statusCode(404)
				.when()
				.get("/text/"
						+ MASHAPE_PUBLIC_USER_ID
						+ ":2c7c3d5f244f1a40069a32224215e0cf9b42485c99d80f357d76f006359c7a18");
	}

}
