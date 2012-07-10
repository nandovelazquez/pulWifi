package es.pulimento.wifi.ui.utils.github;

import org.json.JSONException;
import org.json.JSONObject;

public class Download {

	private String mUrl;
	private String mVersion;

	public Download(JSONObject j) {
		try {
			mUrl = j.getString("html_url");
			mVersion = j.getString("description");
		} catch (JSONException e) {
			// Should not happen...
		}
	}

	public Download(String url, String version) {
		mUrl = url;
		mVersion = version;
	}

	public void setUrl(String u) {
		mUrl = u;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setVersion(String v) {
		mVersion = v;
	}

	public String getVersion() {
		return mVersion;
	}
}
