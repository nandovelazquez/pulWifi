package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CrackAlgorithm {

	private List<Matcher> matchers;
	private List<String> patterns;
	private int n_of_patterns;
	private int working_pattern;

	public CrackAlgorithm(String essid, String bssid) {

		/*
		 * NOTE: I assume that odd numbers are essid patterns
		 * and even numbers are bssid patterns that should
		 * be checked in pairs.
		 */

		matchers = new ArrayList<Matcher>();
		patterns = new ArrayList<String>();
		working_pattern = -1;

		setPatterns();

		n_of_patterns = patterns.size();

		for (int i = 0; i < n_of_patterns; i += 2) {
			matchers.add(Pattern.compile(patterns.get(i)).matcher(essid));
			matchers.add(Pattern.compile(patterns.get(i + 1)).matcher(bssid));
		}
	}

	protected void addPattern(String pat, String pat2) {
		patterns.add(pat);
		patterns.add(pat2);
	}

	public boolean isCrackeable() {
		for (int i = 0; i < n_of_patterns; i += 2)
			if (matchers.get(i).find() && matchers.get(i + 1).find()) {
				working_pattern = i;
				return true;
			}

		return false;
	}

	public String crack() {
		if (working_pattern != -1) {
			return crackAlgorithm(matchers.get(working_pattern).group(1), matchers.get(working_pattern + 1).group(1));
		} else if (isCrackeable()) {
			crack();
		}
		return null;
	}

	protected abstract void setPatterns();

	protected abstract String crackAlgorithm(String essid_data, String bssid_data);

}
