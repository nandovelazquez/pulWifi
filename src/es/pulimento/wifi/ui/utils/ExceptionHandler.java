package es.pulimento.wifi.ui.utils;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler mDefaultHandler;

	public ExceptionHandler() {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {

		/*
		 * TODO:
		 * Report error to our server or mail.
		 */

		/* Let Android manage the exception... */
		mDefaultHandler.uncaughtException(t, e);
	}
}
