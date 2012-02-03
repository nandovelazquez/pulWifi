package es.pulimento.wifi.dialogs;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import es.pulimento.wifi.R;

public class ShowPasswordsDialog extends Dialog {

	TextView mTextView;

	public ShowPasswordsDialog(Context context, List<String> passwords) {
		super(context);
		
		//Set UI
		this.setContentView(R.layout.dialog_showpasswords);
		
		mTextView = (TextView) findViewById(R.id.dialog_showpasswords_list);

		for(String p : passwords)
			mTextView.append(p+"\n");

		//this.setContentView(mTextView);
		if(passwords.size() == 1)
			this.setTitle(R.string.dialog_showpass_title_one);
		else
			this.setTitle(R.string.dialog_showpass_title_many);
	}
}