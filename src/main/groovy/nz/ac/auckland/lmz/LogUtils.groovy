package nz.ac.auckland.lmz

import nz.ac.auckland.util.JacksonHelper

/**
 * A utility package for assisting with formatting log messages.
 * <p>Author: <a href="http://gplus.to/tzrlk">Peter Cummuskey</a></p>
 */
public abstract class LogUtils {

    /**
     * Formats a provided log message into the format of ( message: {"param":"value"} ).
     * @param message The message to prefix the formatted message with.
     * @param context The context of the message, to be serialised as part of the formatting.
     * @return The provided message, joined with the json serialised context, separated by a colon and a space.
     */
    public static String format(String message, Map context) {
        return "$message: ${JacksonHelper.serialize(context)}";
    }

    /** Cannot be instantiated */
    private LogUtils() {}

}
