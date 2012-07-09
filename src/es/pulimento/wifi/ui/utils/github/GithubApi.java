package es.pulimento.wifi.ui.utils.github;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.json.JSONException;

import android.util.Log;

public class GithubApi {

	// Constants...
	private static String BASEURL = "https://api.github.com/repos/pulWifi/pulWifi/";

	// Variables...
	private Boolean mAuthed;

	public GithubApi() {
		mAuthed = false;
	}

	public Download getLastDownload() {
		try {
			URL url = new URL(BASEURL+"downloads");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = in.readLine()) != null)
				sb.append(s);
			in.close();
			return new Download((new JSONArray(sb.toString())).getJSONObject(0));
		} catch (MalformedURLException e) {
			// Never going to happen...
		} catch (IOException e) {
			// No internet...
		} catch (JSONException e) {
			// Error in JSON...
		}
		
		return null;
	}

	/**
	 * TODO Still needing an authentication system.
	 * @param i Issue to post in Github.
	 */
	public void reportIssue(Issue i) {
		if(!mAuthed)
			return;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("title", i.getTitle()));
		nameValuePairs.add(new BasicNameValuePair("body", i.getBody()));
		nameValuePairs.add(new BasicNameValuePair("labels", i.getLabels().toString()));

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(BASEURL+"issues");
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			Log.v("ASD", EntityUtils.getContentCharSet(response.getEntity()));
		} catch (UnsupportedEncodingException e1) {
			// Nothing to be done...
			Log.v("ASD", "EX");
		} catch (ClientProtocolException e1) {
			// Nothing to be done...
			Log.v("ASD", "EX");
		} catch (IOException e1) {
			// Nothing to be done...
			Log.v("ASD", "EX");
		}
	}
}
