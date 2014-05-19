package com.ldimitrov.burnmessenger.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.ldimitrov.burnmessenger.adapters.MessageAdapter;
import com.ldimitrov.burnmessenger.util.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends ListFragment {

    protected List<ParseObject> mMessages;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorScheme(R.color.swipeRefresh1,
                R.color.swipeRefresh2,
                R.color.swipeRefresh3,
                R.color.swipeRefresh4);

        //disable screenshot ability
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setProgressBarIndeterminateVisibility(true);

        retrieveMessages();
    }

    private void retrieveMessages() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if (e == null) {
                    // We found messages!
                    mMessages = messages;

                    String[] msgs = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        msgs[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;
                    }
                    //check if adapter is empty, if not created it
                    // this saves creating the adapter every time
                    if (getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(
                                getListView().getContext(),
                                mMessages);
                        setListAdapter(adapter);
                    } else {
                        //refill adapter
                        ((MessageAdapter) getListView().getAdapter()).refill(mMessages);
                    }
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView list, View view, final int position, long id) {
        super.onListItemClick(list, view, position, id);
        //TODO - start timer once the image or video is downloaded
        AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
        builder.setTitle(R.string.self_destruct_title);
        builder.setMessage(R.string.message_destruct_label);
        builder.setPositiveButton(R.string.button_proceed, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	
                ParseObject message = mMessages.get(position);
                String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
                ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
                String senderName = message.getString(ParseConstants.KEY_SENDER_NAME);
                String displayMessage = message.getString(ParseConstants.KEY_MESSAGE);
                if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
                    Uri fileUri = Uri.parse(file.getUrl());
                    Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                    intent.setData(fileUri);
                    intent.putExtra(ParseConstants.KEY_SENDER_NAME, senderName);
                    startActivity(intent);
                } else if (messageType.equals(ParseConstants.TYPE_VIDEO)) {
                    Uri fileUri = Uri.parse(file.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                    intent.setDataAndType(fileUri, "video/*");
                    startActivity(intent);
                } else if (messageType.equals(ParseConstants.TYPE_TEXT)){
                	Intent messageIntent = new Intent(getActivity(), ViewMessageActivity.class);
                	messageIntent.putExtra(ParseConstants.KEY_MESSAGE, displayMessage);
                	messageIntent.putExtra(ParseConstants.KEY_SENDER_NAME, senderName);
                	startActivity(messageIntent);                	
                	
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Message from: " + senderName);
                    builder.setMessage(displayMessage);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    AlertDialog messageDialog = builder.create();
                    messageDialog.show();*/
                }


                // Delete message
                List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
                // check count of recipients:
                // - if there is only one - delete the whole message
                // - if there is more than one - remove the recipient id only and save change - so other recipients can still view the message.

                if (ids.size() == 1) {
                    // last recipient - delete the whole message!
                    message.deleteInBackground();
                } else {
                    // remove the current recipient only and save
                    ids.remove(ParseUser.getCurrentUser().getObjectId());

                    ArrayList<String> idsToRemove = new ArrayList<String>();
                    idsToRemove.add(ParseUser.getCurrentUser().getObjectId());

                    message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
                    //TODO - delete files from Parse
                    message.saveInBackground();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            retrieveMessages();
        }
    };
}
