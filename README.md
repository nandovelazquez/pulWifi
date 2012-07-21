pulWifi
=======
 
pulWifi shows the default password of some wireless networks.
Copyright (C) 2011 Javi Pulido and Antonio Vázquez Blanco.

LEGAL NOTE
----------
The very only intention of this application is to demonstrate the insecurity of your own routers, and warn of the danger of using default passwords. The author of this application is not responsible for the end user use made of this. When using this application with a wireless network, make sure that is your own or have permission from the owner / administrator of the network. Remember that using the broadband connection of others is a crime. Please do not use this application in an ilegal way. Use below your own responsability.

LICENSE
-------
This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

CONTRIBUTING
------------
You can freely contribute through our GitHub repository (https://github.com/pulWifi/pulWifi).

CODING GUIDELINES
-----------------
The first thing is that I don't believe in the 80 character limit as nowerdays computer screens have very high resolutions.
I don't put a space between a function name and its parenthesis. Not even when it is a flow control function.
Brackets are opened in the same line as their statements.
If a single instruction is executed no brackets are needed but the instruction must be in a new line separated from the condition.

This is a well formated piece of code:
```java
public WirelessNetwork(String ESSID, String BSSID, int signal, String capabilities) {
	mEssid = ESSID;
	mBssid = BSSID.toUpperCase();
	mPasswords = new ArrayList<String>();
	mSignal = signal;
	mCapabilities = WirelessEncryption.parseEncription(capabilities);
	if(mDatabase.containsKey(mEssid))
		mCrackeable = mDatabase.get(mEssid);
	else {
		mCrackeable = (new CrackNetwork(this)).isCrackeable();
		mDatabase.put(mEssid, mCrackeable);
	}
}
```