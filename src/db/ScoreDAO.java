package db;

import model.Score;
import model.LeaderboardEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    /**
     * Insert skor baru.
     * Kolom timestamp diisi langsung oleh MySQL dengan NOW() agar kita tidak bergantung ke java.time di sisi Java.
     */
    public static boolean insertScore(Score s) {
        String sql = "INSERT INTO scores (user_id, wpm, accuracy, timestamp) VALUES (?, ?, ?, NOW())";
        try (Connection c = KoneksiDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getUserId());
            ps.setDouble(2, s.getWpm());
            ps.setDouble(3, s.getAccuracy());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ambil 10 skor terbaik (urut WPM desc) beserta username (JOIN ke tabel users).
     * Format Waktu dibiarkan raw (String) sesuai isi DB agar tidak perlu java.time.*
     */
    public static List<LeaderboardEntry> getTop10WithUsername() {
        List<LeaderboardEntry> list = new ArrayList<>();
        String sql =
            "SELECT u.username, s.wpm, s.accuracy, s.timestamp " +
            "FROM scores s JOIN users u ON s.user_id = u.id " +
            "ORDER BY s.wpm DESC " +
            "LIMIT 10";
        try (Connection c = KoneksiDatabase.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String username = rs.getString(1);
                double wpm      = rs.getDouble(2);
                double acc      = rs.getDouble(3);
                String ts       = rs.getString(4); // tampil apa adanya (YYYY-MM-DD HH:MM:SS...)
                list.add(new LeaderboardEntry(username, wpm, acc, ts));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /** Ambil skor mentah (tanpa JOIN) – tidak dipakai leaderboard, tapi berguna untuk debug. */
    public static List<Score> getTopScoresRaw(int limit) {
        List<Score> list = new ArrayList<>();
        String sql = "SELECT user_id, wpm, accuracy FROM scores ORDER BY wpm DESC LIMIT ?";
        try (Connection c = KoneksiDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int uid    = rs.getInt(1);
                    double wpm = rs.getDouble(2);
                    double acc = rs.getDouble(3);
                    list.add(new Score(uid, wpm, acc));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
