package es.pulimento.wifi.dialogs;

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

		this.setContentView(R.layout.dialog_license);

		((WebView)findViewById(R.id.dialog_license_webview)).loadUrl(mContext.getString(R.string.dialog_license_file_url));
	}
}
