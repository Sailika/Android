package com.pcs.sleep.test;

import org.json.JSONObject;

import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.TextView;

import com.pcs.sleep.MainActivity;

public class AsyncTastTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity activity;
	private TextView id;
	private TextView name, website, link, likes;
	private Button download;
	private int actualID, actualLikes;
	private String actualName, actualWebsite, actuallink;
	private JSONObject object;

	public AsyncTastTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		assertNotNull(activity);// Check if Activity exists
		// Getting the Views
		id = (TextView) activity.findViewById(com.pcs.sleep.R.id.id);
		name = (TextView) activity.findViewById(com.pcs.sleep.R.id.name);
		website = (TextView) activity.findViewById(com.pcs.sleep.R.id.web);
		link = (TextView) activity.findViewById(com.pcs.sleep.R.id.link);
		likes = (TextView) activity.findViewById(com.pcs.sleep.R.id.like);

		download = (Button) activity.findViewById(com.pcs.sleep.R.id.download);

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {

		assertNotNull("Failed getting activity context", getActivity());
		assertNotNull("Can't find a view... Has layout changed?", id);
		assertNotNull("Can't find a view... Has layout changed?", name);
		assertNotNull("Can't find a view... Has layout changed?", website);
		assertNotNull("Can't find a view... Has layout changed?", link);
		assertNotNull("Can't find a view... Has layout changed?", likes);
		assertNotNull("Can't find a view... Has layout changed?", download);

	}

	public void testButton() {
		TouchUtils.clickView(AsyncTastTest.this, download);
		SystemClock.sleep(5000);
		assertNotNull(id);
	}

	public void testCheckID() {
		TouchUtils.clickView(AsyncTastTest.this, download);
		SystemClock.sleep(5000);

		actualID = Integer.parseInt(id.getText().toString());
		assertEquals(192930, actualID);

	}

	public void testCheckName() {
		TouchUtils.clickView(AsyncTastTest.this, download);
		SystemClock.sleep(5000);
		actualName = name.getText().toString();
		assertEquals("Sailika", actualName);
	}

	public void testCheckLink() {
		TouchUtils.clickView(AsyncTastTest.this, download);
		SystemClock.sleep(5000);
		actuallink = link.getText().toString();
		assertEquals("https://www.facebook.com/FacebookDevelopers", actuallink);

	}

	public void testCheckLikes() {
		TouchUtils.clickView(AsyncTastTest.this, download);
		SystemClock.sleep(5000);
		actualLikes = Integer.parseInt(likes.getText().toString());
		assertEquals(12, actualLikes);

	}

	public void testCheckWeb() {
		TouchUtils.clickView(AsyncTastTest.this, download);
		SystemClock.sleep(6000);
		actualWebsite = website.getText().toString();
		assertEquals("SAilika", actualWebsite);

	}
}
