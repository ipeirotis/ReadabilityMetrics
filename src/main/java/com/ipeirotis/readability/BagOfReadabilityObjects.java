package com.ipeirotis.readability;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.ipeirotis.readability.entities.MetricType;

@XmlRootElement
public class BagOfReadabilityObjects {	

	private Map<MetricType, Double> results = new HashMap<MetricType, Double>();

	public BagOfReadabilityObjects(Readability r) {
		for (MetricType metric : MetricType.values()) {
			results.put(metric, r.getMetric(metric));
		}
	}
 
	public BagOfReadabilityObjects() {
		
	}
	
	public void setMetric(MetricType t, Double v) {
		results.put(t, v);
	}
	
	public Double getMetric(MetricType t) {
		return results.get(t);
	}

}
