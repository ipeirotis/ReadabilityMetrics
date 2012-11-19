package com.ipeirotis.readability.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ipeirotis.readability.engine.Readability;
import com.ipeirotis.readability.model.MetricType;
import com.ipeirotis.readability.model.Text;
import com.ipeirotis.readability.model.TextMetric;

@Component
public class ReadabilityService {
	public Collection<TextMetric> getMetrics(Text t) {
		List<TextMetric> result = new ArrayList<TextMetric>();
		Readability r = new Readability(t.getContent());
		
		for (MetricType metricType : MetricType.values()) {
			TextMetric textMetric = new TextMetric(t, metricType);
			
			BigDecimal bd = new BigDecimal(Double.toString(r.getMetric(metricType)));
			bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);

			textMetric.setValue(bd);
			
			result.add(textMetric);
		}
		
		return result;
	}

}
