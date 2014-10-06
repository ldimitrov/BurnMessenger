package com.ldimitrov.burnmessenger;

import android.app.Application;

import com.ldimitrov.burnmessenger.activities.MainActivity;
import com.ldimitrov.burnmessenger.activities.R;
import com.ldimitrov.burnmessenger.util.ParseConstants;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class BurnMessengerApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		// make sure to change the "#..#" with the actualy keys obtained from Parse.com after registration!
		Parse.initialize(this, "####################################", "#####################################");
        PushService.setDefaultPushCallback(this, MainActivity.class, R.drawable.ic_stat_ic_launcher);
        ParseInstallation.getCurrentInstallation().saveInBackground();
	}

    public static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }
}
