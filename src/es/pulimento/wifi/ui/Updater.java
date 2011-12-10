package es.pulimento.wifi.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.pulimento.wifi.R;

public class Updater extends Activity implements OnClickListener {

	private Context mContext;
	private TextView mLatestVersion;
	private XMLReader mXmlReader;

	private final String VERSION_URL = "https://raw.github.com/pulWifi/pulWifi/master/res/values/strings.xml";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_updater);

		mContext = this;
		mLatestVersion = (TextView) findViewById(R.id.layout_updater_latest_version);
		try {
			mXmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		} catch (ParserConfigurationException e) {
			// TODO: Manage...
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO: Manage...
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO: Manage...
			e.printStackTrace();
		}

		((Button) findViewById(R.id.layout_updater_check)).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.layout_updater_check:
			try {
				VersionHandler mVersionHandler = new VersionHandler();
				InputSource dis = new InputSource(new URL(VERSION_URL).openConnection().getInputStream());
				mXmlReader.setContentHandler(mVersionHandler);
				mXmlReader.parse(dis);
				mLatestVersion.setText(mVersionHandler.getVersion());
			} catch (MalformedURLException e) {
				// TODO: Manage...
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: Manage...
				e.printStackTrace();
				Toast.makeText(mContext, R.string.updater_error, Toast.LENGTH_LONG).show();
			} catch (SAXException e) {
				// TODO: Manage...
				e.printStackTrace();
			}
			break;
		}
	}

	public class VersionHandler extends DefaultHandler {
		
		private boolean bversion = false;
		private String mVersion;

		public String getVersion() {
			return mVersion;
		}

		@Override
        public void startDocument() throws SAXException {
			mVersion = "";
        }
 
        @Override
        public void endDocument() throws SAXException {
                // Nothing to do...
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        	if (localName.equals("string") && atts.getValue("name").equals("app_version")) {
        		bversion = true;
        	}
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        	if (localName.equals("string")) {
        		bversion = false;
        	}
        }

        @Override
        public void characters(char ch[], int start, int length) {
        	if(bversion)
        		mVersion = new String(ch, start, length);
        }
	}
}