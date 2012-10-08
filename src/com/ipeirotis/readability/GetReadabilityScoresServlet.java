package com.ipeirotis.readability;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ipeirotis.readability.entities.MetricType;

@SuppressWarnings("serial")
public class GetReadabilityScoresServlet extends HttpServlet {

	private HttpServletResponse r;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) {

		try {
			this.r = resp;

			String text = req.getParameter("text");
			if (text == null || text.trim().length() == 0) {
				r.setStatus(500);
				print("No text uploaded");
				return;
			}

			Readability read = new Readability(text);
 
			String output = req.getParameter("format");
			if (output == null) {
				resp.setContentType("text/plain");
			} else if (output.equals("txt")) {
				resp.setContentType("text/plain");
			} else if (output.equals("json")) {
				resp.setContentType("application/json");
				Gson gson = new Gson();
				String json = gson.toJson(read.getMetrics());
				print(json);
				return;
			} else if (output.equals("xml")) {
				resp.setContentType("text/xml");
				return;
			}

			String metric = req.getParameter("metric");
			if (metric == null) {
				r.setStatus(500);
				print("No metric parameter defined and output is set to txt.");
				return;
			}
			Double v;
			try {
				v = read.getMetric(MetricType.fromString(metric));
			} catch (IllegalArgumentException e) {
				r.setStatus(500);
				StringBuffer sb = new StringBuffer("Invalid value for the metric parameter. It should be one of: ");
				for (MetricType m : MetricType.values()) {
					sb.append(m.name() +", ");
				}
				sb.append(" and any other value is invalid.");
				print( sb.toString() );
				return;
			}
			print(v.toString());
			

		} catch (com.google.apphosting.api.DeadlineExceededException e) {
			print("Reached execution time limit.");
			return;
		}

	}

	private void print(String message) {
		try {
			r.getWriter().println(message);
			r.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
