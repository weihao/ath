package org.akadia.ath.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Version implements Comparable<Version> {

    private final String version;

    public Version(String version) {
        if (version == null)
            throw new IllegalArgumentException("Version can not be null");
        if (!version.matches("[0-9]+(\\.[0-9]+)*"))
            throw new IllegalArgumentException("Invalid version format");
        this.version = version;
    }

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

    public final String get() {
        return this.version;
    }

    @Override
    public int compareTo(Version that) {
        if (that == null)
            return 1;
        String[] thisParts = this.get().split("\\.");
        String[] thatParts = that.get().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);
        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ?
                    Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ?
                    Integer.parseInt(thatParts[i]) : 0;
            if (thisPart < thatPart)
                return -1;
            if (thisPart > thatPart)
                return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (that == null)
            return false;
        if (this.getClass() != that.getClass())
            return false;
        return this.compareTo((Version) that) == 0;
    }
}