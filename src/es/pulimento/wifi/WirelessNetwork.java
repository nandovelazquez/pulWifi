package es.pulimento.wifi;

import android.os.Parcel;
import android.os.Parcelable;

public class WirelessNetwork implements Parcelable,Comparable<WirelessNetwork> {
	
		//Atributos
		private String mEssid, mBssid, capabilities;
		private boolean crackeable;
		public String clave;
		int signal;		
		
		//public static WirelessNetwork current;
		
		//Constructores
		public WirelessNetwork(String ESSID, String BSSID, boolean isCrackeable, int signal, String capabilities) {
			mEssid = ESSID;
			mBssid = BSSID;
			crackeable = isCrackeable;
			clave = null;
			this.signal = signal;
			this.capabilities = capabilities;
			//current = this;
		}
		
		/*public WirelessNetwork(String ESSID, String BSSID) {
			mEssid = ESSID;
			mBssid = BSSID;
			crackeable = isCrackeable(this);
			clave = null;
			//current = this;
		}*/

		public WirelessNetwork(Parcel in) {
			readFromParcel(in);
			//current = this;
		}

		public String getEssid() {
			return mEssid;
		}
		
		public int getSignal(){
			return signal;
		}

		public String getBssid() {
			return mBssid;
		}
		
		public String getClave() {
			return clave;
		}
	
		public boolean getCrackeable(){
			return crackeable;
		}
		
		public String getCapabilities(){
			return capabilities;
		}
		

		public void setmEssid(String mEssid) {
			this.mEssid = mEssid;
		}

		public void setmBssid(String mBssid) {
			this.mBssid = mBssid;
		}

		public void setCrackeable(boolean crackeable) {
			this.crackeable = crackeable;
		}

		public void setClave(String clave) {
			this.clave = clave;
		}

		public int describeContents() {
			return 0;
		}
		
		
		//@Override
		public void writeToParcel(Parcel dest, int flags) {
			// We just need to write each field into the
			// parcel. When we read from parcel, they
			// will come back in the same order
			dest.writeString(mEssid);
			dest.writeString(mBssid);
			dest.writeBooleanArray(new boolean[]{ crackeable });
		}

		//@Override
		private void readFromParcel(Parcel in) {
			// We just need to read back each
			// field in the order that it was
			// written to the parcel
			mEssid = in.readString();
			mBssid = in.readString();
			boolean c[] = new boolean[1];
			in.readBooleanArray(c);
			crackeable = c[0];
		}

		public static final Parcelable.Creator<WirelessNetwork> CREATOR =
	    	new Parcelable.Creator<WirelessNetwork>() {
	            public WirelessNetwork createFromParcel(Parcel in) {
	                return new WirelessNetwork(in);
	            }
	 
	            public WirelessNetwork[] newArray(int size) {
	                return new WirelessNetwork[size];
	            }
	        };

		public int compareTo(WirelessNetwork w0) {
			// TODO Auto-generated method stub
			if(this.getCrackeable()){
				if(w0.getCrackeable()){
					return w0.getSignal() - this.getSignal();
				}
				return -1;
			}			
			else if(w0.getCrackeable()) return 1;
			return 0;
		}
}
