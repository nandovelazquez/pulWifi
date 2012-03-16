package es.pulimento.wifi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import es.pulimento.wifi.R;
import es.pulimento.wifi.core.WirelessNetwork;

public class ManualFragment extends Fragment {

	private EditText mEditTextBssid;
	private EditText mEditTextEssid;
	private Button mButtonAccept;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/* create view and return it */
		View inflatedView = inflater.inflate(R.layout.layout_manualfragment, container, false);
		mEditTextEssid = (EditText) inflatedView.findViewById(R.id.layout_manualcrack_essid);
		mEditTextBssid = (EditText) inflatedView.findViewById(R.id.layout_manualcrack_bssid);
		mButtonAccept = (Button) inflatedView.findViewById(R.id.layout_manualcrack_accept);
		return inflatedView;
	}

	@Override
	public void onStart() {
		super.onStart();
		mButtonAccept.setOnClickListener(new View.OnClickListener() {

			private WirelessNetwork mWirelessNetwork;

			@Override
			public void onClick(View v) {
				mWirelessNetwork = new WirelessNetwork(mEditTextEssid.getText().toString(), mEditTextBssid.getText()
						.toString(), 0, "[WEP] [WPA]");
				Log.i("pulWifi", "ManualCrack onClick(), created -> " + mWirelessNetwork.toString());
				if (!mWirelessNetwork.isCrackeable()) {
					Toast.makeText(getActivity().getApplicationContext(), R.string.manualcrack_inputerror,
							Toast.LENGTH_LONG).show();
					return;
				}
				mWirelessNetwork.crack();
				Intent i = new Intent(getActivity().getApplicationContext(), ShowPassActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra(ShowPassActivity.EXTRA_NETWORK, mWirelessNetwork);
				startActivity(i);
				return;
			}
		});

	}
}
