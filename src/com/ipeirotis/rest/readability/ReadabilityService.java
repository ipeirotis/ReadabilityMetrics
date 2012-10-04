package com.ipeirotis.rest.readability;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


import com.ipeirotis.readability.BagOfReadabilityObjects;
import com.ipeirotis.readability.Readability;

/**
 * Restful services for Readability.
 * 
 * @author TimoT
 *
 */
@Path("/")
public class ReadabilityService {

	private static final String TEXT_STORE = "READABILITY_TEXT_STORE";
	private static final String TEXT_STORE_TEXT_PROPERTY = "text";
	
	/**
	 * Test method.
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
	    return "Hello, this is Readability REST project.";
	}
	

	/**
	 * Get text stored from database, specified by key.
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/getText/{id}")
	public String getText(@PathParam("id") Long id) {
		DatastoreService dataStore =
                DatastoreServiceFactory.getDatastoreService();
		String text = null;
		Key textStoreKey = KeyFactory.createKey(TEXT_STORE, id);
		try {
			Entity textEntity = dataStore.get(textStoreKey);
			text = (String) textEntity.getProperty(TEXT_STORE_TEXT_PROPERTY);
		} catch (EntityNotFoundException e) {
			return "No text found with id: "+id;
		}		
		return text;
	}
	
	/**
	 * Insert new text to datastore and return datastore key.
	 * @param text
	 * @return key as long.
	 */
	@POST
	@Consumes("text/plain")
	@Produces(MediaType.TEXT_PLAIN)
    @Path("/insertText")
	public Long insertText(String text) {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Entity textEntity = new Entity(TEXT_STORE);
		textEntity.setProperty(TEXT_STORE_TEXT_PROPERTY, text);
		Key newKey = dataStore.put(textEntity);
		return newKey.getId();
	}
	
	/**
	 * Update text in the datastore.
	 * @param text
	 * @return
	 */
	@PUT
	@Consumes("text/plain")
	@Produces(MediaType.TEXT_PLAIN)
    @Path("/updateText/{id}")
	public Boolean updateText(@PathParam("id") Long textId, String text) {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Key textStoreKey = KeyFactory.createKey(TEXT_STORE, textId);
		Entity textEntity = new Entity(textStoreKey);
		textEntity.setProperty(TEXT_STORE_TEXT_PROPERTY, text);
		dataStore.put(textEntity);
		return true;
	}
	
	/**
	 * Get all metrics for stored text in the database.
	 * @param textId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
    @Path("/getMetrics/{id}")
	public BagOfReadabilityObjects getMetrics(@PathParam("id") Long textId) {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		try {
			Key textStoreKey = KeyFactory.createKey(TEXT_STORE, textId);
			Entity textEntity = dataStore.get(textStoreKey);
			String text = (String) textEntity.getProperty(TEXT_STORE_TEXT_PROPERTY);
			return new Readability(text).getMetrics();
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Gets individual metric value for stored text.
	 * @param textId
	 * @param type
	 * @return value of metric as double
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
    @Path("/getOneMetric/{id}/{metric_type}")
	public Double getMetric(@PathParam("id") Long textId, 
			@PathParam("metric_type") MetricType type) {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Double value = null;
		
		try {
			Key textStoreKey = KeyFactory.createKey(TEXT_STORE, textId);
			Entity textEntity = dataStore.get(textStoreKey);
			String text = (String) textEntity.getProperty(TEXT_STORE_TEXT_PROPERTY);
			Readability read = new Readability(text);
			
			if(type.equals(MetricType.ARI)) {
				value = read.getARI();
			} 
			if(type.equals(MetricType.COLEMAN_LIAU)) {
				value = read.getColemanLiau();
			} 
			else if(type.equals(MetricType.FLESCH_KINCAID)) {
				value = read.getFleschKincaidGradeLevel();
			} 
			else if(type.equals(MetricType.FLESCH_READING)) {
				value = read.getFleschReadingEase();
			}
			else if(type.equals(MetricType.GUNNING_FOG)) {
				value = read.getGunningFog();
			} 
			else if(type.equals(MetricType.SMOG)) {
				value = read.getSMOG();
			} 			
			else if(type.equals(MetricType.SMOG_INDEX)) {
				value = read.getSMOGIndex();
			}
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return value;
	}
	
	/**
	 * Deletes text from datastore.
	 * @param textId
	 */
    @DELETE
    @Path("/deleteText/{id}")
    public void deleteText(@PathParam("id") Long textId) {
    	DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
    	Key textStoreKey = KeyFactory.createKey(TEXT_STORE, textId);
    	dataStore.delete(textStoreKey);
    }
}
