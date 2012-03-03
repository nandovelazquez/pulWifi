package es.pulimento.wifi.core;

import java.util.ArrayList;

import es.pulimento.wifi.core.algorithms.AlgorithmList;
import es.pulimento.wifi.core.algorithms.AndaredAlgorithm;
import es.pulimento.wifi.core.algorithms.CrackAlgorithm;
import es.pulimento.wifi.core.algorithms.DlinkAlgorithm;
import es.pulimento.wifi.core.algorithms.HaweiAlgorithm;
import es.pulimento.wifi.core.algorithms.Md5CAlgorithm;
import es.pulimento.wifi.core.algorithms.Md5ZAlgorithm;
import es.pulimento.wifi.core.algorithms.Wlan6XAlgorithm;

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
		if(DlinkAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new DlinkAlgorithm(mESSID, mBSSID));
		if(HaweiAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new HaweiAlgorithm(mESSID, mBSSID));
		if(Md5CAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new Md5CAlgorithm(mESSID, mBSSID));
		if(Md5ZAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new Md5CAlgorithm(mESSID, mBSSID));
		if(Wlan6XAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new Wlan6XAlgorithm(mESSID, mBSSID));
		
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