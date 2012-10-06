package com.ipeirotis.rest.readability;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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

	//datastore for storing text.
	private static final String TEXT_STORE = "READABILITY_TEXT_STORE";
	private static final String TEXT_STORE_TEXT = "text";
	private static final String TEXT_STORE_TIMESTAMP = "timestamp";

	//datastore for storing calculated metrics.
	private static final String METRICS_STORE = "READABILITY_METRICS";
	private static final String METRICS_STORE_TIMESTAMP = "timestamp";
	private static final String METRICS_STORE_ARI = "ari";
	private static final String METRICS_STORE_SMOG = "smog"; 
	private static final String METRICS_STORE_FLESCH_READING = "flesch_reading";
	private static final String METRICS_STORE_FLESCH_KINCAID = "flesch_kincaid";
	private static final String METRICS_STORE_GUNNING_FOG = "gunning_fog";
	private static final String METRICS_STORE_COLEMAN_LIAU = "coleman_liau";
	private static final String METRICS_STORE_SMOG_INDEX = "smog_index";
	/**
	 * Test method.
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayPlainTextHello() {
	    return createHTML("Hello, this is Readability REST project");
	}
	

	/**
	 * Get text stored from database, specified by key.
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/getText/{id}")
	public Response getText(@PathParam("id") Long id) {
		DatastoreService dataStore =
                DatastoreServiceFactory.getDatastoreService();
		Key textStoreKey = KeyFactory.createKey(TEXT_STORE, id);
		try {
			StringBuffer buffer = new StringBuffer();
			Entity textEntity = dataStore.get(textStoreKey);
			Date d = new Date((Long) textEntity.getProperty(TEXT_STORE_TIMESTAMP));
			buffer.append("Timestamp: "+d.toString());
			buffer.append("<br>");
			buffer.append("Text: ");
			buffer.append((String) textEntity.getProperty(TEXT_STORE_TEXT));
			return Response.status(Response.Status.OK).entity(
					createHTML(buffer.toString())).build();
		} catch (EntityNotFoundException e) {
			String content = createHTML("Text not found with id: "+id);
			return Response.status(Response.Status.NOT_FOUND).entity(content).build();
		}		
	}
	
	/**
	 * Insert new text to datastore and return datastore key.
	 * @param text
	 * @return key as long.
	 */
	@POST
	@Consumes("text/plain")
	@Produces(MediaType.TEXT_HTML)
    @Path("/insertText")
	public Response insertText(String text) {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Entity textEntity = new Entity(TEXT_STORE);
		textEntity.setProperty(TEXT_STORE_TEXT, text);
		textEntity.setProperty(TEXT_STORE_TIMESTAMP, System.currentTimeMillis());
		Key newKey = dataStore.put(textEntity);
		return Response.status(Response.Status.OK).
				entity(createHTML("ID="+newKey.getId())).build();
	}
	
	/**
	 * Update text in the datastore.
	 * @param text
	 * @return
	 */
	@PUT
	@Consumes("text/plain")
	@Produces(MediaType.TEXT_HTML)
    @Path("/updateText/{id}")
	public Response updateText(@PathParam("id") Long textId, String text) {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Key textStoreKey = KeyFactory.createKey(TEXT_STORE, textId);
		Entity textEntity = new Entity(textStoreKey);
		textEntity.setProperty(TEXT_STORE_TEXT, text);
		textEntity.setProperty(TEXT_STORE_TIMESTAMP, System.currentTimeMillis());
		dataStore.put(textEntity);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Get all metrics for stored text in the database.
	 * @param textId
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
    @Path("/getMetrics/{id}")
	public Response getMetrics(@PathParam("id") Long textId) {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		try {
			//get text from text store
			Key textStoreKey = KeyFactory.createKey(TEXT_STORE, textId);
			Entity textEntity = dataStore.get(textStoreKey);
			String text = (String) textEntity.getProperty(TEXT_STORE_TEXT);
			Long timestamp = (Long) textEntity.getProperty(TEXT_STORE_TIMESTAMP);			
			Date textStoreDate = new Date(timestamp);

			//get possible metrics from metric store.
			Key metricStoreKey = KeyFactory.createKey(METRICS_STORE, textId);
			Entity metricEntity = null;
			Date metricDate = null;
			try {
				metricEntity = dataStore.get(metricStoreKey);				
				metricDate = new Date((Long) metricEntity.getProperty(METRICS_STORE_TIMESTAMP));
			} catch (EntityNotFoundException e) {
				//not found.
			}	
			String result = null;
			//text is new or metrics are outdated.
			BagOfReadabilityObjects metrics = null;
			if(metricEntity == null || metricDate.before(textStoreDate)) {
				metrics = new Readability(text).getMetrics();				
				//store metrics
				metricEntity = new Entity(metricStoreKey);				
				metricEntity.setProperty(METRICS_STORE_SMOG, metrics.getSMOG());
				metricEntity.setProperty(METRICS_STORE_FLESCH_READING, metrics.getFleschReading());
				metricEntity.setProperty(METRICS_STORE_FLESCH_KINCAID, metrics.getFleschKincaid());
				metricEntity.setProperty(METRICS_STORE_ARI, metrics.getARI());
				metricEntity.setProperty(METRICS_STORE_GUNNING_FOG, metrics.getGunningFog());
				metricEntity.setProperty(METRICS_STORE_COLEMAN_LIAU, metrics.getColemanLiau());
				metricEntity.setProperty(METRICS_STORE_SMOG_INDEX, metrics.getSMOGIndex());
				
				metricEntity.setProperty(METRICS_STORE_TIMESTAMP, 
						(Long) System.currentTimeMillis());
				dataStore.put(metricEntity);
				metricDate = new Date((Long) metricEntity.getProperty(METRICS_STORE_TIMESTAMP));
			}
			else {//return stored metrics.
				metrics = new BagOfReadabilityObjects();
				metrics.setSMOG((Double) metricEntity.getProperty(METRICS_STORE_SMOG));
				metrics.setFleschReading((Double) 
						metricEntity.getProperty(METRICS_STORE_FLESCH_READING));
				metrics.setFleschKincaid((Double) 
						metricEntity.getProperty(METRICS_STORE_FLESCH_KINCAID));
				metrics.setARI((Double) metricEntity.getProperty(METRICS_STORE_ARI));
				metrics.setGunningFog((Double) 
						metricEntity.getProperty(METRICS_STORE_GUNNING_FOG));
				metrics.setColemanLiau((Double) 
						metricEntity.getProperty(METRICS_STORE_COLEMAN_LIAU));
				metrics.setSMOGIndex((Double) 
						metricEntity.getProperty(METRICS_STORE_SMOG_INDEX));
			}
			result = createMetricTable(metricDate, metrics);
			return Response.status(Status.OK).entity(result).build();
		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND).
				entity(createHTML("Text not found with id: "+textId)).build();
		}		
	}
	
	/**
	 * Create html table with metrics.
	 * @param date
	 * @param metrics
	 * @return
	 */
	private String createMetricTable(Date date, BagOfReadabilityObjects metrics) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<table border='1'>");
		buffer.append("<tr>");
		buffer.append("<td>Metrics timestamp</td><td>");			
		buffer.append(date.toString());
		buffer.append("</td></tr>");			
		buffer.append("<tr>");
		buffer.append("<td>ARI</td><td>");			
		buffer.append(metrics.getARI());
		buffer.append("</td></tr>");			
		
		buffer.append("<tr>");
		buffer.append("<td>Coleman Liau</td><td>");			
		buffer.append(metrics.getColemanLiau());
		buffer.append("</td></tr>");			

		buffer.append("<tr>");
		buffer.append("<td>Flesch Kincaid</td><td>");			
		buffer.append(metrics.getFleschKincaid());
		buffer.append("</td></tr>");			

		buffer.append("<tr>");
		buffer.append("<td>Flesch Reading</td><td>");			
		buffer.append(metrics.getFleschReading());
		buffer.append("</td></tr>");
		
		buffer.append("<tr>");
		buffer.append("<td>Cunning Frog</td><td>");			
		buffer.append(metrics.getGunningFog());
		buffer.append("</td></tr>");			
		
		buffer.append("<tr>");
		buffer.append("<td>SMOG</td><td>");			
		buffer.append(metrics.getSMOG());
		buffer.append("</td></tr>");			

		buffer.append("<tr>");
		buffer.append("<td>SMOG Index</td><td>");			
		buffer.append(metrics.getSMOGIndex());
		buffer.append("</td></tr>");			
		buffer.append("</table>");
		return createHTML(buffer.toString());
	}
	
	/**
	 * Gets individual metric value for stored text.
	 * @param textId
	 * @param type
	 * @return value of metric as double
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
    @Path("/getOneMetric/{id}/{metric_type}")
	public Response getMetric(@PathParam("id") Long textId, 
			@PathParam("metric_type") String typeString) {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Double value = null;
		MetricType type = null;
		try {
			Key textStoreKey = KeyFactory.createKey(TEXT_STORE, textId);
			Entity textEntity = dataStore.get(textStoreKey);
			String text = (String) textEntity.getProperty(TEXT_STORE_TEXT);
			Readability read = new Readability(text);
			type = MetricType.fromString(typeString);
			if(type == null) {				
				StringBuffer buffer = new StringBuffer();
				buffer.append("Unknown metric type: ");
				buffer.append(typeString);
				buffer.append("<BR>");
				buffer.append("Available types are (in lower- or uppercase): ");
				buffer.append("SMOG, FLESCH_READING, FLESCH_KINCAID, ARI, ");
				buffer.append("GUNNING_FOG, COLEMAN_LIAU and SMOG_INDEX");
				buffer.append("<BR>");
				return Response.status(Status.NOT_FOUND).
						entity(createHTML(buffer.toString())).build();				
			}
			else if(type.equals(MetricType.ARI)) {
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
			return Response.status(Status.NOT_FOUND).
					entity(createHTML("Text not found with id: "+textId)).build();
		}		
		return Response.status(Status.OK).
				entity(createHTML(type.toString()+"="+value)).build();
	}
	
	/**
	 * Deletes text from datastore.
	 * @param textId
	 */
    @DELETE
    @Produces(MediaType.TEXT_HTML)
    @Path("/deleteText/{id}")
    public Response deleteText(@PathParam("id") Long textId) {
    	DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
    	Key textStoreKey = KeyFactory.createKey(TEXT_STORE, textId);
    	try {
			dataStore.get(textStoreKey);
	        dataStore.delete(textStoreKey);   		
		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND).
					entity(createHTML("Text not found with id: "+textId)).build();
		}
    	return Response.status(Status.OK).build();    
     }
    
    /**
     * Helper method to create HTML.
     * @param content
     * @return
     */
    private String createHTML(String content) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("<HTML>");
    	buffer.append("<BODY>");
    	buffer.append(content);
    	buffer.append("</BODY>");
    	buffer.append("</HTML>");
    	return buffer.toString();
    }
}
