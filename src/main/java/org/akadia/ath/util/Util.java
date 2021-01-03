package org.akadia.ath.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {
    public static String getLatestVersion() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=87124"
            ).openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            return new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentVersion() {
        return Util.class.getPackage().getImplementationVersion();
    }

    public static boolean isUpdateToDate() {
        Version spigot = new Version(getLatestVersion());
        Version current = new Version(getCurrentVersion());
        return current.compareTo(spigot) >= 0;
    }
}
