package com.ipeirotis.readability;

import com.ipeirotis.readability.entities.MetricType;

/**
 * Implements various readability indexes
 * 
 * @author Panos Ipeirotis
 * 
 */
public class Readability {

	private static SentenceExtractor se = new SentenceExtractor();

	Integer sentences;

	Integer complex;

	Integer words;

	Integer syllables;

	Integer characters;

	public double getMetric(MetricType t) throws IllegalArgumentException {
		
		switch (t) {
			case SMOG: 
				return getSMOG();
			case FLESCH_READING: 
				return getFleschReadingEase();
			case FLESCH_KINCAID: 
				return getFleschKincaidGradeLevel(); 
			case ARI: 
				return getARI();
			case GUNNING_FOG: 
				return getGunningFog();
			case COLEMAN_LIAU: 
				return getColemanLiau();
			case SMOG_INDEX: 
				return getSMOGIndex();
			case CHARACTERS: 
				return getCharacters();
			case SYLLABLES: 
				return getSyllables();
			case WORDS: 
				return getWords();
			case COMPLEXWORDS: 
				return getComplex();
			case SENTENCES: 
				return getSentences();
				
			default:
				throw new IllegalArgumentException("Readability Metric '"+t.toString()+"' not supported");
		}
	}
	
	private int getCharacters() {
		return characters;
	}

	private int getComplex() {
		return complex;
	}

	private int getSentences() {
		return sentences;
	}

	private int getSyllables() {
		return syllables;
	}

	private int getWords() {
		return words;
	}

	public Readability(String text) {

		// We add the "." for the sentence extractor to pick the last sentence, if it does not end with a punctuation mark.
		this.sentences = getNumberOfSentences(text + ".");
		this.complex = getNumberOfComplexWords(text);
		this.words = getNumberOfWords(text);
		this.syllables = getNumberOfSyllables(text);
		this.characters = getNumberOfCharacters(text);

	}

	/**
	 * This is a helper function in order to generate a JSON object
	 * 
	 * @return The readability metrics in a single object.
	 */
	public BagOfReadabilityObjects getMetrics() {
		return new BagOfReadabilityObjects(this);
	}

	/**
	 * Returns true is the word contains 3 or more syllables
	 * 
	 * @param w
	 * @return
	 */
	private static boolean isComplex(String w) {
		int syllables = Syllabify.syllable(w);
		return (syllables > 2);
	}

	/**
	 * Returns the number of letter characters in the text
	 * 
	 * @return
	 */
	private static int getNumberOfCharacters(String text) {
		String cleanText = Utilities.cleanLine(text);
		String[] word = cleanText.split(" ");

		int characters = 0;
		for (String w : word) {
			characters += w.length();
		}
		return characters;
	}

	/**
	 * Returns the number of words with 3 or more syllables
	 * 
	 * @param text
	 * @return the number of words in the text with 3 or more syllables
	 */
	private static int getNumberOfComplexWords(String text) {
		String cleanText = Utilities.cleanLine(text);
		String[] words = cleanText.split(" ");
		int complex = 0;
		for (String w : words) {
			if (isComplex(w))
				complex++;
		}
		return complex;
	}

	private static int getNumberOfWords(String text) {
		String cleanText = Utilities.cleanLine(text);
		String[] word = cleanText.split(" ");
		int words = 0;
		for (String w : word) {
			if (w.length() > 0)
				words++;
		}
		return words;
	}

	/**
	 * Returns the total number of syllables in the words of the text
	 * 
	 * @param text
	 * @return the total number of syllables in the words of the text
	 */
	private static int getNumberOfSyllables(String text) {
		String cleanText = Utilities.cleanLine(text);
		String[] word = cleanText.split(" ");
		int syllables = 0;
		for (String w : word) {
			if (w.length() > 0) {
				syllables += Syllabify.syllable(w);
			}
		}
		return syllables;
	}

	private static int getNumberOfSentences(String text) {
		int l = se.getSentences(text).length;
		if (l > 0)
			return l;
		else if (text.length() > 0)
			return 1;
		else
			return 0;
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/SMOG_Index
	 * 
	 * @param text
	 * @return The SMOG index of the text
	 */
	private double getSMOGIndex() {
		double score = Math.sqrt(complex * (30.0 / sentences)) + 3;
		return Utilities.round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/SMOG
	 * 
	 * @param text
	 * @return Retugns the SMOG value for the text
	 */
	private double getSMOG() {
		double score = 1.043 * Math.sqrt(complex * (30.0 / sentences)) + 3.1291;
		return Utilities.round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Flesch-Kincaid_Readability_Test
	 * 
	 * @param text
	 * @return Returns the Flesch_Reading Ease value for the text
	 */
	private double getFleschReadingEase() {

		double score = 206.835 - 1.015 * words / sentences - 84.6 * syllables / words;

		return Utilities.round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Flesch-Kincaid_Readability_Test
	 * 
	 * @param text
	 * @return Returns the Flesch-Kincaid_Readability_Test value for the text
	 */
	private double getFleschKincaidGradeLevel() {
		double score = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
		return Utilities.round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Automated_Readability_Index
	 * 
	 * @param text
	 * @return the Automated Readability Index for text
	 */
	private double getARI() {
		double score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
		return Utilities.round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Gunning-Fog_Index
	 * 
	 * @param text
	 * @return the Gunning-Fog Index for text
	 */
	private double getGunningFog() {
		double score = 0.4 * (words / sentences + 100 * complex / words);
		return Utilities.round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Coleman-Liau_Index
	 * 
	 * @return The Coleman-Liau_Index value for the text
	 */
	private double getColemanLiau() {
		double score = (5.89 * characters / words) - (30 * sentences / words) - 15.8;
		return Utilities.round(score, 3);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String GreenEggsAndHam = "I do not like them in a box. " + "I do not like them with a fox. "
				+ "I do not like them in a house. " + "I do not like them with a mouse. " + "I do not like them here or there. "
				+ "I do not like them anywhere. " + "I do not like green eggs and ham. " + "I do not like them, Sam-I-am.";

		Readability r = new Readability(GreenEggsAndHam);

		System.out.println("SMOG Index :" + r.getSMOGIndex());
		System.out.println("SMOG :" + r.getSMOG());
		System.out.println("Flesch Reading Ease :" + r.getFleschReadingEase());
		System.out.println("Flesch-Kincaid Grade Level :" + r.getFleschKincaidGradeLevel());
		System.out.println("Automated Readability Index :" + r.getARI());
		System.out.println("Gunning-Fog Index :" + r.getGunningFog());
		System.out.println("Coleman-Liau Index :" + r.getColemanLiau());

		System.out.println("\n--------------------------------------------------------\n");
		String logorrhea = "The word logorrhoea is often used pejoratively " + "to describe prose that is highly abstract and "
				+ "contains little concrete language. Since abstract " + "writing is hard to visualize, it often seems as though "
				+ "it makes no sense and all the words are excessive. "
				+ "Writers in academic fields that concern themselves mostly "
				+ "with the abstract, such as philosophy and especially "
				+ "postmodernism, often fail to include extensive concrete "
				+ "examples of their ideas, and so a superficial examination "
				+ "of their work might lead one to believe that it is all nonsense.";

		r = new Readability(logorrhea);

		System.out.println("SMOG Index :" + r.getSMOGIndex());
		System.out.println("SMOG :" + r.getSMOG());
		System.out.println("Flesch Reading Ease :" + r.getFleschReadingEase());
		System.out.println("Flesch-Kincaid Grade Level :" + r.getFleschKincaidGradeLevel());
		System.out.println("Automated Readability Index :" + r.getARI());
		System.out.println("Gunning-Fog Index :" + r.getGunningFog());
		System.out.println("Coleman-Liau Index :" + r.getColemanLiau());

	}

}
