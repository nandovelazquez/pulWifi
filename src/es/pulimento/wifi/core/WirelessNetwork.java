package es.pulimento.wifi.core;

import java.util.ArrayList;
import java.util.HashMap;

import es.pulimento.wifi.R;

import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents a Wireless Network.
 */
public class WirelessNetwork implements Parcelable, Comparable<WirelessNetwork> {

	/**
	 * This enumeration represents the encryption of a wireless network.
	 */
	public enum WirelessEncryption {
		/**
		 * No encryption.
		 */
		OPEN,
		/**
		 * Wired Equivalent Privacy.
		 */
		WEP,
		/**
		 * Wi-Fi Protected Access on its first or second version.
		 */
		WPA,
		/**
		 * Simbolizes an unparseable encryption.
		 */
		UNKNOWN;

		/**
		 * Given a capabilities string returns an encryption representation.
		 * @param cap {@link android.net.wifi.ScanResult#capabilities} string.
		 * @return {@link es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption} object representing the passed capabilities.
		 */
		public static WirelessEncryption parseEncription(String cap) {
			if(cap == null || cap.equals("OPEN") || cap.equals(""))
				return WirelessEncryption.OPEN;
			else if(cap.contains("WEP"))
				return WirelessEncryption.WEP;
			else if(cap.contains("WPA"))
				return WirelessEncryption.WPA;
			else
				return WirelessEncryption.UNKNOWN;
		}

		/**
		 * Get the name of the current object.
		 * @return Android string ID.
		 */
		public int toStringId() {
			if(this.equals(OPEN))
				return R.string.listadapter_open;
			else if(this.equals(WEP))
				return R.string.listadapter_wep;
			else if(this.equals(WPA))
				return R.string.listadapter_wpa;
			else return R.string.listadapter_unknown;
		}

		/**
		 * @deprecated You sould use toStringId() instead of this method.
		 */
		@Override
		@Deprecated
		public String toString() {
			return null;
		}
	}

	static HashMap<String, Boolean> mDatabase = new HashMap<String, Boolean>();

	private String mEssid, mBssid;
	private boolean mCrackeable;
	private int mSignal;
	private ArrayList<String> mPasswords;
	private WirelessEncryption mCapabilities;

	/**
	 * Build up an {@link es.pulimento.wifi.core.WirelessNetwork} object from an {@link android.net.wifi.ScanResult} object.
	 * @param s Android {@link android.net.wifi.ScanResult} for building the object.
	 */
	public WirelessNetwork(ScanResult s) {
		mEssid = s.SSID;
		mBssid = s.BSSID.toUpperCase();
		mPasswords = new ArrayList<String>();
		mSignal = s.level;
		mCapabilities = WirelessEncryption.parseEncription(s.capabilities);
		if(mDatabase.containsKey(mEssid))
			mCrackeable = mDatabase.get(mEssid);
		else
		{
			mCrackeable = (new CrackNetwork(this)).isCrackeable();
			mDatabase.put(mEssid, mCrackeable);
		}
	}

	/**
	 * Build up an {@link es.pulimento.wifi.core.WirelessNetwork} object from it's details.
	 * @param ESSID Represents the network name.
	 * @param BSSID Access point hardware address.
	 * @param signal The strength of the signal in decibels.
	 * @param capabilities The encryption of the network.
	 */
	public WirelessNetwork(String ESSID, String BSSID, int signal, String capabilities) {
		mEssid = ESSID;
		mBssid = BSSID.toUpperCase();
		mPasswords = new ArrayList<String>();
		mSignal = signal;
		mCapabilities = WirelessEncryption.parseEncription(capabilities);
		if(mDatabase.containsKey(mEssid))
		{
			mCrackeable = mDatabase.get(mEssid);
		}
		else
		{
			mCrackeable = (new CrackNetwork(this)).isCrackeable();
			mDatabase.put(mEssid, mCrackeable);
		}
	}

	/**
	 * Constructor needed for letting this class implement {@link android.os.Parcelable}.
	 * @param in {@link android.os.Parcel} from which a {@link es.pulimento.wifi.core.WirelessNetwork} object is created.
	 */
	public WirelessNetwork(Parcel in) {
		// We just need to read back each field in the order that it was
		// written to the parcel.
		mEssid = in.readString();
		mBssid = in.readString();
		boolean c[] = new boolean[1];
		in.readBooleanArray(c);
		mCrackeable = c[0];
		if(mPasswords == null)
			mPasswords = new ArrayList<String>();
		in.readStringList(mPasswords);
		mSignal = in.readInt();
		mCapabilities = WirelessEncryption.parseEncription(in.readString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// We just need to write each field into the parcel.
		// When we read from parcel, they will come back in the same order.
		dest.writeString(mEssid);
		dest.writeString(mBssid);
		dest.writeBooleanArray(new boolean[]{ mCrackeable });
		dest.writeStringList(mPasswords);
		dest.writeInt(mSignal);
		// TODO: Replace deprecated method....
		dest.writeString(mCapabilities.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Must be implemented for letting the object be {@link android.os.Parcelable}
	 */
	public static final Parcelable.Creator<WirelessNetwork> CREATOR = new Parcelable.Creator<WirelessNetwork>() {
		public WirelessNetwork createFromParcel(Parcel in) {
			return new WirelessNetwork(in);
		}

		public WirelessNetwork[] newArray(int size) {
			return new WirelessNetwork[size];
		}
	};

	/**
	 * Getter for the name of the wireless network.
	 * @return The name of the wireless network.
	 */
	public String getEssid() {
		return mEssid;
	}

	/**
	 * Getter for the signal level.
	 * @return The signal of the wireless network in decibeles.
	 */
	public int getSignal() {
		return mSignal;
	}

	/**
	 * Getter for the hardware address of the access point.
	 * @return The hardware address of the acces point.
	 */
	public String getBssid() {
		return mBssid;
	}

	/**
	 * Getter for the possible passwords of the wireless network.
	 * @return An array containing all possible passwords of the wireless network.
	 */
	public ArrayList<String> getPasswords() {
		return mPasswords;
	}

	/**
	 * Method that computes all possible passwords of the current network. Should be called before {@link es.pulimento.wifi.core.WirelessNetwork#getPasswords}.
	 */
	public void crack() {
		if(mCrackeable) {
			String[] passwds = (new CrackNetwork(this)).crackNetwork().split("\n");
			mPasswords.clear();
			for(String tmp : passwds)
				if(!tmp.contains("null"))
					mPasswords.add(tmp);
		}
	}

	/**
	 * Returns whether this network is crackeable or not.
	 * @return True if this network is vulnerable and false if not.
	 */
	public boolean getCrackeable() {
		return mCrackeable;
	}

	/**
	 * Returns an {@link es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption} object representing the encryption of the net.
	 * @return The encryption of the net in an {@link es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption} object.
	 */
	public WirelessEncryption getCapabilities() {
		return mCapabilities;
	}

	/**
	 * Comparator between nets. Gives preference to vulnerable ones and after this criteria gives preference to those with a higher signal level.
	 */
	public int compareTo(WirelessNetwork w0) {
		if(this.getCrackeable()) {
			if(w0.getCrackeable()) {
				return w0.getSignal() - this.getSignal();
			}
			return -1;
		}
		else if(w0.getCrackeable())
			return 1;
		return 0;
	}
}