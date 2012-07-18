/*
 *  pulWifi , Copyright (C) 2011-2012 Javi Pulido / Antonio Vázquez
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

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import es.pulimento.wifi.R;

public class AboutDialog extends Dialog implements OnClickListener {

	private Context mContext;

	public AboutDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the content for our dialog...
		this.setContentView(R.layout.dialog_about);
		this.setTitle(R.string.dialog_about_title);

		// Make links clickable.
		((TextView) findViewById(R.id.dialog_about_text)).setMovementMethod(LinkMovementMethod.getInstance());

		// Set click listeners...
		((Button) findViewById(R.id.dialog_about_license_button)).setOnClickListener(this);
		((Button) findViewById(R.id.dialog_about_changelog_button)).setOnClickListener(this);
		((Button) findViewById(R.id.dialog_about_open_source_licenses)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.dialog_about_license_button:
				(new LicenseDialog(mContext)).show();
				break;
			case R.id.dialog_about_changelog_button:
				(new ChangelogDialog(mContext)).show();
				break;
			case R.id.dialog_about_open_source_licenses:
				(new ThirdPartyLicensesDialog(mContext)).show();
				break;
		}
	}
}