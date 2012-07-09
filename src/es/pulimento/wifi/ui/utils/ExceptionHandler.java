package es.pulimento.wifi.ui.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import es.pulimento.wifi.ui.utils.github.Issue;

import android.util.Log;

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

		Issue i = new Issue("Exception in "+getFileName(e), "TRACE:\n"+getStackTrace(e)+"\n\nCAUSE TRACE:\n"+getStackTrace(e.getCause()), "Automated Report");

		// Let Android manage the exception...
		mDefaultHandler.uncaughtException(t, e);
	}

	public String getFileName(Throwable e) {
		for(StackTraceElement s : e.getCause().getStackTrace())
			if(PACKAGE.equals(s.getClassName().substring(0, PACKAGE.length())))
				return s.getClassName().substring(s.getClassName().lastIndexOf('.')+1);
		return "Unknown";
	}

	public String getStackTrace(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}
}
