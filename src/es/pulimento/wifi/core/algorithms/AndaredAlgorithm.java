package es.pulimento.wifi.core.algorithms;

import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;

/**
 * Andared cracking algorithm.
 * This is a series of network access points in public education centers of Andaluc’a, Spain which where set up by the government.
 */
public class AndaredAlgorithm extends CrackAlgorithm {

	/**
	 * {@inheritDoc}
	 */
	public AndaredAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	@Override
	protected void setPatterns() {

		// ESSID: Andared
		// BSSID: Any
		addPattern("(Andared)", "([0-9A-Fa-f:]{17})");

	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		return "6b629f4c299371737494c61b5a101693a2d4e9e1f3e1320f3ebf9ae379cecf32";
	}

	public static boolean supportsEncryption(WirelessEncryption w) {
		return w.equals(WirelessEncryption.WPA);
	}

}
