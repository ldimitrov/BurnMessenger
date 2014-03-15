package com.ldimitrov.burnmessenger.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class SendMessageActivity extends Activity {

    protected MenuItem mSendMenuItem;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        setupActionBar();
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


    /*final Dialog messageDialog = new Dialog(this);
    messageDialog.setContentView(R.layout.message_bar);
    messageDialog.setTitle(R.string.send_message_title);
    messageDialog.show();

    Button messageButton = (Button) messageDialog.findViewById(R.id.send_button);
    messageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText messageText = (EditText) messageDialog.findViewById(R.id.edit_text_message);
            final String messageValue = messageText.getText().toString();
            if (TextUtils.isEmpty(messageValue)) {
                Toast.makeText(MainActivity.this, "Enter a message first!", Toast.LENGTH_SHORT).show();
            } else {
                Intent recipientsIntent = new Intent(MainActivity.this, RecipientsActivity.class);
                recipientsIntent.putExtra(ParseConstants.KEY_MESSAGE, messageValue);
                recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, ParseConstants.TYPE_TEXT);
                startActivity(recipientsIntent);
                messageDialog.hide();
            }
        }
    });*/
