package rslbot;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * JNA mapping for the native RaidClient DLL.
 */
public interface RaidClient extends Library {
    RaidClient INSTANCE = Native.load("RaidClient", RaidClient.class);

    boolean clickRaid(int x, int y);
    boolean captureRaid(String path);
}
