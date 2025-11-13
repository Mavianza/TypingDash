package _archive;
import java.sql.*;

import db.KoneksiDatabase;

public class DBPing {
    public static void main(String[] args) {
        try (Connection c = KoneksiDatabase.getConnection()) {
            System.out.println("OK: Connected to " + c.getCatalog());
            try (Statement st=c.createStatement();
                 ResultSet rs=st.executeQuery("SELECT COUNT(*) FROM users")) {
                rs.next();
                System.out.println("users.count = " + rs.getInt(1));
            }
        } catch (Exception e) {
            System.err.println("FAIL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
