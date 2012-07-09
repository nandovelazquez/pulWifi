package es.pulimento.wifi.ui.utils;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionHandler implements UncaughtExceptionHandler {

	// Constants.
	private static String PACKAGE = "es.pulimento.wifi";

	// Variables.
	private UncaughtExceptionHandler mDefaultHandler;

	public ExceptionHandler() {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {

		/*
		 * TODO:
		 * Report to Github project issues through API v3.
		 */

		/* Let Android manage the exception... */
		mDefaultHandler.uncaughtException(t, e);
	}

	public String getFileName(Throwable e) {
		for(StackTraceElement s : e.getCause().getStackTrace())
			if(PACKAGE.equals(s.getClassName().substring(0, PACKAGE.length())))
				return s.getClassName().substring(s.getClassName().lastIndexOf('.')+1);
		return "Unknown";
	}
}
