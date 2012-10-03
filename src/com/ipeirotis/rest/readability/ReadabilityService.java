package com.ipeirotis.rest.readability;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.ipeirotis.readability.BagOfReadabilityObjects;

/**
 * Restful services for Readability.
 * 
 * @author TimoT
 *
 */
public class ReadabilityService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
	    return "Hello, this is Readability project.";
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/getText")
	public String getText(Integer id) {
		return null;
	}
	
	@POST
    @Consumes("application/xml")
    @Path("/insertText")
	public Boolean insertText(Text text) {
		return null;
	}
	
	@PUT
    @Consumes("application/xml")
    @Path("/updateText")
	public Boolean updateText(Text text) {
		return null;
	}
	
	@GET
    @Consumes("application/xml")
    @Path("/getMetrics")
	public BagOfReadabilityObjects getMetrics(Integer id) {
		return null;
	}
	
	@GET
    @Consumes("application/xml")
    @Path("/getOneMetric")
	public BagOfReadabilityObjects getMetric(Integer id, MetricType type) {
		return null;
	}
	
	
    @DELETE
    @Produces("application/xml")
    @Path("/deleteText")
    public Boolean deleteText(Integer id) {
    	return null;
    }
}
