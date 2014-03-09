package com.ldimitrov.burnmessenger;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecepientsActivity extends ListActivity {
	public static final String TAG = RecepientsActivity.class.getSimpleName();

	protected List<ParseUser> mFriends;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_recepients);
		setupActionBar();
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		//add progress bar from MainActivity
		setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);

		mFriendsRelation.getQuery().findInBackground(
				new FindCallback<ParseUser>() {
					@Override
					public void done(List<ParseUser> friends, ParseException e) {
						setProgressBarIndeterminateVisibility(false);
						if(e == null){
							mFriends = friends;
							// use mFriends as data source for the list view of the
							// fragment - adapter
							String[] usernames = new String[mFriends.size()];
							int i = 0;
							for (ParseUser user : mFriends) {
								usernames[i] = user.getUsername();
								i++;
							}
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									getListView().getContext(),
									android.R.layout.simple_list_item_checked,
									usernames);
							setListAdapter(adapter);
						}					
						else
						{
							Log.e(TAG, e.getMessage());
							AlertDialog.Builder builder = new AlertDialog.Builder(RecepientsActivity.this);
							builder.setMessage(e.getMessage())
								.setTitle(R.string.error_title)
								.setPositiveButton(android.R.string.ok, null);
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					}
				});
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recepients, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
