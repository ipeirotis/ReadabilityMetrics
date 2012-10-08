package com.ipeirotis.readability.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ipeirotis.readability.Readability;
import com.ipeirotis.readability.entities.MetricType;


public class MetricsTest {

	
	
	private Readability lr = new Readability(Texts.longText);
	
	
	@Test
	public void SMOG() {
		
		assertTrue(lr.getMetric(MetricType.SMOG) == 15.021);
		
	}

	@Test
	public void SMOGIndex() {
		
		assertTrue(lr.getMetric(MetricType.SMOG_INDEX) == 14.402);
	}
	
	@Test
	public void ARI() {
	
		assertTrue(lr.getMetric(MetricType.ARI) == 17.33);
	}
	
	@Test
	public void FleschKincaidGradeLevel() {
		
		assertTrue(lr.getMetric(MetricType.FLESCH_KINCAID) == 15.348);

	}
	
	@Test
	public void FleschReadingEase() {

		assertTrue(lr.getMetric(MetricType.FLESCH_READING) == 36.083);

	}
	
	@Test
	public void ColemanLiau() {
		
		assertTrue(lr.getMetric(MetricType.COLEMAN_LIAU) == 13.746);

	}
	
	@Test
	public void GunningFog() {
		
		assertTrue(lr.getMetric(MetricType.GUNNING_FOG) == 17.2);
		
	}
	
	@Test
	public void characters() {
		
		assertTrue(lr.getMetric(MetricType.CHARACTERS) == 446);
		
	}
	
	@Test
	public void sentences() {
		
		assertTrue(lr.getMetric(MetricType.SENTENCES) == 3);
		
	}
	
	@Test
	public void words() {
		
		assertTrue(lr.getMetric(MetricType.WORDS) == 86);
		
	}
	
	@Test
	public void complexWords() {
		
		assertTrue(lr.getMetric(MetricType.COMPLEXWORDS) == 13);
		
	}
	
	@Test
	public void syllables() {
		
		assertTrue(lr.getMetric(MetricType.SYLLABLES) == 144);
		
	}
	
	
}
