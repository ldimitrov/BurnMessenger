package com.ldimitrov.burnmessenger;

import android.app.Application;

import com.ldimitrov.burnmessenger.activities.MainActivity;
import com.parse.Parse;
import com.parse.PushService;

public class BurnMessengerApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "EgWS4wnqEX5hxmMTOEBp5axx9GcczodBR02CPORM", "nAXp60HEL4Suk3roA5tNmviuLihTCAyujGeXY5Sn");
		PushService.setDefaultPushCallback(this, MainActivity.class);
	}
}
