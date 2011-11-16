package es.pulimento.wifi;

import java.io.Serializable;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity  extends Activity implements Serializable {
	// TODO: Retrieve what to show through an intent extra.

	private static final long serialVersionUID = 8084640600387669464L;

	private WebView mWebView;
	static boolean readme;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		if(readme)
			abreREADME();
		else
			abreGPL();
	}

	public void abreREADME(){
		mWebView.loadUrl(getString(R.string.about_readme_url));
	}

	public void abreGPL(){
		mWebView.loadUrl("file:///android_asset/LEGAL.htm");
	}
}