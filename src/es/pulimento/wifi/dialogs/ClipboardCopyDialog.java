package es.pulimento.wifi.dialogs;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(mPasswords.size() == 1) {
			mClipboardManager.setText(mPasswords.get(0));
			Toast.makeText(mContext, mContext.getString(R.string.showpass_toclipboard)+"( "+mPasswords.get(0)+" )", Toast.LENGTH_LONG).show();
			Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(i);
			this.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String key = ((TextView)view).getText().toString();
		mClipboardManager.setText(key);
		Toast.makeText(mContext, mContext.getString(R.string.showpass_toclipboard)+"( "+key+" )", Toast.LENGTH_LONG).show();
		Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
}
