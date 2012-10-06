package com.ipeirotis.readability;

public class BagOfReadabilityObjects {
	private Integer characters;

	private Integer words;

	private Integer sentences;

	private Integer syllables;

	private Integer complexwords;

	private Double ari;

	private Double colemanliau;

	private Double fleschkincaid;

	private Double fleschreading;

	private Double gunningfog;

	private Double smog;

	private Double smogindex;

	public BagOfReadabilityObjects() {
		// no-args constructor
	}

	public Integer getCharacters() {
		return characters;
	}

	public void setCharacters(Integer characters) {
		this.characters = characters;
	}

	public Integer getWords() {
		return words;
	}

	public void setWords(Integer words) {
		this.words = words;
	}

	public Integer getSentences() {
		return sentences;
	}

	public void setSentences(Integer sentences) {
		this.sentences = sentences;
	}

	public Integer getSyllables() {
		return syllables;
	}

	public void setSyllables(Integer syllables) {
		this.syllables = syllables;
	}

	public Integer getComplexwords() {
		return complexwords;
	}

	public void setComplexwords(Integer complexwords) {
		this.complexwords = complexwords;
	}

	public Double getARI() {
		return this.ari;
	}

	public void setARI(Double ari) {
		this.ari = ari;
	}

	public Double getColemanLiau() {
		return colemanliau;
	}

	public void setColemanLiau(Double colemanLiau) {
		this.colemanliau = colemanLiau;
	}

	public Double getFleschKincaid() {
		return fleschkincaid;
	}

	public void setFleschKincaid(Double fleschKincaid) {
		this.fleschkincaid = fleschKincaid;
	}

	public Double getFleschReading() {
		return fleschreading;
	}

	public void setFleschReading(Double fleschReading) {
		fleschreading = fleschReading;
	}

	public Double getGunningFog() {
		return gunningfog;
	}

	public void setGunningFog(Double gunningFog) {
		this.gunningfog = gunningFog;
	}

	public Double getSMOG() {
		return smog;
	}

	public void setSMOG(Double smog) {
		this.smog = smog;
	}

	public Double getSMOGIndex() {
		return smogindex;
	}

	public void setSMOGIndex(Double smogindex) {
		this.smogindex = smogindex;
	}
}
