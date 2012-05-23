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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import es.pulimento.wifi.R;

public class UpdateDialog extends AlertDialog implements OnClickListener {

	private String mUri;
	private Context mContext;

	public UpdateDialog(Context context, String uri) {
		super(context);

		mUri = uri;
		mContext = context;

		this.setCancelable(false);
		this.setTitle(R.string.dialog_udpater_title);
		this.setMessage(mContext.getText(R.string.dialog_udpater_message));
		this.setButton(BUTTON_POSITIVE, mContext.getText(R.string.dialog_updater_positive), this);
		this.setButton(BUTTON_NEGATIVE, mContext.getText(R.string.dialog_updater_negative), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		this.dismiss();
		switch(which) {
		case BUTTON_POSITIVE:
			mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mUri)));
			break;
		case BUTTON_NEGATIVE:
			this.dismiss();
			break;
		}
	}
}