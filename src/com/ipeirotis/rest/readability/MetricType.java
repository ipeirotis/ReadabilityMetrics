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
	
	public static MetricType fromString(String value) {
		value = value.toUpperCase();
		if("SMOG".equals(value)) {
			return MetricType.SMOG;
		}
		else if("FLESCH_READING".equals(value)) {
			return MetricType.FLESCH_READING;
		}
		else if("FLESCH_KINCAID".equals(value)) {
			return MetricType.FLESCH_KINCAID;
		}
		else if("ARI".equals(value)) {
			return MetricType.ARI;
		}
		else if("GUNNING_FOG".equals(value)) {
			return MetricType.GUNNING_FOG;
		}
		else if("COLEMAN_LIAU".equals(value)) {
			return MetricType.COLEMAN_LIAU;
		}
		else if("SMOG_INDEX".equals(value)) {
			return MetricType.SMOG_INDEX;
		}
		else {
			return null;
		}
	}
}
