package es.pulimento.wifi.core.algorithms;

import es.pulimento.wifi.core.WirelessEncryption;

public class DiscusAlgorithm extends CrackAlgorithm {

	/*
	 * Discus cracking algorithm.
	 * 
	 * Pirelli Discuss DRG A225 router default
	 * WPA2-PSK password cracking algorithm. 
	 * 
	 */

	public DiscusAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	@Override
	protected void setPatterns() {

		// BSSID: Discus--XXXXXX
		// ESSID: Any
		addPattern("Discus--([0-9a-fA-F]{6})", "([0-9A-Fa-f:]{17})");
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		return "YW0" + Integer.toString((Integer.parseInt(essid_data, 16)-0xD0EC31)>>2);
	}

	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		if(mCapabilities.equals(WirelessEncryption.WPA))
			return true;

		return false;
	}

}
