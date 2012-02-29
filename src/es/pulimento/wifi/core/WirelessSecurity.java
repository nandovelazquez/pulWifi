package es.pulimento.wifi.core;

public class WirelessSecurity {

	public static enum Encription {
		OPEN,
		WEP,
		WPA,
		WPA2
	}

	public static Encription parseEncription(String cap) {
		// TODO: Implement.
		return Encription.WEP;
	}
}
