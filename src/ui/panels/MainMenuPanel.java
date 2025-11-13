package ui.panels;

import javax.swing.*;
import java.awt.*;

import ui.Theme;

public class MainMenuPanel extends JPanel {
    public interface MenuListener {
        void onStartGame();
        void onOpenLeaderboard();
        void onLogout();
    }

    private static final Dimension BUTTON_SIZE = new Dimension(300, 46);

    private final MenuListener listener;
    private final JLabel welcomeLabel = new JLabel("Selamat datang!");

    public MainMenuPanel(MenuListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());
        setBackground(Theme.COLOR_BG);

        welcomeLabel.setForeground(Theme.COLOR_ACCENT);
        welcomeLabel.setFont(Theme.FONT_MAIN.deriveFont(Font.BOLD, 30f));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton play = accentButton("Mulai Main");
        JButton lb   = accentButton("Lihat Leaderboard");
        JButton out  = secondaryButton("Logout");

        play.addActionListener(e -> listener.onStartGame());
        lb.addActionListener(e -> listener.onOpenLeaderboard());
        out.addActionListener(e -> listener.onLogout());

        JPanel content = new JPanel();
        content.setBackground(Theme.COLOR_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(32, 24, 32, 24));

        play.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb.setAlignmentX(Component.CENTER_ALIGNMENT);
        out.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(welcomeLabel);
        content.add(Box.createVerticalStrut(24));
        content.add(play);
        content.add(Box.createVerticalStrut(12));
        content.add(lb);
        content.add(Box.createVerticalStrut(18));
        content.add(out);

        add(content);
    }

    private JButton accentButton(String label) {
        JButton button = new JButton(label);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setBackground(Theme.COLOR_ACCENT);
        button.setForeground(Color.BLACK);
        button.setFont(Theme.FONT_MAIN.deriveFont(Font.BOLD, 18f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton secondaryButton(String label) {
        JButton button = accentButton(label);
        button.setBackground(Theme.COLOR_PANEL);
        button.setForeground(Theme.COLOR_TEXT);
        return button;
    }

    public void setWelcomeMessage(String msg){
        welcomeLabel.setText(msg);
    }
}
