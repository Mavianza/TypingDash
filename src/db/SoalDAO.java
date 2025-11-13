package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SoalDAO {

    // Ambil soal random; return null kalau kosong
    public static String getRandomSoal() {
        String sql = "SELECT data FROM soal ORDER BY RAND() LIMIT 1";
        try (Connection c = KoneksiDatabase.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            System.err.println("getRandomSoal() error: " + e.getMessage());
        }
        return null;
    }

    // Tambah soal baru
    public static boolean insertSoal(String text) {
        String sql = "INSERT INTO soal(data) VALUES(?)";
        try (Connection c = KoneksiDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, text);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("insertSoal() error: " + e.getMessage());
            return false;
        }
    }

    // (Opsional) Ambil semua soal
    public static List<String> getAll() {
        List<String> out = new ArrayList<>();
        String sql = "SELECT data FROM soal ORDER BY created_at DESC";
        try (Connection c = KoneksiDatabase.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) out.add(rs.getString(1));
        } catch (SQLException e) {
            System.err.println("getAll() error: " + e.getMessage());
        }
        return out;
    }
}
