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

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import es.pulimento.wifi.R;

public class ShowPasswordsDialog extends Dialog {

	TextView mTextView;

	public ShowPasswordsDialog(Context context, List<String> passwords) {
		super(context);

		// Set UI
		this.setContentView(R.layout.dialog_showpasswords);

		mTextView = (TextView) findViewById(R.id.dialog_showpasswords_list);

		for (String p : passwords)
			mTextView.append(p + "\n");

		if (passwords.size() == 1)
			this.setTitle(R.string.dialog_showpass_title_one);
		else
			this.setTitle(R.string.dialog_showpass_title_many);
	}
}