package com.paradigmcreatives.apspeak.gcm;

import com.paradigmcreatives.apspeak.R;

/**
 * DoodleNotificationInfo configurations.
 * 
 * @author Swaroop | ParadigmCreatives
 */
public class PushNotificationConfig {
    /**
     * Application package name.
     */
    public static final String CURRENT_PKG_NAME = "com.paradigmcreatives.apspeak";

    /**
     * Application name.
     */
    public static final String APP_NAME = "Whatsay";

    public static final String PROTO = "PROTO:";

    /**
     * DoodleNotificationInfo icon.
     */
    public static final int NOTIFICATION_ICON = R.drawable.notification_icon;

    /**
     * DoodleNotificationInfo sound status.
     */
    public static final boolean NOTIFICATION_SOUND = true;

    /**
     * DoodleNotificationInfo vibrate status.
     */
    public static final boolean NOTIFICATION_VIBRATE = true;

    /**
     * If this flag is set to true the application will automatically register the device with the google GCM service
     * once the device gets unregistered with the GCM server.
     */
    public static final boolean AUTO_RE_REGISTER_WHEN_UNREGISTERED = true;
}// end of class