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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import es.pulimento.wifi.R;
import es.pulimento.wifi.core.WirelessNetwork;

public class ManualFragment extends Fragment implements OnClickListener {

	private EditText mEditTextBssid;
	private EditText mEditTextEssid;
	private Button mButtonAccept;
	private ToggleButton mToggleButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/* create view and return it */
		View inflatedView = inflater.inflate(R.layout.layout_manualfragment, container, false);
		mEditTextEssid = (EditText) inflatedView.findViewById(R.id.layout_manualcrack_essid);
		mEditTextBssid = (EditText) inflatedView.findViewById(R.id.layout_manualcrack_bssid);
		mButtonAccept = (Button) inflatedView.findViewById(R.id.layout_manualcrack_accept);
		mToggleButton = (ToggleButton) inflatedView.findViewById(R.id.layout_manualcrack_togglebutton);
		return inflatedView;
	}

	@Override
	public void onStart() {
		super.onStart();

		mButtonAccept.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String capabilities = (String) (mToggleButton.isChecked() ? mToggleButton.getTextOn() : mToggleButton.getTextOff());
		WirelessNetwork mWirelessNetwork = new WirelessNetwork(mEditTextEssid.getText().toString(), mEditTextBssid.getText().toString(), 1, capabilities);
		if (!mWirelessNetwork.isCrackeable()) {
			Toast.makeText(getActivity().getApplicationContext(), R.string.manualcrack_inputerror, Toast.LENGTH_LONG).show();
			return;
		}
		mWirelessNetwork.crack();
		Intent i = new Intent(getActivity().getApplicationContext(), ShowPassActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(ShowPassActivity.EXTRA_NETWORK, mWirelessNetwork);
		startActivity(i);
		return;
	}
}
