package es.pulimento.wifi.core;


public class CrackNetwork {

	private WirelessEncryption mCapabilities;
	private String mESSID, mBSSID;

	public CrackNetwork(WirelessNetwork w) {
		mCapabilities = w.getCapabilities();
		mESSID = w.getEssid();
		mBSSID = w.getBssid();

	}

	public boolean isCrackeable() {
		// TODO: Reimplement under the new organization.

		if(mCapabilities.equals(WirelessEncryption.OPEN))
			return true;

		return false;
	}

	public String crackNetwork() {
		if(mCapabilities.equals(WirelessEncryption.OPEN))
			return "NOPASSNOPASSNOPASSNOPASS";	// TODO: Change this to empty string.

		return null;
	}	
}