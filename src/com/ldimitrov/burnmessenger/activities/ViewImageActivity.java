package com.ldimitrov.burnmessenger.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ldimitrov.burnmessenger.util.ParseConstants;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class ViewImageActivity extends Activity {

    protected MenuItem mSendMenuItem;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_view_image);

        //disable screenshot ability
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setupActionBar();
        String senderName = getIntent().getExtras().getString(ParseConstants.KEY_SENDER_NAME);
        setTitle("Image from " + senderName);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Uri imageUri = getIntent().getData();
        // getting images from the web .. android does not support that by defaul - DAFUQ
        // use Picasso library instead to load a image directly to the imageView.
        // Amazing! :) synchronous, caches images
        //TODO set progress bar here
        //setProgressBarIndeterminateVisibility(true);
        Picasso.with(this).load(imageUri.toString()).into(imageView);
        //TODO - this is not the proper place to set timer for messages.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //after 10 seconds, finish this activity here and go back to parent activity
                finish();
            }
        },6*10*1000);
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