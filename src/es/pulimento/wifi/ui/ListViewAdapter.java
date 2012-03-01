package es.pulimento.wifi.ui;

import java.util.ArrayList;
import java.util.Collections;

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
import es.pulimento.wifi.R;
import es.pulimento.wifi.core.WirelessNetwork;

public class ListViewAdapter extends ArrayAdapter<WirelessNetwork> {

	/* Variables. */
	private ArrayList<WirelessNetwork> mItems;
	private Drawable mDrawLocked, mDrawUnlocked;
	private Drawable mSignalLevel1, mSignalLevel2, mSignalLevel3, mSignalLevel4;
	private LayoutInflater mLayoutInflater;

	public ListViewAdapter(Context context, int textViewResourceId, ArrayList<WirelessNetwork> items) {
		super(context, textViewResourceId, items);

		Resources res = context.getResources();

		/* Initialize all variables. */
		mItems = items;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDrawLocked = res.getDrawable(R.drawable.locked);
		mDrawUnlocked = res.getDrawable(R.drawable.unlocked);
		mSignalLevel1 = res.getDrawable(R.drawable.signal_1);
		mSignalLevel2 = res.getDrawable(R.drawable.signal_2);
		mSignalLevel3 = res.getDrawable(R.drawable.signal_3);
		mSignalLevel4 = res.getDrawable(R.drawable.signal_4);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/* Sort items before showing them. */
		Collections.sort(mItems);

		if (convertView == null)
			convertView = mLayoutInflater.inflate(R.layout.layout_selectwireless_listitem, null);

		WirelessNetwork item = mItems.get(position);
		if (item != null) {
			TextView crackeable = (TextView) convertView.findViewById(R.id.layout_selecwireless_listitem_crackeable);
			if (crackeable != null)
				crackeable.setBackgroundDrawable((item.getCrackeable()) ? mDrawUnlocked : mDrawLocked);

			TextView essid = (TextView) convertView.findViewById(R.id.layout_selecwireless_listitem_essid);
			if (essid != null)
				essid.setText(item.getEssid());

			TextView bssid = (TextView) convertView.findViewById(R.id.layout_selecwireless_listitem_bssid);
			if (bssid != null)
				bssid.setText(item.getBssid());

			ImageView signal = (ImageView) convertView.findViewById(R.id.layout_selecwireless_listitem_strength);
			if (signal != null) {
				int signalLevel = WifiManager.calculateSignalLevel(item.getSignal(), 4);
				signal.setImageDrawable((signalLevel==0) ? mSignalLevel1 : (signalLevel==1) ? mSignalLevel2 : (signalLevel==2) ? mSignalLevel3 : mSignalLevel4);
			}

			TextView capabilities = (TextView) convertView.findViewById(R.id.layout_selecwireless_listitem_security);
			if(capabilities != null)
				capabilities.setText(item.getCapabilities().toString());
		}
		return convertView;
	}

	public WirelessNetwork getItemAtPosition(int position) {
		return mItems.get(position);
	}
}
