package es.pulimento.wifi.core;

import es.pulimento.wifi.R;

public enum WirelessEncryption {
		OPEN,
		WEP,
		WPA,
		UNKNOWN;

	public static WirelessEncryption parseEncription(String cap) {
		if(cap.equals("OPEN") || cap.equals("") || cap == null)
			return WirelessEncryption.OPEN;
		else if(cap.contains("WEP"))
			return WirelessEncryption.WEP;
		else if(cap.contains("WPA"))
			return WirelessEncryption.WPA;
		else
			return WirelessEncryption.UNKNOWN;
	}

	public int toStringId() {
		if(this.equals(OPEN))
			return R.string.listadapter_open;
		else if(this.equals(WEP))
			return R.string.listadapter_wep;
		else if(this.equals(WPA))
			return R.string.listadapter_wpa;
		else
			return R.string.listadapter_unknown;
	}
}
