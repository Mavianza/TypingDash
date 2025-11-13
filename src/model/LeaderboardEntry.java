package model;

public class LeaderboardEntry {
    public final String username;
    public final double wpm;
    public final double accuracy;
    public final String timestamp; // ambil langsung dari DB sebagai string

    public LeaderboardEntry(String username, double wpm, double accuracy, String timestamp) {
        this.username = username;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
    }
}
