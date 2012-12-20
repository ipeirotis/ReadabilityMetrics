package com.ipeirotis.readability.web.v1;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
@Path("/v1")
public class V1Resource extends BaseResource {
	@GET
	@Produces("text/plain")
	@Path("/info")
	public String info() {
		return "Hello, World!";
	}

	@GET
	@Path("/debug")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> debug() {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		
		for (String k : this.requestHeaders.getRequestHeaders().keySet()) {
			result.put(k, requestHeaders.getRequestHeader(k));
		}
		
		return result;
	}
}
