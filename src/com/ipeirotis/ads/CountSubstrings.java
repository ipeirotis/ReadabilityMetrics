package com.ipeirotis.ads;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;



public class CountSubstrings {

	static final int min_length = 10;
	static final int max_length = 50;
	static final int min_support = 20;
	
	public static String getFile(String filename) {
		StringBuffer buffer = new StringBuffer();

		try {
			BufferedReader dataInput = new BufferedReader(new FileReader(new File(filename)));
			String line;

			while ((line = dataInput.readLine()) != null) {
				// buffer.append(cleanLine(line.toLowerCase()));
				buffer.append(line);
				buffer.append('\n');
			}
			dataInput.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		HashMap<String, Integer> index = new HashMap<String, Integer>();
		
		
		String[] urls = getFile("c:\\temp\\adsafe.txt").split("\n");
		
		for (String url  : urls) {
			String s=null;
			
			try {
				s = java.net.URLDecoder.decode( url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			for (int start=0; start<s.length(); start++) {
				for (int end=start+min_length; end<Math.min(s.length(), start+max_length); end++) {
					String substring = s.substring(start, end);
					if (index.containsKey(substring)) {
						Integer count = index.get(substring);
						index.put(substring, count+1);
					} else {
						index.put(substring, 1);
					}
				}
			}
		}
		
		HashMap<Integer, ArrayList<String>> counter = new HashMap<Integer, ArrayList<String>>();
		for (String word : index.keySet()) {
			Integer number =  index.get(word);
			if (number<min_support) continue;
			//System.out.println(word+":"+number);
			
			ArrayList<String> strings = null;
			if (counter.containsKey(number)) {
				strings = counter.get(number);
			} else {
				strings = new ArrayList<String>();
			}
			
			// Check if "word" already contains a strings in "strings"
			ArrayList<String> toremove = new ArrayList<String>();
			for (String s : strings) {
				if (word.contains(s)) {
					toremove.add(s);
				}
			}
			strings.removeAll(toremove);
			
			boolean contained = false;
			for (String s : strings) {
				if (s.contains(word)) {
					contained=true;
					break;
				}
			}
			if (!contained)	strings.add(word);
			counter.put(number, strings);
		}
		
		// Start scanning from the most frequest strings, and eliminate substrings of lower frequencey
		for (Integer c: counter.keySet()) {
			ArrayList<String> longstrings = counter.get(c);

			for (Integer c2: counter.keySet()) {
				if (c<=c2) continue;

				ArrayList<String> shorterstrings = counter.get(c2);
				ArrayList<String> toremove = new ArrayList<String>();
				for (String longstring : longstrings) {
					for (String shortstring : shorterstrings) {
						if (longstring.contains(shortstring)) {
							toremove.add(shortstring);
						}
					}
				}
				shorterstrings.removeAll(toremove);
				counter.put(c2, shorterstrings);
				
				
			}
				

		}
		
		for (Integer c: new TreeSet<Integer>(counter.keySet())) {
			if (c<min_support) continue;
			System.out.print(c +"\t");
			ArrayList<String> strings = counter.get(c);
			for (String s : strings) {
				if (s.contains("AAAAA")) continue;
				System.out.print(s +"\t");
			}
			System.out.println();
		}

	}
	
}
