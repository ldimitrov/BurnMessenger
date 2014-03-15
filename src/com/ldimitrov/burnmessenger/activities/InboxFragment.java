package com.ldimitrov.burnmessenger.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ldimitrov.burnmessenger.activities.MessageAdapter;
import com.ldimitrov.burnmessenger.util.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class InboxFragment extends ListFragment {

    protected List<ParseObject> mMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox,container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setProgressBarIndeterminateVisibility(true);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (e == null) {
                    // We found messages!
                    mMessages = messages;

                    String[] msgs = new String[mMessages.size()];
                    int i = 0;
                    for(ParseObject message : mMessages) {
                        msgs[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;
                    }
                    MessageAdapter adapter = new MessageAdapter(
                            getListView().getContext(),
                            mMessages);
                    setListAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        String senderName = message.getString(ParseConstants.KEY_SENDER_NAME);
        String displayMessage = message.getString(ParseConstants.KEY_MESSAGE);
        if(messageType.equals(ParseConstants.TYPE_IMAGE)) {
            Uri fileUri = Uri.parse(file.getUrl());
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(fileUri);
            startActivity(intent);
        } else if (messageType.equals(ParseConstants.TYPE_VIDEO)) {
            Uri fileUri = Uri.parse(file.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri, "video/*");
            startActivity(intent);
        } else {
            //TODO - add this to a separate activity
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Message from: " + senderName);
            builder.setMessage(displayMessage);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
