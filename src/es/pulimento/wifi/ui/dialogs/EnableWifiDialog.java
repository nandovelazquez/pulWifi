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

package es.pulimento.wifi.ui.dialogs;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiManager;
import es.pulimento.wifi.R;

public class EnableWifiDialog extends AlertDialog implements OnClickListener {

	private WeakReference<Activity> mActivity;
	private WifiManager mWifiManager;
	private Context mContext;

	public EnableWifiDialog(Context context, WeakReference<Activity> activity) {
		super(context);
		mContext = context;
		mActivity = activity;
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		this.setCancelable(false);
		this.setTitle(R.string.mainactivity_ask_dialog_title);
		this.setMessage(context.getText(R.string.mainactivity_ask_dialog_msg));
		this.setButton(BUTTON_POSITIVE, context.getText(R.string.mainactivity_ask_dialog_yes_button), this);
		this.setButton(BUTTON_NEGATIVE, context.getText(R.string.mainactivity_ask_dialog_no_button), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		this.dismiss();
		switch(which) {
		case BUTTON_POSITIVE:
			if(!mWifiManager.setWifiEnabled(true))
				new FailedDialog(mContext, mActivity);
			break;
		case BUTTON_NEGATIVE:
			mActivity.get().finish();
			break;
		}
	}
}
