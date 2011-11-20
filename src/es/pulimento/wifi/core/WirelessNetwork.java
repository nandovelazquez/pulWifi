package es.pulimento.wifi.core;

import java.util.ArrayList;
import java.util.HashMap;

import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

public class WirelessNetwork implements Parcelable, Comparable<WirelessNetwork> {
	// TODO: Be able of processing capabilities.

	static HashMap<String, Boolean> mDatabase = new HashMap<String, Boolean>();

	private String mEssid, mBssid, mCapabilities;
	private boolean mCrackeable;
	private int mSignal;
	private ArrayList<String> mPasswords;

	public WirelessNetwork(ScanResult s) {
		mEssid = s.SSID;
		mBssid = s.BSSID.toUpperCase();
		mPasswords = new ArrayList<String>();
		mSignal = s.level;
		mCapabilities = s.capabilities;
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

	public WirelessNetwork(String ESSID, String BSSID, int signal, String capabilities) {
		mEssid = ESSID;
		mBssid = BSSID.toUpperCase();
		mPasswords = new ArrayList<String>();
		mSignal = signal;
		mCapabilities = capabilities;
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

	public WirelessNetwork(Parcel in) {
		// We just need to read back each field in the order that it was written to the parcel.
		mEssid = in.readString();
		mBssid = in.readString();
		boolean c[] = new boolean[1];
		in.readBooleanArray(c);
		mCrackeable = c[0];
		if(mPasswords == null)
			mPasswords = new ArrayList<String>();
		in.readStringList(mPasswords);
		mSignal = in.readInt();
		mCapabilities = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// We just need to write each field into the parcel.
		// When we read from parcel, they will come back in the same order.
		dest.writeString(mEssid);
		dest.writeString(mBssid);
		dest.writeBooleanArray(new boolean[]{ mCrackeable });
		dest.writeStringList(mPasswords);
		dest.writeInt(mSignal);
		dest.writeString(mCapabilities);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<WirelessNetwork> CREATOR = new Parcelable.Creator<WirelessNetwork>() {
		public WirelessNetwork createFromParcel(Parcel in) {
			return new WirelessNetwork(in);
		}

		public WirelessNetwork[] newArray(int size) {
			return new WirelessNetwork[size];
		}
	};

	public String getEssid() {
		return mEssid;
	}

	public int getSignal() {
		return mSignal;
	}

	public String getBssid() {
		return mBssid;
	}

	public ArrayList<String> getPasswords() {
		return mPasswords;
	}

	public void crack() {
		String[] passwds = (new CrackNetwork(this)).crackNetwork().split("\n");
		mPasswords.clear();
		for(String tmp : passwds)
			if(!tmp.contains("null"))
				mPasswords.add(tmp);
	}

	public boolean getCrackeable() {
		return mCrackeable;
	}

	public String getCapabilities() {
		return mCapabilities;
	}

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