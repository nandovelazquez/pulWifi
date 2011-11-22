package es.pulimento.wifi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.pulimento.wifi.R;
import es.pulimento.wifi.core.WirelessNetwork;
import es.pulimento.wifi.dialogs.AboutDialog;
import es.pulimento.wifi.dialogs.ClipboardCopyDialog;
import es.pulimento.wifi.dialogs.ShowPasswordsDialog;

public class ShowPass extends Activity implements OnClickListener {

	public static final String EXTRA_NETWORK = "EXTRA_WIRELESS_NETWORK";

	private Context mContext;
	private WirelessNetwork mWirelessNetwork;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_showpass);

		mContext = this;
		mWirelessNetwork = this.getIntent().getExtras().getParcelable(EXTRA_NETWORK);

		((TextView) findViewById(R.id.layout_showpass_essid)).setText(mWirelessNetwork.getEssid());
		((TextView) findViewById(R.id.layout_showpass_bssid)).setText(mWirelessNetwork.getBssid());

		((Button) findViewById(R.id.layout_showpass_back)).setOnClickListener(this);
		((Button) findViewById(R.id.layout_showpass_clipboard)).setOnClickListener(this);
		((Button) findViewById(R.id.layout_showpass_show)).setOnClickListener(this);

		if(mWirelessNetwork.getPasswords().get(0).equals("NOPASSNOPASSNOPASSNOPASS")) {
			Toast.makeText(mContext, R.string.showpass_nopass, Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			this.finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.layout_showpass_back:
			this.finish();
			break;
		case R.id.layout_showpass_show:
			(new ShowPasswordsDialog(mContext, mWirelessNetwork.getPasswords())).show();
			break;
		case R.id.layout_showpass_clipboard:
			(new ClipboardCopyDialog(mContext, mWirelessNetwork.getPasswords())).show();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_generic, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			(new AboutDialog(mContext)).show();
			return true;
		case R.id.menu_quit:
			this.finish();
			return true;
		}
		return false;
	}
}
