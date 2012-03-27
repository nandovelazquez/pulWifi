package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;
import java.util.List;

import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;

/**
 * Wlan 6X algorithm.
 * 
 * Supported network names are:
 * WLANXXXXXX
 * WIFIXXXXXX
 * YACOMXXXXXX
 */
public class Wlan6XAlgorithm extends CrackAlgorithm {

	/**
	 * {@inheritDoc}
	 */
	public Wlan6XAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	@Override
	protected void setPatterns() {

		// ESSID: WLANXXXXXX / WIFIXXXXXX / YACOMXXXXXX
		// BSSID: Any
		addPattern("(?:WLAN|WIFI|YACOM)([0-9a-fA-F]{6})", "([0-9A-Fa-f:]{17})");
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		bssid_data = bssid_data.toUpperCase();

		char[] essid_part = essid_data.toCharArray();
		for(int k = 0; k < 6; ++k)
			if(essid_part[k] >= 'A')
				essid_part[k] = (char)(essid_part[k] - 55);

		char[] bssid_part = new char[2];
		bssid_part[0] = bssid_data.charAt(15);
		bssid_part[1] = bssid_data.charAt(16);
		if(bssid_part[0] >= 'A')
			bssid_part[0] = (char)(bssid_part[0] - 55);
		if(bssid_part[1] >= 'A')
			bssid_part[1] = (char)(bssid_part[1] - 55);

		List<String> passList = new ArrayList<String>();
		for(int i = 0; i < 10; ++i) {
			int aux = i + (essid_part[3] & 0xf) + (bssid_part[0] & 0xf) + (bssid_part[1] & 0xf);
			int aux1 = (essid_part[1] & 0xf) + (essid_part[2] & 0xf) + (essid_part[4] & 0xf) + (essid_part[5] & 0xf);
			int second = aux ^ (essid_part[5] & 0xf);
			int sixth = aux ^ (essid_part[4] & 0xf);
			int tenth = aux ^ (essid_part[3] & 0xf);
			int third = aux1 ^ (essid_part[2] & 0xf);
			int seventh = aux1 ^ (bssid_part[0] & 0xf);
			int eleventh = aux1 ^ (bssid_part[1] & 0xf);
			int fourth = (bssid_part[0] & 0xf) ^ (essid_part[5] & 0xf);
			int eighth = (bssid_part[1] & 0xf) ^ (essid_part[4] & 0xf);
			int twelfth = aux ^ aux1;
			int fifth = second ^ eighth;
			int ninth = seventh ^ eleventh;
			int thirteenth = third ^ tenth;
			int first = twelfth ^ sixth;
			passList.add((Integer.toHexString(first & 0xf) + Integer.toHexString(second & 0xf)
					+ Integer.toHexString(third & 0xf) + Integer.toHexString(fourth & 0xf)
					+ Integer.toHexString(fifth & 0xf) + Integer.toHexString(sixth & 0xf)
					+ Integer.toHexString(seventh & 0xf) + Integer.toHexString(eighth & 0xf)
					+ Integer.toHexString(ninth & 0xf) + Integer.toHexString(tenth & 0xf)
					+ Integer.toHexString(eleventh & 0xf) + Integer.toHexString(twelfth & 0xf)
					+ Integer.toHexString(thirteenth & 0xf)).toUpperCase());
		}

		StringBuilder s = new StringBuilder();
		for (String sp : passList)
			s.append(sp + "\n");
		return s.toString();
	}

	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		return false;
	}

}
