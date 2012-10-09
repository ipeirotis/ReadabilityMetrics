package com.ipeirotis.readability.rest;

import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.ipeirotis.readability.BagOfReadabilityObjects;
import com.ipeirotis.readability.Readability;
import com.ipeirotis.readability.entities.MetricType;
import com.ipeirotis.readability.entities.MetricsEntry;
import com.ipeirotis.readability.entities.TextEntry;


/**
 * Restful services for Readability.
 * 
 * @author TimoT
 * 
 */
@Path("/")
public class ReadabilityService {




	/**
	 * Test method.
	 * 
	 * @return
	 */
	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello, this is Readability REST project";
	}

	/**
	 * Test method.
	 * 
	 * @return
	 */
	@GET
	@Path("test")
	@Produces(MediaType.TEXT_HTML)
	public String sayHTMLTextHello() {
		String title = "Hello World - This is the Readability Service!";
		String content = "Hello, this is Readability REST project";
		return createHTML(title,content);
	}
	
	/**
	 * Test method.
	 * 
	 * @return
	 */
	@GET
	@Path("test")
	@Produces(MediaType.TEXT_XML)
	public String sayXMLTextHello() {
		return "<?xml version=\"1.0\"?>"+"<hello>Hello, this is Readability REST project</hello>";
	}

	
	/**
	 * Insert new text to datastore and return datastore key.
	 * 
	 * @param text
	 * @return key as long.
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("text")
	public Response createText(@QueryParam("message") String text) {

		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Entity textEntry = new Entity(TextEntry.TEXT_STORE);
		Text txt = new Text(text);
		
		textEntry.setProperty(TextEntry.TEXT_STORE_TEXT, txt);
		textEntry.setProperty(TextEntry.TEXT_STORE_TIMESTAMP, System.currentTimeMillis());
		Key key = dataStore.put(textEntry);
		String id = String.valueOf(key.getId());
		return Response.status(Response.Status.OK).entity(id).build();
	}

	
	/**
	 * Returns text stored in the datastore
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("text/{id}")
	public Response readText(@PathParam("id") Long id) {

		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Key textStoreKey = KeyFactory.createKey(TextEntry.TEXT_STORE, id);
		try {
			StringBuffer sb = new StringBuffer();
			Entity textEntity = dataStore.get(textStoreKey);
			Date d = new Date((Long) textEntity.getProperty(TextEntry.TEXT_STORE_TIMESTAMP));
			Text text = (Text) textEntity.getProperty(TextEntry.TEXT_STORE_TEXT);
			sb.append("Timestamp: " + d.toString() + "\n");
			sb.append("Text: " + text.getValue());
			return Response.status(Response.Status.OK).entity(sb.toString()).build();
		} catch (EntityNotFoundException e) {
			String content = ("Text not found. ID: " + id);
			return Response.status(Response.Status.NOT_FOUND).entity(content).build();
		}
	}


	/**
	 * Update text in the datastore.
	 * 
	 * @param text
	 * @return
	 */
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Path("text/{id}")
	public Response updateText(@PathParam("id") Long id, @FormParam("message") Text text) {

		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Key textStoreKey = KeyFactory.createKey(TextEntry.TEXT_STORE, id);
		Entity textEntity = new Entity(textStoreKey);
		textEntity.setProperty(TextEntry.TEXT_STORE_TEXT, text);
		textEntity.setProperty(TextEntry.TEXT_STORE_TIMESTAMP, System.currentTimeMillis());
		Key key = dataStore.put(textEntity);
		String sid = String.valueOf(key.getId());
		return Response.status(Response.Status.OK).entity(sid).build();
	}

	/**
	 * Deletes text from datastore.
	 * 
	 * @param textId
	 */
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("text/{id}")
	public Response deleteText(@PathParam("id") Long textId) {

		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey(TextEntry.TEXT_STORE, textId);
		// First check if the id exists
		try {
			dataStore.get(key);
		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND).entity(("Text not found with id: " + textId)).build();
		}
		// And if it does, delete it.
		dataStore.delete(key);
		return Response.status(Status.OK).build();
	}
	
	

	/**
	 * Get metrics as JSON object.
	 * 
	 * @param textId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("text/{id}/getMetrics")
	public BagOfReadabilityObjects getJsonMetrics(@PathParam("id") Long textId) {

		BagOfReadabilityObjects metrics = getMetrics(textId);
		if (metrics != null) {
			return metrics;
		} else {
			return new BagOfReadabilityObjects();// return empty object, null is not allowed.
		}
	}

	/**
	 * Get metrics either from datastore or calculate metrics for new or
	 * update texts.
	 * 
	 * @param id
	 * @return
	 */
	private BagOfReadabilityObjects getMetrics(Long id) {

		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

			// get text from text store
			Key tKey = KeyFactory.createKey(TextEntry.TEXT_STORE, id);
			Entity textEntity;
			try {
				 textEntity = dataStore.get(tKey);
			} catch (EntityNotFoundException e) {
				// No entry for this textId
				return null;
			} 

			// get metrics from metric store.
			Key mKey = KeyFactory.createKey(MetricsEntry.METRICS_STORE, id);
			Entity metricEntity;
			try {
				metricEntity = dataStore.get(mKey);
			} catch (EntityNotFoundException e) {
				// no such entry, so we create it and save it
				metricEntity = createMetricEntity(id, textEntity);
				dataStore.put(metricEntity);
			} 
			
		  // We check if the text has been updated after the last time the metrics were updated
			Long textTimestamp = (Long) textEntity.getProperty(TextEntry.TEXT_STORE_TIMESTAMP);
			Long metricTimestamp = (Long) metricEntity.getProperty(MetricsEntry.METRICS_STORE_TIMESTAMP);
			if (metricTimestamp<textTimestamp) {
				// Yes, the text has been updated since the last time we computed the metrics
				metricEntity = createMetricEntity(id, textEntity);
				dataStore.put(metricEntity);
			} 
			
			BagOfReadabilityObjects metrics = createResponseFromDatastore(metricEntity);
			return metrics;

	}

	/**
	 * @param textEntity
	 * @param mKey
	 * @return
	 */
	private Entity createMetricEntity(Long textId, Entity textEntity) {

		Entity metricEntity;
		Key mKey = KeyFactory.createKey(MetricsEntry.METRICS_STORE, textId);
		metricEntity = new Entity(mKey);
		
		Text text = (Text) textEntity.getProperty(TextEntry.TEXT_STORE_TEXT);
		Readability r = new Readability(text.getValue());
		for (MetricType metric : MetricType.values()) {
			metricEntity.setProperty(metric.name(), r.getMetric(metric));
		}
		metricEntity.setProperty(MetricsEntry.METRICS_STORE_TIMESTAMP, System.currentTimeMillis());
		return metricEntity;
	}

	/**
	 * @param metricEntity
	 * @return
	 */
	private BagOfReadabilityObjects createResponseFromDatastore(Entity metricEntity) {

		BagOfReadabilityObjects metrics = new BagOfReadabilityObjects();
		for (String property: metricEntity.getProperties().keySet()) {
			MetricType t;
			try {
				t = MetricType.fromString(property);
			} catch (IllegalArgumentException e) {
				// This property is not part of the supported Readability Metrics
				continue;
			}
			Double value = (Double) metricEntity.getProperty(property);
			metrics.setMetric(t, value);
		}
		return metrics;
	}


	

	/**
	 * Helper method to create HTML.
	 * 
	 * @param content
	 * @return
	 */
	 private String createHTML(String title, String content) {
	  StringBuffer buffer = new StringBuffer();
	  buffer.append("<HTML>");
		buffer.append("<TITLE>");
		buffer.append(title);
		buffer.append("</TITLE>");
	  buffer.append("<BODY>");
	  buffer.append(content);
	  buffer.append("</BODY>");
	  buffer.append("</HTML>");
	  return buffer.toString();
	  }
}
