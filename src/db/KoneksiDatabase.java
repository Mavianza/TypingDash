package db;

import java.sql.*;
import java.security.MessageDigest;

public class KoneksiDatabase {
    private static final String DB_NAME="typing_dash";
    private static final String URL  = "jdbc:mysql://localhost:3306/"+DB_NAME+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException("MySQL Driver not found.", e); }
    }

    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL, USER, PASS); }

    // ---- contoh minimal register / login (password di-hash SHA-256) ----
    private static String sha256(String s){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(s.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(byte x: b) sb.append(String.format("%02x", x));
            return sb.toString();
        }catch(Exception e){ throw new RuntimeException(e); }
    }

    public static boolean registerUser(String username, String password){
        String check = "SELECT 1 FROM users WHERE username=?";
        String ins   = "INSERT INTO users(username,password_hash) VALUES(?,?)";
        try(Connection c=getConnection()){
            try(PreparedStatement ps=c.prepareStatement(check)){
                ps.setString(1, username);
                try(ResultSet rs=ps.executeQuery()){ if(rs.next()) return false; }
            }
            try(PreparedStatement ps=c.prepareStatement(ins)){
                ps.setString(1, username);
                ps.setString(2, sha256(password));
                return ps.executeUpdate()==1;
            }
        }catch(SQLException e){ e.printStackTrace(); return false; }
    }

    public static int loginUser(String username, String password){
        String q = "SELECT id, password_hash FROM users WHERE username=?";
        try(Connection c=getConnection(); PreparedStatement ps=c.prepareStatement(q)){
            ps.setString(1, username);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next()) return -1;
                int id = rs.getInt("id");
                String hash = rs.getString("password_hash");
                return hash.equals(sha256(password)) ? id : -1;
            }
        }catch(SQLException e){ e.printStackTrace(); return -1; }
    }

public static boolean saveScore(int userId, double wpm, double accuracy){
    String q = "INSERT INTO scores(user_id, wpm, accuracy) VALUES(?,?,?)";
    try(Connection c=getConnection(); PreparedStatement ps=c.prepareStatement(q)){
        ps.setInt(1, userId);
        ps.setDouble(2, wpm);
        ps.setDouble(3, accuracy);
        return ps.executeUpdate()==1;
    } catch(Exception e){ e.printStackTrace(); return false; }
}

public static ResultSet getLeaderboard(Connection c) throws SQLException {
    String q = """
      SELECT u.username, s.wpm, s.accuracy, s.created_at
      FROM scores s JOIN users u ON s.user_id=u.id
      ORDER BY s.wpm DESC, s.accuracy DESC, s.created_at DESC
      LIMIT 50
    """;
    PreparedStatement ps = c.prepareStatement(q);
    return ps.executeQuery(); // caller yang close
}

}
