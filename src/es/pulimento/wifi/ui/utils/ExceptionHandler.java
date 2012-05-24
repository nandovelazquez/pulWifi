package es.pulimento.wifi.ui.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private Context mContext;
	private UncaughtExceptionHandler mDefaultExceptionHandler;

	public ExceptionHandler(Context context) {
		mContext = context;
		mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		/*
		 * Get stack trace...
		 */
		Writer result = new StringWriter();
        e.printStackTrace(new PrintWriter(result));
        String stacktrace = result.toString();

        /*
         * Send an email.
         */
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		i.setType("plain/text");
		i.putExtra(android.content.Intent.EXTRA_EMAIL, "pulwifi@gmail.com");
		i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Crash Report");
		i.putExtra(android.content.Intent.EXTRA_TEXT, "Comments:\n- Add any comment here if...\n\n\nStack trace:\n"+stacktrace);
		try {
		    mContext.startActivity(Intent.createChooser(i, "Send report by email..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(mContext, "There are no email clients installed...", Toast.LENGTH_LONG).show();
		}
		mDefaultExceptionHandler.uncaughtException(t, e);
	}

}
