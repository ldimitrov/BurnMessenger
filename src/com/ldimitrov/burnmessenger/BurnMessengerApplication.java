package com.ldimitrov.burnmessenger;

import android.app.Application;

import com.ldimitrov.burnmessenger.activities.MainActivity;
import com.ldimitrov.burnmessenger.util.ParseConstants;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class BurnMessengerApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "EgWS4wnqEX5hxmMTOEBp5axx9GcczodBR02CPORM", "nAXp60HEL4Suk3roA5tNmviuLihTCAyujGeXY5Sn");
		PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
	}

    public static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }
}