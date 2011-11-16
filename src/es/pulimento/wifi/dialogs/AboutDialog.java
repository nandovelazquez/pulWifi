package es.pulimento.wifi.dialogs;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import es.pulimento.wifi.R;

public class AboutDialog extends Dialog implements OnClickListener {

	private Context mContext;

	public AboutDialog(Context context) {
		super(context);

		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the content for our dialog...
		this.setContentView(R.layout.dialog_about);

		// Make links clickable.
		((TextView) findViewById(R.id.dialog_about_text_id)).setMovementMethod(LinkMovementMethod.getInstance());

		// Set click listeners...
		((Button)findViewById(R.id.dialog_about_license_button_id)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.dialog_about_license_button_id:
			(new LicenseDialog(mContext)).show();
			break;
		}
	}
}