package com.ldimitrov.burnmessenger.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ldimitrov.burnmessenger.util.FileHelper;
import com.ldimitrov.burnmessenger.util.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class RecepientsActivity extends ListActivity {
	public static final String TAG = RecepientsActivity.class.getSimpleName();

	protected List<ParseUser> mFriends;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected MenuItem mSendMenuItem;
	protected Uri mMediaUri;
	protected String mFileType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_recepients);
		setupActionBar();
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mMediaUri = getIntent().getData();
		mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);
		
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
		mSendMenuItem = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_send:
			ParseObject message = createMessage();
			if(message == null) {
				//error
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.error_selecting_file)
					.setTitle(R.string.error_selecting_file_title)
					.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
				
			}
			else {
				send(message);
				//go back to previous activity in the stack - MainActivity in this case
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	

	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		if(list.getCheckedItemCount() > 0){
			mSendMenuItem.setVisible(true);
		}
		else {
			mSendMenuItem.setVisible(false);
		}
	}
	
	protected ParseObject createMessage(){
		ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
		message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
		message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
		message.put(ParseConstants.KEY_FILE_TYPE, mFileType);
		
		byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
		
		if(fileBytes == null){
			return null;
		}
		else {
			if(mFileType.equals(ParseConstants.TYPE_IMAGE)){
				fileBytes = FileHelper.reduceImageForUpload(fileBytes);
			}
			
			String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
			ParseFile file = new ParseFile(fileName, fileBytes);
			message.put(ParseConstants.KEY_FILE, file);
			
			return message;
		}
	}
	
	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		for(int i = 0; i < getListView().getCount(); i++) {
			if(getListView().isItemChecked(i)){
				recipientIds.add(mFriends.get(i).getObjectId());
			}
		}
		return recipientIds;
	}
	
	protected void send(ParseObject message) {
		message.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e == null){
					//success
					Toast.makeText(RecepientsActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
				}
				else {
					AlertDialog.Builder builder = new AlertDialog.Builder(RecepientsActivity.this);
					builder.setMessage(R.string.error_sending_message)
						.setTitle(R.string.error_selecting_file_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}
}
