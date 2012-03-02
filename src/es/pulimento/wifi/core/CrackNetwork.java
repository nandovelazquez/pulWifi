package es.pulimento.wifi.core;

import java.util.ArrayList;

import es.pulimento.wifi.core.algorithms.AlgorithmList;
import es.pulimento.wifi.core.algorithms.AndaredAlgorithm;
import es.pulimento.wifi.core.algorithms.CrackAlgorithm;

public class CrackNetwork {

	private AlgorithmList algorithms;
	private WirelessEncryption mCapabilities;
	private String mESSID, mBSSID;

	public CrackNetwork(WirelessNetwork w) {

		algorithms = (AlgorithmList) new ArrayList<CrackAlgorithm>();
		mCapabilities = w.getCapabilities();
		mESSID = w.getEssid();
		mBSSID = w.getBssid();

		if(AndaredAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new AndaredAlgorithm(mESSID, mBSSID));
		
		
	}

	public boolean isCrackeable() {
		if(mCapabilities.equals(WirelessEncryption.OPEN))
			return true;

		return algorithms.isCrackeable();
	}

	public String crackNetwork() {
		if(mCapabilities.equals(WirelessEncryption.OPEN))
			return "NOPASSNOPASSNOPASSNOPASS";	// TODO: Change this to empty string.

		return algorithms.crack();
	}	
}