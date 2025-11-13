package ui.panels;

import app.Main;
import db.ScoreDAO;
import model.LeaderboardEntry;
import ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    private final Main mainApp;

    private final JLabel title = new JLabel("Top 10 Leaderboard", SwingConstants.CENTER);
    private final JTable table;
    private final DefaultTableModel model;
    private final JButton btnBack = new JButton("« Kembali ke Menu");

    public LeaderboardPanel(Main mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout(10,10));
        setBackground(Theme.COLOR_BG);

        // ===== Title (center) =====
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Theme.COLOR_ACCENT);
        JPanel titleWrap = new JPanel(new BorderLayout());
        titleWrap.setBackground(Theme.COLOR_BG);
        titleWrap.setBorder(BorderFactory.createEmptyBorder(20, 12, 10, 12));
        titleWrap.add(title, BorderLayout.CENTER);

        JSeparator accentLine = new JSeparator();
        accentLine.setForeground(Theme.COLOR_ACCENT);
        accentLine.setBackground(Theme.COLOR_ACCENT);
        accentLine.setPreferredSize(new Dimension(0, 2));
        accentLine.setBorder(BorderFactory.createEmptyBorder());
        titleWrap.add(accentLine, BorderLayout.SOUTH);

        add(titleWrap, BorderLayout.NORTH);

        // ===== Table =====
        model = new DefaultTableModel(
                new Object[]{"Username", "WPM", "Akurasi (%)", "Waktu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 1 -> String.class;
                    case 2 -> String.class; // sudah diformat persen
                    default -> String.class;
                };
            }
        };

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(32);
        table.setFont(Theme.FONT_MAIN);
        table.setForeground(Theme.COLOR_TEXT);
        table.setBackground(Theme.COLOR_PANEL);
        table.setGridColor(new Color(60,60,60));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setRowSelectionAllowed(false);
        table.setFocusable(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(Theme.COLOR_ACCENT);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.COLOR_ACCENT.darker()));

        // right align untuk angka WPM
        TableCellRenderer right = (tbl, value, isSel, hasFocus, row, col) -> {
            JLabel l = new JLabel(value == null ? "" : value.toString());
            l.setOpaque(true);
            l.setBackground(isSel ? table.getSelectionBackground() : Theme.COLOR_PANEL);
            l.setForeground(Theme.COLOR_TEXT);
            l.setFont(Theme.FONT_MAIN);
            l.setHorizontalAlignment(SwingConstants.RIGHT);
            l.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 10));
            return l;
        };
        table.getColumnModel().getColumn(1).setCellRenderer(right);

        // center untuk username & waktu tetap default-left (lebih mudah dibaca)
        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scroll.getViewport().setBackground(Theme.COLOR_PANEL);
        add(scroll, BorderLayout.CENTER);

        // ===== Bottom action =====
        JPanel bottom = new JPanel();
        bottom.setBackground(Theme.COLOR_BG);
        btnBack.setBackground(Theme.COLOR_ACCENT);
        btnBack.setForeground(Color.BLACK);
        btnBack.setFont(Theme.FONT_MAIN.deriveFont(Font.BOLD, 18f));
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
        btnBack.setPreferredSize(new Dimension(240, 48));
        bottom.add(btnBack);
        bottom.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottom.setBorder(BorderFactory.createEmptyBorder(18, 10, 24, 10));
        add(bottom, BorderLayout.SOUTH);

        // actions
        btnBack.addActionListener(e -> mainApp.showPanel("menu"));

        // isi awal
        reload();
    }

    public void onPanelShown() { reload(); }

    private void reload() {
        model.setRowCount(0);
        List<LeaderboardEntry> top = ScoreDAO.getTop10WithUsername();
        for (LeaderboardEntry e : top) {
            // format akurasi ke persen dengan 2 desimal (pakai String.format, tanpa java.time/security/nio)
            String acc = String.format("%.2f%%", e.accuracy);
            String wpm = String.format("%.0f", e.wpm);
            model.addRow(new Object[]{ e.username, wpm, acc, e.timestamp });
        }
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"-", "0", "0.00%", "-"});
        }
    }
}
