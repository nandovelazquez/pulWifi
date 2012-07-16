package es.pulimento.wifi.ui.utils.github;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;

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

	public String toJSONString() {
		//return "{ \"title\": \""+mTitle+"\", \"body\": \""+mBody+"\", \"labels\": "+mLabels.toString()+"}";
		try {
			return
				new JSONStringer()
					.object()
						.key("title")
						.value(mTitle)
						.key("body")
						.value(mBody)
						.key("labels")
						.value(mLabels)
					.endObject()
				.toString();
		} catch (JSONException e) {
			// Sould not happen...
		}
		return null;
	}
}
