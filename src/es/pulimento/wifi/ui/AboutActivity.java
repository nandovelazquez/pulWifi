package es.pulimento.wifi.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import es.pulimento.wifi.R;
import es.pulimento.wifi.ui.dialogs.ChangelogDialog;
import es.pulimento.wifi.ui.dialogs.LicenseDialog;
import es.pulimento.wifi.ui.dialogs.ThirdPartyLicensesDialog;

public class AboutActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_about);

		// Make links clickable
		((TextView) findViewById(R.id.act_about_text)).setMovementMethod(LinkMovementMethod.getInstance());

		// Set click listeners ...
		((Button) findViewById(R.id.act_about_license_button)).setOnClickListener(this);
		((Button) findViewById(R.id.act_about_changelog_button)).setOnClickListener(this);
		((Button) findViewById(R.id.act_about_open_source_licenses)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.act_about_license_button:
				(new LicenseDialog(AboutActivity.this)).show();
				break;
			case R.id.act_about_changelog_button:
				(new ChangelogDialog(AboutActivity.this)).show();
				break;
			case R.id.act_about_open_source_licenses:
				(new ThirdPartyLicensesDialog(AboutActivity.this)).show();
				break;
		}
	}

}
