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

package es.pulimento.wifi.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import es.pulimento.wifi.R;
import es.pulimento.wifi.ui.dialogs.ChangelogDialog;
import es.pulimento.wifi.ui.dialogs.LicenseDialog;
import es.pulimento.wifi.ui.dialogs.ThirdPartyLicensesDialog;

public class AboutActivity extends SherlockActivity implements OnClickListener {

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_aboutactivity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_about_back:
				this.finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
