package es.pulimento.wifi.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import es.pulimento.wifi.R;

public class ChangelogDialog extends Dialog {

	private Context mContext;

	public ChangelogDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_generic);
		this.setTitle(R.string.dialog_changelog_title);

		((WebView) findViewById(R.id.dialog_generic_webview)).loadUrl(mContext.getString(R.string.dialog_changelog_file_url));
	}
}