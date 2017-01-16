package com.ipeirotis.readability.engine;

import java.util.regex.Pattern;

/**
 * @author Panos Ipeirotis
 * @author Steve Ash
 * 
 *         Java Code to estimate the number of syllables in a word.
 * 
 *         Translation of the Perl code by Greg Fast, found at:
 *         http://search.cpan.org/author/GREGFAST/Lingua-EN-Syllable-0.251/
 * 
 *         For documentation and comments
 *         http://search.cpan.org/src/GREGFAST/Lingua
 *         -EN-Syllable-0.251/Syllable.pm
 *         
 * 2017-01-15 updated to EN-Syllable-0.30 and fixed regex issue
 * 
 */
public class Syllabify {

  private static final Pattern VOWELS = Pattern.compile("[^aeiouy]+");

  private static final String[] staticSubMatches = {"cial", "tia", "cius", "cious", "giu", "ion", "iou"};
  private static final Pattern[] regexSubMatches = {
      Pattern.compile(".*sia$"),
      Pattern.compile(".*.ely$"),
      Pattern.compile(".*[^td]ed$")
  };

  private static final String[] staticAddMatches = {"ia", "riet", "dien", "iu", "io", "ii", "microor"};
  private static final Pattern[] regexAddMatches = {
      Pattern.compile(".*[aeiouym]bl$"),
      Pattern.compile(".*[aeiou]{3}.*"),
      Pattern.compile("^mc.*"),
      Pattern.compile(".*ism$"),
      Pattern.compile(".*isms$"),
      Pattern.compile(".*([^aeiouy])\\1l$"),
      Pattern.compile(".*[^l]lien.*"),
      Pattern.compile("^coa[dglx]..*"),
      Pattern.compile(".*[^gq]ua[^auieo].*"),
      Pattern.compile(".*dnt$")
  };

  public static int syllable(String word) {

    word = word.toLowerCase();
    if (word.equals("w")) {
      return 2;
    }
    if (word.length() == 1) {
      return 1;
    }
    word = word.replaceAll("'", " ");

    if (word.endsWith("e")) {
      word = word.substring(0, word.length() - 1);
    }

    String[] phonems = VOWELS.split(word);

    int syl = 0;
    for (int i = 0; i < staticSubMatches.length; i++) {
      if (word.contains(staticSubMatches[i])) {
        syl -= 1;
      }
    }
    for (int i = 0; i < regexSubMatches.length; i++) {
      if (regexSubMatches[i].matcher(word).matches()) {
        syl -= 1;
      }
    }
    for (int i = 0; i < staticAddMatches.length; i++) {
      if (word.contains(staticAddMatches[i])) {
        syl += 1;
      }
    }
    for (int i = 0; i < regexAddMatches.length; i++) {
      if (regexAddMatches[i].matcher(word).matches()) {
        syl += 1;
      }
    }

    for (int i = 0; i < phonems.length; i++) {
      if (phonems[i].length() > 0) {
        syl++;
      }
    }

    if (syl == 0) {
      syl = 1;
    }

    return syl;
  }

}
