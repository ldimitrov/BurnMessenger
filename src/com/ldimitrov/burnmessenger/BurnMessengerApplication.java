package com.ldimitrov.burnmessenger;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class BurnMessengerApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "EgWS4wnqEX5hxmMTOEBp5axx9GcczodBR02CPORM", "nAXp60HEL4Suk3roA5tNmviuLihTCAyujGeXY5Sn");
		
		
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();				
	}
}
