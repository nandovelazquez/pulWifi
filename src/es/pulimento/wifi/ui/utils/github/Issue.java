package es.pulimento.wifi.ui.utils.github;

import org.json.JSONArray;

public class Issue {

	private String mTitle;
	private String mBody;
	private JSONArray mLabels;

	public Issue() {
		mLabels = new JSONArray();
	}

	public Issue(String title, String body, String label) {
		mTitle = title;
		mBody = body;
		mLabels = new JSONArray();
		mLabels.put(label);
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String t) {
		mTitle = t;
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String b) {
		mBody = b;
	}

	public JSONArray getLabels() {
		return mLabels;
	}

	public void addLabel(String l) {
		mLabels.put(l);
	}	
}
