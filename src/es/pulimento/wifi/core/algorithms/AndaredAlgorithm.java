package es.pulimento.wifi.core.algorithms;

import es.pulimento.wifi.core.WirelessEncryption;

public class AndaredAlgorithm extends CrackAlgorithm {

	/*
	 * Andared cracking algorithm.
	 * 
	 * This is a series of network access points in
	 * public education centers of Andaluc’a, Spain
	 * which where set up by the gobernment.
	 * 
	 */

	public static WirelessEncryption[] encryption = { WirelessEncryption.WPA };

	@Override
	protected void setPatterns() {

		// ESSID: Andared
		// BSSID: Any
		addPattern("Andared", "*");

	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		return "6b629f4c299371737494c61b5a101693a2d4e9e1f3e1320f3ebf9ae379cecf32";
	}

}
