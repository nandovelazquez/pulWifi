/*
 *  pulWifi , Copyright (C) 2011-2012 Javi Pulido / Antonio V‡zquez
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

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.pulimento.wifi.R;

@SuppressWarnings("deprecation")
public class ClipboardCopyDialog extends Dialog implements OnItemClickListener {

	private ClipboardManager mClipboardManager;
	private ArrayList<String> mPasswords;
	private Context mContext;
	private ListView mListView;

	public ClipboardCopyDialog(Context context, ArrayList<String> passwords) {
		super(context);

		// Set content...
		this.setTitle(R.string.dialog_cipboardcopy_title);
		this.setContentView(R.layout.dialog_clipboardcopy);

		// Define elements...
		mContext = context;
		mPasswords = passwords;
		mClipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
		mListView = (ListView) findViewById(R.id.dialog_clipboardcopy_list);

		// Set adapters...
		mListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPasswords));

		// Listeners...
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String key = ((TextView) view).getText().toString();
		mClipboardManager.setText(key);
		Toast.makeText(mContext, mContext.getString(R.string.showpass_toclipboard) + " (" + key + ")", Toast.LENGTH_LONG).show();
		Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
}