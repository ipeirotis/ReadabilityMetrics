package com.ipeirotis.readability.web.v1;

import java.math.BigDecimal;
import java.net.URLDecoder;
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

/**
 * Represents the Text Manipulation Resource
 * 
 * @author aldrin
 * 
 */
@Component
@Scope("request")
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Path("/readability/api/v1/text")
public class TextResource extends BaseResource {
	@Autowired
	TextService textService;

	@Autowired
	TextMetricService textMetricService;

	@Autowired
	ReadabilityService readabilityService;

	/**
	 * Handler needed for CORS support
	 * 
	 * @return CORS Response
	 */
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

	/**
	 * Updates (read: stores) a Text
	 * 
	 * @param contents
	 *            Body Context (Form Method)
	 * @return Created Text Object
	 */
	@POST
	public Text updateText(String contents) throws Exception {
		@SuppressWarnings("unused")
		Text o = null, t = null;

		/*
		 * In case we're receiving it thru mashape, we need to unescape.
		 */
		if (contents.startsWith("body="))
			contents = URLDecoder.decode(contents.substring("body=".length()), "UTF-8");

		t = new Text(user, contents);

		if (null == (o = textService.findFirstByPrimaryKey(t.getId()))) {
			textService.save(t);
			textMetricService.save(readabilityService.getMetrics(t));
		}

		return t;
	}

	/**
	 * Given an Id, returns the Text
	 * 
	 * @param id
	 *            id, in the form &lt;userid&gt:&lt;sha1hash&gt;
	 * 
	 * @return Text Object
	 */
	@Path("/{id : [^/]+}")
	@GET
	public Text findById(@PathParam("id") String id) throws Exception {
		String lookupId = id;

		/*
		 * Accepts Partial Id
		 */
		if (-1 == lookupId.indexOf(':')) {
			lookupId = user.getId() + ":" + lookupId;
		} else {
			/*
			 * Otherwise, forces the id to belong to this user
			 */
			if (! lookupId.startsWith(user.getId() + ":")) {
				lookupId = user.getId() + ":" + lookupId.substring(1 + lookupId.indexOf(':'));
			}
		}

		Text t = textService.findFirstByPrimaryKey(lookupId);

		if (null == t || (!t.getOwner().equals(user.getId())))
			throw new WebApplicationException(404);

		return t;
	}

	/**
	 * Returns the text metrics for given id
	 * 
	 * @param id
	 *            id, in the form &lt;userid&gt:&lt;sha1hash&gt;
	 * @return Json Array of Object Id + Metrics
	 */
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

	/**
	 * Returns a Named Metric
	 * 
	 * @param id
	 *            id, in the form &lt;userid&gt:&lt;sha1hash&gt;
	 * @param metricType
	 *            metric type (ARI, CHARACTERS, SYLLABLES, ...)
	 * @return Named Metric (just like the example in findMetricsById
	 */
	@Path("/{id : [^/]+}/metrics/{metricType : \\w+}")
	@GET
	public Map<String, Object> findNamedMetricsById(@PathParam("id") String id,
			@PathParam("metricType") String metricType) throws Exception {
		Text t = findById(id);
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		result.put("id", t.getId());

		Map<String, BigDecimal> metrics = new LinkedHashMap<String, BigDecimal>();

		TextMetric m = textMetricService.findFirstByPrimaryKey(String.format(
				"%s:%s", t.getId(), metricType));

		metrics.put(m.getMetricType(), m.getValue());

		result.put("metrics", metrics);

		return result;
	}

	/**
	 * Deletes a Text
	 * 
	 * @param id
	 *            id, in the form &lt;userid&gt:&lt;sha1hash&gt;
	 * @return Text Data, prior to removal
	 */
	@Path("/{id : [^/]+}")
	@DELETE
	public Text deleteById(@PathParam("id") String id) throws Exception {
		Text t = findById(id);

		textService.delete(t);

		return t;
	}

	/**
	 * Returns all texts which belongs to this user
	 * 
	 * @return array of Text Objects
	 */
	@GET
	public Collection<Text> findAll() {
		return Lists.newArrayList(textService.findAllByParentKey(user.getId()));
	}
}
