package es.pulimento.wifi.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import es.pulimento.wifi.R;

public class LicenseDialog extends Dialog {

	private Context mContext;

	public LicenseDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_generic);
		this.setTitle(R.string.dialog_license_title);

		((WebView) findViewById(R.id.dialog_generic_webview)).loadUrl(mContext.getString(R.string.dialog_license_file_url));
	}
}