package com.ipeirotis.readability;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.*;

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
			if (metric.equals("SMOGIndex")) {
				print("" + read.getSMOGIndex());
			} else if (metric.equals("SMOG")) {
				print("" + read.getSMOG());
			} else if (metric.equals("FleschReading")) {
				print("" + read.getFleschReadingEase());
			} else if (metric.equals("FleschKincaid")) {
				print("" + read.getFleschKincaidGradeLevel());
			} else if (metric.equals("ARI")) {
				print("" + read.getARI());
			} else if (metric.equals("GunningFog")) {
				print("" + read.getGunningFog());
			} else if (metric.equals("ColemanLiau")) {
				print("" + read.getColemanLiau());
			} else {
				r.setStatus(500);
				print("Invalid value for the metric parameter. It should be one of SMOGIndex, SMOG, FleschReading, FleschKincaid, ARI, GunningFog, ColemanLiau");
				return;
			}

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
