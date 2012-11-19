package com.ipeirotis.readability.web.v1;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.ipeirotis.readability.model.Text;
import com.ipeirotis.readability.model.TextMetric;
import com.ipeirotis.readability.service.ReadabilityService;
import com.ipeirotis.readability.service.TextMetricService;
import com.ipeirotis.readability.service.TextService;

@Component
@Scope("request")
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Path("/v1/text")
public class TextResource extends BaseResource {
	@Autowired
	TextService textService;

	@Autowired
	TextMetricService textMetricService;

	@Autowired
	ReadabilityService readabilityService;
	
	@OPTIONS
	@Path(".*")
	public Response getOptionsForUpdateText() throws Exception {
		return Response
				.ok()
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods",
						"GET, POST, DELETE, OPTIONS")
				.header("Access-Control-Allow-Headers", "x-requested-with")
				.build();
	}

	@POST
	public Text updateText(String contents) throws Exception {
		@SuppressWarnings("unused")
		Text o = null, t = null;

		if (contents.startsWith("body=")) {
			contents = contents.substring("body=".length());
		}
		
		t = new Text(user, contents);

		if (null == (o = textService.findFirstByPrimaryKey(t.getId()))) {
			textService.save(t);
			textMetricService.save(readabilityService.getMetrics(t));
		}

		return t;
	}

	@Path("/{id : [^/]+}")
	@GET
	public Text findById(@PathParam("id") String id) throws Exception {
		Text t = textService.findFirstByPrimaryKey(id);

		if (null == t || (!t.getOwner().equals(user.getId())))
			throw new WebApplicationException(404);

		return t;
	}

	@Path("/{id : [^/]+}/metrics")
	@GET
	public Map<String, Object> findMetricsById(@PathParam("id") String id)
			throws Exception {
		Text t = findById(id);
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		result.put("id", t.getId());

		Map<String, BigDecimal> metrics = new LinkedHashMap<String, BigDecimal>();

		for (TextMetric m : textMetricService.findAllByParentKey(t.getId())) {
			metrics.put(m.getMetricType(), m.getValue());
		}

		result.put("metrics", metrics);

		return result;
	}

	@Path("/{id : [^/]+}/metrics/{metricType : \\w+}")
	@GET
	public Map<String, Object> findNamedMetricsById(@PathParam("id") String id,
			@PathParam("metricType") String metricType) throws Exception {
		Text t = findById(id);
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		result.put("id", t.getId());

		Map<String, BigDecimal> metrics = new LinkedHashMap<String, BigDecimal>();

		TextMetric m = textMetricService.findFirstByPrimaryKey(String.format("%s:%s", t.getId(), metricType));

		metrics.put(m.getMetricType(), m.getValue());

		result.put("metrics", metrics);

		return result;
	}

	@Path("/{id : [^/]+}")
	@DELETE
	public Text deleteById(@PathParam("id") String id) throws Exception {
		Text t = textService.findFirstByPrimaryKey(id);

		if (null == t || (t.getOwner() != user.getId()))
			throw new WebApplicationException(404);

		textService.delete(t);

		return t;
	}

	@GET
	public Collection<Text> findAll() {
		return Lists.newArrayList(textService.findAllByParentKey(user.getId()));
	}
}
