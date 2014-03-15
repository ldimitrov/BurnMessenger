package com.ldimitrov.burnmessenger.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ldimitrov.burnmessenger.util.ParseConstants;

public class SendMessageActivity extends Activity {

    protected MenuItem mSendMenuItem;
    protected Button mMessageButton;
    protected EditText mMessage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        setupActionBar();

        mMessageButton = (Button)findViewById(R.id.send_button);
        mMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessage = (EditText)findViewById(R.id.edit_text_message);
                String messageValue = mMessage.getText().toString();
                if(TextUtils.isEmpty(messageValue)){
                    Toast.makeText(SendMessageActivity.this, "Enter a message first!", Toast.LENGTH_LONG).show();
                } else {
                    Intent recipientsIntent = new Intent(SendMessageActivity.this, RecipientsActivity.class);
                    recipientsIntent.putExtra(ParseConstants.KEY_MESSAGE, messageValue);
                    recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, ParseConstants.TYPE_TEXT);
                    startActivity(recipientsIntent);
                    finish();
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