package com.ipeirotis.readability.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Entity
@JsonIgnoreProperties(value = { "owner" })
public class TextMetric extends BaseEntity {
	public TextMetric() {
	}
	
	public TextMetric(Text parent, MetricType metricType) {
		this.id = String.format("%s:%s", parent.getId(), metricType.name());
		this.owner = parent.getId();
		this.metricType = metricType.name();
	}
	
	@Id
	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Index
	String owner;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String text) {
		this.owner = text;
	}

	@Index
	String metricType;

	public String getMetricType() {
		return metricType;
	}

	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}

	@Serialize
	BigDecimal value;

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
