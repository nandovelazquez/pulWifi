package es.pulimento.wifi.ui.utils;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private Activity mActivity;
	private UncaughtExceptionHandler mDefaultHandler;

	public ExceptionHandler(Activity activity) {
		mActivity = activity;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {

		/*
         * Send an email.
         */
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		i.setType("plain/text");
		i.putExtra(android.content.Intent.EXTRA_EMAIL, "pulwifi@gmail.com");
		i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Crash Report");
		i.putExtra(android.content.Intent.EXTRA_TEXT, "Comments:\n- Add any comment here if...\n\n\nStack trace:\n"+Log.getStackTraceString(e));

		try {
			mActivity.startActivity(Intent.createChooser(i, "Send report by email..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(mActivity, "There are no email clients installed...", Toast.LENGTH_LONG).show();
		}

		/* Let Android manage the exception... */
		mDefaultHandler.uncaughtException(t, e);
	}
}
