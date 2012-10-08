package com.ipeirotis.readability.test;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class WebServiceTest {

	private static final String base = "http://ipeirotis.appspot.com/readability";
	
	@Test
	public void test() {

		System.out.println("Readability, Test call, accept text/plain .... \n");
		Client client = Client.create();
		
		WebResource webResource = client.resource(base + "/test");
	 	ClientResponse response = webResource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		
	 	if (response.getStatus() != 200) {
		   fail("Failed : HTTP error code : " + response.getStatus());
		}
	 	
	 	String output = response.getEntity(String.class);
	  
		System.out.println("Output from Server .... \n");
		System.out.println(output);
	 	
	}
	
	@Test
	public void testXML() {

		System.out.println("Readability, Test call, accept text/xml .... \n");
		Client client = Client.create();
		
		WebResource webResource = client.resource(base + "/test");
	 	ClientResponse response = webResource.accept(MediaType.TEXT_XML).get(ClientResponse.class);
		
	 	if (response.getStatus() != 200) {
		   fail("Failed : HTTP error code : " + response.getStatus());
		}
	 	
	 	String output = response.getEntity(String.class);
	  
		System.out.println("Output from Server .... \n");
		System.out.println(output);
	 	
	}
	
	@Test
	public void testPOST() {

		System.out.println("Readability, POST .... \n");
		System.out.println("Text: "+Texts.longText);
		Client client = Client.create();
		
		WebResource webResource = client.resource(base + "/text");
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	  queryParams.add("message", Texts.longText);
	 	ClientResponse response = webResource
	 			.queryParams(queryParams)
	 			.post(ClientResponse.class, Texts.longText);
		
	 	if (response.getStatus() != 200) {
		   fail("Failed : HTTP error code : " + response.getStatus() +"\n" + response.getEntity(String.class));
		}
	 	
	 	String output = response.getEntity(String.class);
	  System.out.println("Output from Server .... ");
		System.out.println(output);
	 	
	}

}
