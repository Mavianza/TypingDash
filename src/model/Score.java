package model;

import java.time.LocalDateTime;

public class Score {
    private int scoreId;
    private int userId;
    private double wpm;
    private double accuracy;
    private LocalDateTime timestamp;

    public Score(int userId, double wpm, double accuracy) {
        this.userId = userId;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.timestamp = LocalDateTime.now();
    }

    // getter & setter
    public int getUserId() { return userId; }
    public double getWpm() { return wpm; }
    public double getAccuracy() { return accuracy; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
