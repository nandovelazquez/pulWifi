/*
 *  pulWifi , Copyright (C) 2011-2012 Javi Pulido / Antonio Vázquez
 *  
 *  This file is part of "pulWifi"
 *
 *  "pulWifi" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  "pulWifi" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with "pulWifi".  If not, see <http://www.gnu.org/licenses/>.
 */

package es.pulimento.wifi.core;

import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;
import es.pulimento.wifi.core.algorithms.AlgorithmList;
import es.pulimento.wifi.core.algorithms.AndaredAlgorithm;
import es.pulimento.wifi.core.algorithms.DiscusAlgorithm;
import es.pulimento.wifi.core.algorithms.DlinkAlgorithm;
import es.pulimento.wifi.core.algorithms.HuaweiAlgorithm;
import es.pulimento.wifi.core.algorithms.ComtrendAlgorithm;
import es.pulimento.wifi.core.algorithms.ZyxelAlgorithm;
import es.pulimento.wifi.core.algorithms.Wlan6XAlgorithm;

/**
 * This class is a holder for all the wireless cracking algorithms. It selects
 * the valid algorithms and passes the function calls to any delegated
 * algorithm.
 */
public class CrackNetwork {

	private AlgorithmList algorithms;
	private WirelessEncryption mCapabilities;
	private String mESSID, mBSSID;

	/**
	 * Default constructor.
	 * 
	 * @param w
	 *            {@link es.pulimento.wifi.core.WirelessNetwork} representing a
	 *            detected network.
	 */
	public CrackNetwork(WirelessNetwork w) {

		algorithms = new AlgorithmList();
		mCapabilities = w.getCapabilities();
		mESSID = w.getEssid();
		mBSSID = w.getBssid();

		if (AndaredAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new AndaredAlgorithm(mESSID, mBSSID));
		if (DiscusAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new DiscusAlgorithm(mESSID, mBSSID));
		if (DlinkAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new DlinkAlgorithm(mESSID, mBSSID));
		if (HuaweiAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new HuaweiAlgorithm(mESSID, mBSSID));
		if (ComtrendAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new ComtrendAlgorithm(mESSID, mBSSID));
		if (ZyxelAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new ComtrendAlgorithm(mESSID, mBSSID));
		if (Wlan6XAlgorithm.supportsEncryption(mCapabilities))
			algorithms.add(new Wlan6XAlgorithm(mESSID, mBSSID));
	}

	/**
	 * Checks whether a network is vulnerable or not.
	 * 
	 * @return Boolean value. True if vulnerable and false if not.
	 */
	public boolean isCrackeable() {
		if (mCapabilities.equals(WirelessEncryption.OPEN))
			return true;

		return algorithms.isCrackeable();
	}

	/**
	 * Function to break the wireless network security.
	 * 
	 * @return A list of passwords separated by newline characters ('\n').
	 */
	public String crackNetwork() {
		if (mCapabilities.equals(WirelessEncryption.OPEN))
			return "";

		return algorithms.crack();
	}
}