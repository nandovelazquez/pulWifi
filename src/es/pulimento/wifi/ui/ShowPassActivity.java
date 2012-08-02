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

package es.pulimento.wifi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import es.pulimento.wifi.R;
import es.pulimento.wifi.core.WirelessNetwork;
import es.pulimento.wifi.ui.dialogs.ClipboardCopyDialog;
import es.pulimento.wifi.ui.dialogs.ShowPasswordsDialog;
import es.pulimento.wifi.ui.utils.ExceptionHandler;



@SuppressWarnings("deprecation")
public class ShowPassActivity extends SherlockActivity implements OnClickListener {

	public static final String EXTRA_NETWORK = "EXTRA_WIRELESS_NETWORK";

	private Context mContext;
	private WirelessNetwork mWirelessNetwork;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		/* Set exception handler... */
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_showpass);

		mContext = this;
		mWirelessNetwork = this.getIntent().getExtras().getParcelable(EXTRA_NETWORK);

		((TextView) findViewById(R.id.layout_showpass_essid)).setText(mWirelessNetwork.getEssid());
		((TextView) findViewById(R.id.layout_showpass_bssid)).setText(mWirelessNetwork.getBssid());

		((Button) findViewById(R.id.layout_showpass_clipboard)).setOnClickListener(this);
		((Button) findViewById(R.id.layout_showpass_show)).setOnClickListener(this);

		if (mWirelessNetwork.getPasswords().get(0) == null) {
			// TODO: Show error dialog...
		} else if (mWirelessNetwork.getPasswords().get(0).equals("")) {
			Toast.makeText(mContext, R.string.showpass_nopass, Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			this.finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_showpass_show:
				(new ShowPasswordsDialog(mContext, mWirelessNetwork.getPasswords())).show();
				break;
			case R.id.layout_showpass_clipboard:
				if (mWirelessNetwork.getPasswords().size() > 1) {
					(new ClipboardCopyDialog(mContext, mWirelessNetwork.getPasswords())).show();
				} else {
					ClipboardManager mClipboardManager = (ClipboardManager) mContext
							.getSystemService(Context.CLIPBOARD_SERVICE);
					mClipboardManager.setText(mWirelessNetwork.getPasswords().get(0));
					Toast.makeText(
							mContext,
							mContext.getString(R.string.showpass_toclipboard) + " ("
									+ mWirelessNetwork.getPasswords().get(0) + ")", Toast.LENGTH_LONG).show();
					Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(i);
				}
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_showpassactivity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_showpass_back:
				this.finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}