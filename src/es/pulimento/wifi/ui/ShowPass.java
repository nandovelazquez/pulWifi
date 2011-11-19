package es.pulimento.wifi.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.pulimento.wifi.R;
import es.pulimento.wifi.core.WirelessNetwork;
import es.pulimento.wifi.dialogs.AboutDialog;
import es.pulimento.wifi.dialogs.ShowPasswordsDialog;

public class ShowPass extends Activity implements OnClickListener {

	public static final String EXTRA_NETWORK = "EXTRA_WIRELESS_NETWORK";

	private Context mContext;
	private ClipboardManager mClipboardManager;
	private WirelessNetwork mWirelessNetwork;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpass);

		mContext = this;
		mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		mWirelessNetwork = this.getIntent().getExtras().getParcelable(EXTRA_NETWORK);

		((TextView) findViewById(R.id.layout_showpass_essid)).setText(mWirelessNetwork.getEssid());
		((TextView) findViewById(R.id.layout_showpass_bssid)).setText(mWirelessNetwork.getBssid());

		((Button) findViewById(R.id.layout_showpass_back)).setOnClickListener(this);
		((Button) findViewById(R.id.layout_showpass_clipboard)).setOnClickListener(this);
		((Button) findViewById(R.id.layout_showpass_show)).setOnClickListener(this);

		if(mWirelessNetwork.getPasswords().get(0).equals("NOPASSNOPASSNOPASSNOPASS")) {
			Toast.makeText(mContext, R.string.showpass_network_nopass_toast, Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			this.finish();
		}
	}
	
	public void clipboard(String clave){
		mClipboardManager.setText(clave);
    	Toast.makeText(mContext, getString(R.string.showpass_toclipboard)+"( "+clave+" )", Toast.LENGTH_LONG).show();
		Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
		this.finish();
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
			List<String> claves = mWirelessNetwork.getPasswords();
			if(claves.size() == 1)
				mClipboardManager.setText(claves.get(0));
			else{
				AlertDialog.Builder builder = new Builder(ShowPass.this);
				builder.setTitle(mWirelessNetwork.getEssid());
			    LayoutInflater inflater = (LayoutInflater) ShowPass.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			    View layout = inflater.inflate(R.layout.results,
			                                   (ViewGroup) findViewById(R.id.layout_root));
			    ListView list = (ListView) layout.findViewById(R.id.list_keys);
				list.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String key = ((TextView)view).getText().toString();
						clipboard(key);
						//Toast.makeText(getApplicationContext(), key + " " + getString(R.string.msg_copied), Toast.LENGTH_SHORT).show();
						//clipboard.setText(key);
						//startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
					}
				});
				
				list.setAdapter(new ArrayAdapter<String>(ShowPass.this, android.R.layout.simple_list_item_1, claves));
				builder.setView(layout);
				Dialog d = builder.create();
				d.show();
				
			}
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
		case R.id.ACERCA_DE:
			(new AboutDialog(mContext)).show();
			return true;
		case R.id.SALIR:
			this.finish();
			return true;
		}
		return false;
	}
}
