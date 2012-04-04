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
