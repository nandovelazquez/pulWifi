package es.pulimento.wifi.ui;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import es.pulimento.wifi.R;

public class MainTabHost extends TabActivity{

	/* Tab host to be populated. */
	private TabHost mTabHost;
	/* Application context. */
	private Context mContext;
	/* Reusable tab specification. */
	private TabSpec mTabSpec;
	/* Reusable intent. */
	private Intent mIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Simplify this by using resources instead of code for tab tittle setting.
		// TODO: Fragments+Pager: http://developer.android.com/resources/samples/Support4Demos/src/com/example/android/supportv4/app/FragmentTabsPager.html

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_maintabhost);

		mContext = this;
		mTabHost = ((TabHost) findViewById(android.R.id.tabhost));

		/* Tab host properties. */
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		/* Set tab number 1. */
		mIntent = new Intent().setClass(this, SelectWirelessNetwork.class);
		View view = LayoutInflater.from(mContext).inflate(R.layout.layout_maintabhost_tabbg, null);
		TextView title = (TextView)view.findViewById(R.id.layout_maintabhost_tabbg_title);
		title.setText(getString(R.string.maintabhost_mode_scan));
		mTabSpec = mTabHost.newTabSpec("ScanModeEnabled").setIndicator(view).setContent(mIntent);
		mTabHost.addTab(mTabSpec);

		/* Set tab number 2. */
		mIntent = new Intent().setClass(this, ManualCrack.class);
		View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_maintabhost_tabbg, null);
		TextView title2 = (TextView)view2.findViewById(R.id.layout_maintabhost_tabbg_title);
		title2.setText(getString(R.string.maintabhost_mode_manual));
		mTabSpec = mTabHost.newTabSpec("ManualModeEnabled").setIndicator(view2).setContent(mIntent);
		mTabHost.addTab(mTabSpec);

		/* Set active tab. */
		mTabHost.setCurrentTab(0);
	}
}