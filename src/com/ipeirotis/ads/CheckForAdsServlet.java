package com.ipeirotis.ads;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

import com.ipeirotis.readability.Utilities;

@SuppressWarnings("serial")
public class CheckForAdsServlet extends HttpServlet {

	private static final String url_adblock_list = "http://ipeirotis.appspot.com/easylist.txt";
	
	private HttpServletResponse r;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {

		this.r = resp;
		resp.setContentType("text/plain");

		Cache cache = null;
        try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        } catch (CacheException e) {
            ;
        }
		
        // We will check later if we need to refresh the cache
        String refresh = req.getParameter("refresh");
        
        // Let's try first to get the regular expressions from the cache 
        String regular_expressions = (String) cache.get(url_adblock_list);
        
        // If the regular expressions are not in the cache, or if we asked for a refresh, then fetch and store
        if (regular_expressions == null || (refresh != null && refresh.equals("yes"))) {
        	regular_expressions = Utilities.fetchURL(url_adblock_list);
			cache.put(url_adblock_list, regular_expressions);
			if (refresh != null && refresh.equals("yes")) return;
        }
        
        
		String url_parameter = req.getParameter("url");
		if (url_parameter == null) {
			r.setStatus(500);
			print("No url specified");
			return;
		}
		
		String html_source = (cache.containsKey(url_parameter))? (String)cache.get(url_parameter) : Utilities.fetchURL(url_parameter);
		cache.put(url_parameter, html_source);
		
		String[] regexes = regular_expressions.split("\n");
        for (String regex : regexes) {
			if (regex.startsWith("!"))
				continue;
			//if (html_source.matches(regex)) {
			if (html_source.contains(regex)) {
				print("true:" + regex);
				return;
			}
		}
		print("false");
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
