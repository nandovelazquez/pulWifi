package es.pulimento.wifi.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import es.pulimento.wifi.R;
import es.pulimento.wifi.ui.dialogs.SupportedNetworksDialog;
import es.pulimento.wifi.ui.dialogs.UpdateDialog;
import es.pulimento.wifi.ui.utils.ActionBarActivity;
import es.pulimento.wifi.ui.utils.PagerHeader;

public class HomeActivity extends ActionBarActivity {

	private ViewPager mPager;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* set layout */
		setContentView(R.layout.layout_homeactivity);

		mContext = getApplicationContext();

		/* create a viewpager and add two pages to it */
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));
		PagerHeader pagerHeader = (PagerHeader) findViewById(R.id.pager_header);
		PagerAdapter pagerAdapter = new PagerAdapter(this, mPager, pagerHeader);

		pagerAdapter.addPage(SelectWirelessNetworkFragment.class, R.string.page_label_networks_list);
		pagerAdapter.addPage(ManualFragment.class, R.string.page_label_manual);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_homeactivity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_share:
				/* only applicable to HoneyComb & above */
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.menu_share_subject));
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.menu_share_text));
				startActivity(Intent.createChooser(shareIntent, getString(R.string.menu_share_title)));
				break;
			/*
			 * case R.id.menu_about:
			 * (new AboutDialog(this)).show();
			 * break;
			 */
			case R.id.menu_quit:
				this.finish();
				break;
			case R.id.menu_networks:
				(new SupportedNetworksDialog(this)).show();
				break;
			case R.id.menu_settings:
				startActivity(new Intent(mContext, Preferences.class));
				break;
			case R.id.menu_updater:
				(new UpdateDialog(this)).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/* PagerAdapter from SuperUser App, (c) 2011 Adam Shanks (ChainsDD) */
	public static class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener,
			PagerHeader.OnHeaderClickListener {

		private final Context mContext;
		private final ViewPager mPager;
		private final PagerHeader mHeader;
		private final ArrayList<PageInfo> mPages = new ArrayList<PageInfo>();

		static final class PageInfo {
			private final Class<?> clss;
			private final Bundle args;

			PageInfo(Class<?> _clss, Bundle _args) {
				clss = _clss;
				args = _args;
			}
		}

		public PagerAdapter(FragmentActivity activity, ViewPager pager, PagerHeader header) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mPager = pager;
			mHeader = header;
			mHeader.setOnHeaderClickListener(this);
			mPager.setAdapter(this);
			mPager.setOnPageChangeListener(this);
		}

		public void addPage(Class<?> clss, int res) {
			addPage(clss, null, res);
		}

		public void addPage(Class<?> clss, String title) {
			addPage(clss, null, title);
		}

		public void addPage(Class<?> clss, Bundle args, int res) {
			addPage(clss, null, mContext.getResources().getString(res));
		}

		public void addPage(Class<?> clss, Bundle args, String title) {
			PageInfo info = new PageInfo(clss, args);
			mPages.add(info);
			mHeader.add(0, title);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mPages.size();
		}

		@Override
		public Fragment getItem(int position) {
			PageInfo info = mPages.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			mHeader.setPosition(position, positionOffset, positionOffsetPixels);
		}

		@Override
		public void onPageSelected(int position) {
			mHeader.setDisplayedPage(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onHeaderClicked(int position) {

		}

		@Override
		public void onHeaderSelected(int position) {
			mPager.setCurrentItem(position);
		}
	}
}