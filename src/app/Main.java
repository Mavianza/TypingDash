package app;

import javax.swing.*;
import java.awt.*;
import ui.panels.LoginPanel;
import ui.panels.MainMenuPanel;
import ui.panels.GamePanel;
import ui.panels.LeaderboardPanel;

public class Main extends JFrame implements
        LoginPanel.LoginListener,
        MainMenuPanel.MenuListener,
        GamePanel.GameListener {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanelContainer = new JPanel(cardLayout);

    private LoginPanel loginPanel;
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private LeaderboardPanel leaderboardPanel;

    private int currentUserId = -1;
    private String currentUsername = null;

    public Main() {
        setTitle("Typing Dash");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 600);
        setLocationRelativeTo(null);

        // init panels
        loginPanel = new LoginPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        gamePanel = new GamePanel(this);              
        leaderboardPanel = new LeaderboardPanel(this);

        // router
        mainPanelContainer.add(loginPanel, "login");
        mainPanelContainer.add(mainMenuPanel, "menu");
        mainPanelContainer.add(gamePanel, "game");
        mainPanelContainer.add(leaderboardPanel, "leaderboard");

        setContentPane(mainPanelContainer);
        showPanel("login");
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanelContainer, name);
    }

    // ====== LoginListener ======
    @Override
    public void onLoginSuccess(int userId, String username) {
        this.currentUserId = userId;
        this.currentUsername = username;

        // tampilkan sapaan di menu & username di game panel (pojok kanan)
        mainMenuPanel.setWelcomeMessage("Welcome, " + username + "!");
        gamePanel.setUsernameLabel(username);         
        showPanel("menu");
    }

    // ====== MenuListener ======
    @Override
    public void onStartGame() {
        // sebelum pindah ke game, set userId dan siapkan game
        gamePanel.setCurrentUser(getCurrentUserId()); // <- kirim userId aktif
        gamePanel.prepareAndStart();                  // <- countdown + start
        showPanel("game");
    }

    @Override
    public void onOpenLeaderboard() {
        // kalau sebelumnya sedang di game, pastikan dihentikan
        gamePanel.stopGame();
        leaderboardPanel.onPanelShown();
        showPanel("leaderboard");
    }

    @Override
    public void onLogout() {
        // bersihkan state + stop game jika masih berjalan
        gamePanel.stopGame();
        currentUserId = -1;
        currentUsername = null;
        showPanel("login");
    }

    // ====== GameListener ======
    @Override
    public void onGameFinished() {
        // setelah simpan skor, balik ke menu
        showPanel("menu");
    }

    public int getCurrentUserId() { return currentUserId; }
    public String getCurrentUsername() { return currentUsername; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}