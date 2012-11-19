package com.ipeirotis.readability.service;

import org.springframework.stereotype.Component;

import com.ipeirotis.readability.model.Text;
import com.ipeirotis.readability.model.TextMetric;

@Component
public class TextMetricService extends CompositeEntityService<TextMetric, Text> {
}
