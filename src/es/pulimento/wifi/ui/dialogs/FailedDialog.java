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

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import es.pulimento.wifi.R;

public class FailedDialog extends AlertDialog implements OnClickListener {

	private WeakReference<Activity> mActivity;

	public FailedDialog(Context context, WeakReference<Activity> activity) {
		super(context);
		mActivity = activity;
		this.setTitle(R.string.mainactivity_failed_dialog_error);
		this.setMessage(context.getString(R.string.mainactivity_failed_dialog_msg));
		this.setButton(BUTTON_NEUTRAL, context.getString(R.string.mainactivity_failed_dialog_ok_button), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == BUTTON_NEUTRAL)
			mActivity.get().finish();
	}

}
