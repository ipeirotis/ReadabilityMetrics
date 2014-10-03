package com.ipeirotis.readability.endpoints;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.ipeirotis.readability.engine.Readability;
import com.ipeirotis.readability.enums.MetricType;

@Api(name = "readability", description = "The API for readability metrics", version = "v1")
public class ReadabilityEndpoint {

    @ApiMethod(name = "getReadabilityMetrics", path = "getReadabilityMetrics", httpMethod = HttpMethod.POST)
    public Map<MetricType, BigDecimal> get(@Named("text") String text) {
        Map<MetricType, BigDecimal> result = new HashMap<MetricType, BigDecimal>();
        Readability r = new Readability(text);

        for (MetricType metricType : MetricType.values()) {            
            BigDecimal value = new BigDecimal(Double.toString(r.getMetric(metricType)));
            value = value.setScale(3, BigDecimal.ROUND_HALF_UP);
            result.put(metricType, value);
        }

        return result;
    }

}