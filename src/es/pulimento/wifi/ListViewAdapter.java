package es.pulimento.wifi;

import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<WirelessNetwork>{

	private ArrayList<WirelessNetwork> mItems;
	private Context mContext;
	private Drawable locked, unlocked;
	
	public ListViewAdapter(Context context, int textViewResourceId, ArrayList<WirelessNetwork> items) {
		super(context, textViewResourceId, items);
		mItems = items;
		mContext = context;
		Resources res = context.getResources();
		locked = res.getDrawable(R.drawable.locked);
		unlocked = res.getDrawable(R.drawable.unlocked);
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Collections.sort(mItems, new CrackeableComparator());
		View v = convertView;
		if (v == null) {
			v = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_item, null);
		}
		WirelessNetwork item = mItems.get(position);
		if (item != null) {
			TextView crackeable = (TextView) v.findViewById(R.id.listview_item_crackeable);
			if (crackeable != null)
				//crackeable.setBackgroundColor((item.getCrackeable())?android.graphics.Color.GREEN:android.graphics.Color.RED);//REVIEW
				crackeable.setBackgroundDrawable((item.getCrackeable())?unlocked:locked);
			TextView essid = (TextView) v.findViewById(R.id.listview_item_essid);
			if (essid != null)
				essid.setText(item.getEssid());
			TextView bssid = (TextView) v.findViewById(R.id.listview_item_bssid);
			if (bssid != null)
				bssid.setText(item.getBssid());
			ImageView signal = (ImageView) v.findViewById(R.id.listview_item_signal_strength);
			signal.setImageDrawable(mContext.getResources().getDrawable((WifiManager.calculateSignalLevel(item.getSignal(), 4)==0)?R.drawable.signal_1:((WifiManager.calculateSignalLevel(item.getSignal(), 4)==1)?R.drawable.signal_2:((WifiManager.calculateSignalLevel(item.getSignal(), 4)==2)?R.drawable.signal_3:R.drawable.signal_4))));
			TextView s = (TextView) v.findViewById(R.id.tv_listitem_security);
			//s.setText(item.getCapabilities());
			if(item.getCapabilities().contains("WPA"))s.setText(R.string.network_wpawpa2);
			else if(item.getCapabilities().contains("WEP"))s.setText(R.string.network_wep);
			if(item.getCapabilities().equals("") || item.getCapabilities() == null)s.setText(R.string.network_open);
			}
		//Collections.sort(mItems, new CrackeableComparator());
		return v;
	}

	public WirelessNetwork getItemAtPosition(int position) {
		return mItems.get(position);
	}

	class CrackeableComparator implements Comparator<WirelessNetwork>{

		//@Override
		public int compare(WirelessNetwork o1, WirelessNetwork o2) {
			if(o1.getCrackeable())
				return -1;
			else if(o2.getCrackeable())
				return 1;
			return 0;
		}
	}
}
