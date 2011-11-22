package es.pulimento.wifi.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import es.pulimento.wifi.R;

public class SupportedNetworksDialog extends Dialog {

	public SupportedNetworksDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.dialog_supportednetworks);
		this.setTitle(R.string.dialog_supportednetworks_title);
	}
}
