package com.ipeirotis.rest.readability;

/**
 * Enumeration of different metric types available.
 *
 * @author TimoT
 *
 */
public enum MetricType {
	SMOG, FLESCH_READING, FLESCH_KINCAID, ARI, GUNNING_FOG, COLEMAN_LIAU, SMOG_INDEX;

	public static MetricType fromString(String value) {

		String v = value.toUpperCase().trim();
		for (MetricType metric : MetricType.values()) {
			if (metric.name().equals(v)) {
				return metric;
			}
		}
		
		throw new IllegalArgumentException("Incorrect readability metric:" + v);
	}
}
