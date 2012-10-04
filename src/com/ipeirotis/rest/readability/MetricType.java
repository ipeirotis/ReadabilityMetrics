package com.ipeirotis.rest.readability;

/**
 * Enumeration of different metric types available.
 *
 * @author TimoT
 *
 */
public enum MetricType {
	SMOG(1), FLESCH_READING(2), FLESCH_KINCAID(3), ARI(4), GUNNING_FOG(5),
	COLEMAN_LIAU(6), SMOG_INDEX(7);

	private Integer type = null;
	private MetricType(Integer type) {
		
	}	
	public Integer getType() {
		return type;
	}
}
