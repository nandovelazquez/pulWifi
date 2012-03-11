package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is an abstract class that provides the base for a wireless cracking algorithm.
 */
public abstract class CrackAlgorithm {

	private List<Matcher> matchers;
	private List<String> patterns;
	private int n_of_patterns;
	private int working_pattern;

	/**
	 * Default constructor.
	 * @param essid The network name.
	 * @param bssid The access point hardware address.
	 */
	public CrackAlgorithm(String essid, String bssid) {

		matchers = new ArrayList<Matcher>();
		patterns = new ArrayList<String>();
		working_pattern = -1;

		setPatterns();

		n_of_patterns = patterns.size();

		for(int i = 0; i < n_of_patterns; i+=2) {
			matchers.add(Pattern.compile(patterns.get(i)).matcher(essid));
			matchers.add(Pattern.compile(patterns.get(i+1)).matcher(bssid));
		}
	}

	protected void addPattern(String pat, String pat2) {
		patterns.add(pat);
		patterns.add(pat2);
	}

	/**
	 * Checks if the algorithm supports the passed network.
	 * @return True if this algorithm can crack the network or false if not.
	 */
	public boolean isCrackeable() {
		for(int i = 0; i < n_of_patterns; i+=2)
			if(matchers.get(i).find() && matchers.get(i+1).find()) {
				working_pattern = i;
				return true;
			}

		return false;
	}

	/**
	 * Cracks the network if possible.
	 * @return All the possible passwords of the network.
	 */
	public String crack() {
		if(working_pattern != -1) {
			return crackAlgorithm(matchers.get(working_pattern).group(1), matchers.get(working_pattern+1).group(1));
		} else if(isCrackeable()) {
			return crack();
		}
		return null;
	}

	/**
	 * Function that should add the patterns for matching the various nets depending on the algorithm.
	 */
	protected abstract void setPatterns();

	/**
	 * Function that should implement the algorithm for cracking a network.
	 * @param essid_data Matched data in network name.
	 * @param bssid_data Matched data in hardware address.
	 * @return The possible passwords of the net or null on error.
	 */
	protected abstract String crackAlgorithm(String essid_data, String bssid_data);

}
