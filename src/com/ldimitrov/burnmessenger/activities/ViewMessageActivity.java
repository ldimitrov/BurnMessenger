package com.ldimitrov.burnmessenger.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.ldimitrov.burnmessenger.util.ParseConstants;

public class ViewMessageActivity extends Activity {

	protected MenuItem mSendMenuItem;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_view_message);

		setupActionBar();
		String senderName = getIntent().getStringExtra(ParseConstants.KEY_SENDER_NAME);
		String message = getIntent().getStringExtra(ParseConstants.KEY_MESSAGE);
		// String senderName =
		// getIntent().getExtras().getString(ParseConstants.KEY_SENDER_NAME);
		// String message =
		// getIntent().getExtras().getString(ParseConstants.KEY_MESSAGE);
		setTitle("Message from " + senderName);
		TextView messageText = (TextView) findViewById(R.id.message_text);
		messageText.setText(message);

		
		final TextView countDownTimer = (TextView)findViewById(R.id.countdown);

		// TODO - this is not the proper place to set timer for messages.
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// after 1 minute, finish this activity here and go back to
				// parent activity
				finish();
			}
		}, 6 * 10 * 1000);

		new CountDownTimer(6 * 10 * 1000, 1000) {
			public void onTick(long millisUntilFinished) {
				countDownTimer.setText("Self-destruction in: " + (millisUntilFinished / 1000));
			}

			public void onFinish() {
				Toast.makeText(ViewMessageActivity.this, R.string.self_destructed_toast, Toast.LENGTH_LONG).show();
			}
		}.start();

	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipients, menu);
		mSendMenuItem = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}