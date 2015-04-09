# CC SDK Demo App
This demo Android app is a boilerplate that shows how to implement AWR Ideas' CC SDK on you app. You can download the code and test in your own device, but do have in mind that the SDK relies on BLE, WiFi, Geolocation (it also uses Google Play Services) and offers registered in that area in order to work properly. For more information, questions or suggestions, please contact AWR Ideas via http://awrideas.com/

## How to use the SDK
1. Add the maven dependency to your project. Add the URL to your project's build.gradle and the dependency to your app's build.gradle. Follow the demo for better results.
2. (Optional) The SDK contains a class called UserInfo, this class can hold your user's data (gender and date of birth) for the SDK configuration process. While those fields are not mandatory, they are recommended in order to show your users better ads.
3. Inside the library there's a class called SDKManager (a singleton), this class contains two methods called registerApp and registerAppInBackground; these methods receive your appID (which you can get from AWR Ideas), a UserInfo object (fields can be null, but the object must not be null, use constructor), a boolean value called shouldUseFloatingNotifications (allows you to control whether the sdk should show ads with complex notifications similar to Facebook's ChatHeads) and a RegistrationListener (For the inBackground method only. This is a callback that notifies you when the configuration process has been completed). You can use any of the two methods to register your app and start the SDK. The main difference is that one runs on the current thread, while the other runs on a background thread and notifies you through an interface method. This process will return an integer value. See table of values by the end of this section.
4. If the process succeded, then you should save a flag or variable to non-volatile memory in order to avoid doing this process everytime the user opens the app.
5. If you want a user to logout and register another one on the SDK, just call the same methods again with new data.


### SDKManager Return Values
These are the possible values returned by the SDK. If you receive something different, please contact us and save your log/stack trace.

SDKManager.SUCCESS = 0  
Everything went fine, carry on.

SDKManager.ERROR_CONNECTION = 1  
The app has no connection to the Internet or the service is unstable.

SDKManager.ERROR_API = 2  
The web service replied with an error or exception, check the log for more info.

SDKManager.ERROR_NO_PLAY_SERVICES = 3  
The device doesn't have Play Services installed or updated. Ask the user to do so.

SDKManager.ERROR_NULL_RESPONSE = 4  
The server sent an empty response. Rather uncommon, usually a network error.

SDKManager.ERROR_UNKNOWN = 5  
Something broke inside the SDK, please report this to us.
