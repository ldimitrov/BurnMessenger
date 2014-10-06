BurnMessenger
=============

Self-destructing messages app.

Uses Parse.com as back-end service. For that reason, If you want to use it, you need to make a registration at Parse.com and obtain the unique Parse service keys.
Once you get them, copy/paste into **BurnMessengerApplication.java** class.

Should look something like this:
    Parse.initialize(this, "########################################", "########################################");

Functionality is implemented and supports sending and recieving of text messages, images and videos up to 10 seconds in length. 

**TODO**

- Integration with Contacts API to facilitate better regitration process a much better way to find/contact friends (as opposed to the current method).
- Email validation and forgotten password functionality
- UI Improvements
