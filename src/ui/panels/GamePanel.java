package ui.panels;

import app.audio.MusicThread;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import db.ScoreDAO;
import db.SoalDAO;          // <<< pakai DAO soal dari DB
import model.Score;

public class GamePanel extends JPanel {

    // ==== Kontrak ke Main ====
    public interface GameListener { void onGameFinished(); }

    // ==== Konstanta ====
    private static final int TOTAL_DURATION = 60; // detik

    // ==== Warna/Style ====
    private final Color BG     = new Color(18,18,18);
    private final Color PANEL  = new Color(28,28,28);
    private final Color ACCENT = new Color(245,166,35);
    private final Color HIGHLIGHT_BG = new Color(255,255,255,60);

    // ==== Header ====
    private final JButton btnBack  = new JButton("« Menu");
    private final JLabel  timerLabel = new JLabel("Waktu: 60s");
    private final JLabel  statLabel  = new JLabel("WPM: 0");
    private final JLabel  userLabel  = new JLabel("");

    // ==== Target & Input ====
    private final JTextPane  targetPane  = new JTextPane();
    private final JTextField inputField  = new JTextField();
    private JScrollPane      targetScroll;

    // ==== Overlay ====
    private final JLabel countdownLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel resultOverlay  = new JPanel(); // tampilkan hasil akhir
    private Timer countdownTimer;
    private Timer countdownGoTimer;

    // ==== Thread & state ====
    private Thread  gameThread;
    private volatile boolean running = false;
    private int sisa = TOTAL_DURATION;

    private MusicThread gameMusic;
    private int currentUserId = -1;
    private String currentUsername = "";

    // ==== Data paragraf ====
    private String paragraph = "";
    private String[]  words = new String[0];
    private boolean[] wordCorrect = new boolean[0];
    private int wordIdx = 0;

    // ==== Metrik ====
    private int correctChars = 0;        // karakter dari kata yang BENAR (tanpa spasi)
    private int totalCharsNoSpace = 0;   // total karakter paragraf (tanpa spasi)

    private final GameListener listener;

    // ======================================================================

    public GamePanel(GameListener listener) {
        this.listener = listener;
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(70,70,70)));

        styleButton(btnBack);
        btnBack.addActionListener(e -> {
            stopGame();
            listener.onGameFinished();
        });

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        left.setOpaque(false);
        left.add(btnBack);
        header.add(left, BorderLayout.WEST);

        JPanel mid = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 6));
        mid.setOpaque(false);
        timerLabel.setForeground(ACCENT);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statLabel.setForeground(Color.LIGHT_GRAY);
        statLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mid.add(timerLabel);
        mid.add(statLabel);
        header.add(mid, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        right.setOpaque(false);
        userLabel.setForeground(Color.LIGHT_GRAY);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        right.add(userLabel);
        header.add(right, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== PARAGRAF CARD (center) =====
        targetPane.setEditable(false);
        targetPane.setBackground(PANEL);
        targetPane.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        targetPane.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        targetPane.setForeground(new Color(230,230,230));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120,90,50)),
                BorderFactory.createEmptyBorder(12,12,12,12)
        ));
        card.add(targetPane, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(820, 220));

        targetScroll = new JScrollPane(card);
        targetScroll.setBorder(BorderFactory.createEmptyBorder());
        targetScroll.getViewport().setBackground(BG);

        // ===== Countdown overlay (di atas card) =====
        countdownLabel.setFont(new Font("Segoe UI", Font.BOLD, 96));
        countdownLabel.setForeground(ACCENT);
        countdownLabel.setVisible(false);
        countdownLabel.setAlignmentX(0.5f);
        countdownLabel.setAlignmentY(0.5f);

        // ===== Result overlay (di tengah, tanpa dimming) =====
        buildResultOverlay();  // bikin panel + tombol, default hidden

        // Stage overlay stack
        JPanel stage = new JPanel();
        stage.setBackground(BG);
        stage.setLayout(new OverlayLayout(stage));
        stage.add(resultOverlay);   // paling atas (hidden default)
        JPanel countdownLayer = new JPanel(new GridBagLayout());
        countdownLayer.setOpaque(false);
        countdownLayer.setAlignmentX(0.5f);
        countdownLayer.setAlignmentY(0.5f);
        countdownLayer.add(countdownLabel);
        stage.add(countdownLayer);
        stage.add(targetScroll);    // paling bawah

        // center-kan stage
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0; gc.weightx=1; gc.weighty=1; gc.anchor=GridBagConstraints.CENTER;
        center.add(stage, gc);
        add(center, BorderLayout.CENTER);

        // ===== INPUT FIELD (lebar setara card, center) =====
        inputField.setBackground(PANEL);
        inputField.setForeground(Color.WHITE);
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120,90,50)),
                BorderFactory.createEmptyBorder(10,12,10,12)
        ));
        inputField.setPreferredSize(new Dimension(820, 48));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottom.setBackground(BG);
        bottom.add(inputField);
        add(bottom, BorderLayout.SOUTH);

        // Validasi per-kata saat spasi/Enter
        inputField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (!running) return;
                if (e.getKeyCode()== KeyEvent.VK_SPACE || e.getKeyCode()== KeyEvent.VK_ENTER) {
                    commitWord();
                } else {
                    updateStatsLabel(); // realtime WPM di header
                }
            }
        });
    }

    // ======================================================================
    // Public API dari Main

    public void setCurrentUser(int userId) { this.currentUserId = userId; }

    public void setUsernameLabel(String username) {
        this.currentUsername = username == null ? "" : username;
        userLabel.setText("User: " + this.currentUsername);
    }

    public void prepareAndStart() {
        // reset UI state
        inputField.setText("");
        inputField.setVisible(true);
        correctChars = 0;
        wordIdx = 0;
        sisa = TOTAL_DURATION;
        running = false;
        timerLabel.setText("Waktu: " + TOTAL_DURATION + "s");
        statLabel.setText("WPM: 0");

        // tampilkan elemen permainan
        targetScroll.setVisible(true);
        resultOverlay.setVisible(false);

        // === load paragraf dari DB (fallback ke file) & siapkan kata ===
        paragraph = loadParagraph();               // <<< ganti sini
        words = splitWords(paragraph);
        wordCorrect = new boolean[words.length];
        totalCharsNoSpace = paragraph.replaceAll("\\s+","").length();

        showReadyPrompt();
        doCountdownThenStart();
    }

    // ======================================================================
    // Internal UI building

    private void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(new Color(60,60,60));
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120,90,50)),
                BorderFactory.createEmptyBorder(6,14,6,14)
        ));
    }

    private void buildResultOverlay() {
        resultOverlay.setOpaque(false);
        resultOverlay.setVisible(false);
        resultOverlay.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setBackground(new Color(22,22,22));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120,90,50), 2),
                BorderFactory.createEmptyBorder(32, 42, 32, 42)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(420, 320));

        JLabel title = new JLabel("Game Selesai!", SwingConstants.CENTER);
        title.setForeground(ACCENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel wpmL = new JLabel("WPM: 0", SwingConstants.CENTER);
        wpmL.setForeground(Color.WHITE);
        wpmL.setFont(new Font("Segoe UI", Font.BOLD, 20));
        wpmL.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel accL = new JLabel("Akurasi: 0,00%", SwingConstants.CENTER);
        accL.setForeground(Color.WHITE);
        accL.setFont(new Font("Segoe UI", Font.BOLD, 20));
        accL.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAgain = buildOverlayButton("Main Lagi");
        btnAgain.addActionListener(e -> prepareAndStart());

        JButton btnMenu = buildOverlayButton("Kembali ke Menu");
        btnMenu.addActionListener(e -> {
            stopGame();
            listener.onGameFinished();
        });

        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(wpmL);
        card.add(Box.createVerticalStrut(8));
        card.add(accL);
        card.add(Box.createVerticalStrut(28));
        card.add(btnAgain);
        card.add(Box.createVerticalStrut(12));
        card.add(btnMenu);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0; gc.anchor=GridBagConstraints.CENTER;
        resultOverlay.add(card, gc);

        // simpan referensi label untuk update nilai
        resultOverlay.putClientProperty("wpmLabel", wpmL);
        resultOverlay.putClientProperty("accLabel", accL);
        resultOverlay.putClientProperty("titleLabel", title);
    }

    private void showResultOverlay(String title, double wpm, double acc) {
        // sembunyikan paragraf & input agar background bersih
        targetScroll.setVisible(false);
        inputField.setVisible(false);

        JLabel wpmL = (JLabel) resultOverlay.getClientProperty("wpmLabel");
        JLabel accL = (JLabel) resultOverlay.getClientProperty("accLabel");
        JLabel ttl  = (JLabel) resultOverlay.getClientProperty("titleLabel");

        ttl.setText(title);
        wpmL.setText(String.format("WPM: %.0f", wpm));
        accL.setText(String.format("Akurasi: %.2f%%", acc));

        resultOverlay.setVisible(true);
        revalidate();
        repaint();
    }

    private JButton buildOverlayButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(58,58,58));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150,150,150)),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));
        btn.setMaximumSize(new Dimension(260, 46));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ======================================================================
    // Countdown & Timer

    private void doCountdownThenStart() {
        stopCountdownTimers();
        countdownLabel.setText("4");
        countdownLabel.setVisible(true);
        inputField.setEnabled(false);

        final int[] counter = {4};
        countdownTimer = new Timer(1000, null);
        countdownTimer.addActionListener(e -> {
            counter[0]--;
            if (counter[0] > 0) {
                countdownLabel.setText(String.valueOf(counter[0]));
            } else {
                countdownTimer.stop();
                countdownLabel.setText("Go!");
                countdownGoTimer = new Timer(600, evt -> {
                    countdownGoTimer.stop();
                    countdownLabel.setVisible(false);
                    startGameProper();
                });
                countdownGoTimer.setRepeats(false);
                countdownGoTimer.start();
            }
        });
        countdownTimer.setInitialDelay(1000);
        countdownTimer.start();
    }

    private void startGameProper() {
        renderTarget();
        running = true;
        inputField.setEnabled(true);
        inputField.requestFocusInWindow();

        // Musik
        try { if (gameMusic != null) gameMusic.stopMusic(); } catch (Exception ignored) {}
        gameMusic = new MusicThread("assets/background_music.wav");
        gameMusic.start();

        // Timer
        if (gameThread != null && gameThread.isAlive()) {
            try { gameThread.interrupt(); } catch (Exception ignored) {}
        }
        gameThread = new Thread(() -> {
            try {
                while (running && sisa > 0) {
                    final int display = sisa;
                    SwingUtilities.invokeLater(() -> {
                        timerLabel.setText("Waktu: " + display + "s");
                        updateStatsLabel();
                    });
                    Thread.sleep(1000);
                    sisa--;
                }
            } catch (InterruptedException ignored) {
            } finally {
                finishGame(sisa <= 0 ? "Waktu Habis!" : "Game Selesai!");
            }
        }, "GameTimerThread");
        gameThread.start();
    }

    private void finishGame(String title) {
        running = false;
        try { if (gameMusic != null) gameMusic.stopMusic(); } catch (Exception ignored) {}
        final double[] res = computeMetrics();
        // Simpan skor async
        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() {
                if (currentUserId <= 0) return false;
                return ScoreDAO.insertScore(new Score(currentUserId, res[0], res[1]));
            }
            @Override protected void done() {
                showResultOverlay(title, res[0], res[1]);
            }
        }.execute();
    }

    // ======================================================================
    // Game mechanics

    private void commitWord() {
        String typed = inputField.getText().trim();
        inputField.setText("");
        if (typed.isEmpty() || wordIdx >= words.length) return;

        String expected = words[wordIdx];
        boolean ok = typed.equals(expected);
        wordCorrect[wordIdx] = ok;
        if (ok) correctChars += expected.length();

        wordIdx++;
        renderTarget();
        updateStatsLabel();

        // selesai lebih cepat
        if (wordIdx >= words.length) {
            stopGame();
            finishGame("Game Selesai!");
        }
    }

    private void renderTarget() {
        StyledDocument doc = new DefaultStyledDocument();

        SimpleAttributeSet gray  = new SimpleAttributeSet();
        StyleConstants.setForeground(gray,  new Color(200,200,200));

        SimpleAttributeSet highlight = new SimpleAttributeSet();
        highlight.addAttributes(gray);
        StyleConstants.setBackground(highlight, HIGHLIGHT_BG);

        SimpleAttributeSet green = new SimpleAttributeSet();
        StyleConstants.setForeground(green, new Color(60,200,110));

        SimpleAttributeSet red   = new SimpleAttributeSet();
        StyleConstants.setForeground(red,   new Color(220,80,80));

        try {
            for (int i=0; i<words.length; i++) {
                if (i>0) doc.insertString(doc.getLength(), " ", gray);
                if (i < wordIdx) {
                    doc.insertString(doc.getLength(), words[i], wordCorrect[i] ? green : red);
                } else if (i == wordIdx) {
                    doc.insertString(doc.getLength(), words[i], highlight); // <<< highlight kata aktif
                } else {
                    doc.insertString(doc.getLength(), words[i], gray);
                }
            }
        } catch (BadLocationException ignored) {}

        centerDocument(doc);
        targetPane.setDocument(doc);
    }

    private void showReadyPrompt() {
        StyledDocument doc = new DefaultStyledDocument();

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setFontSize(center, 28);
        StyleConstants.setBold(center, true);
        StyleConstants.setForeground(center, new Color(200, 200, 200));

        try { doc.insertString(0, "Ready???", center); } catch (BadLocationException ignored) {}

        centerDocument(doc);
        targetPane.setDocument(doc);
    }

    private void centerDocument(StyledDocument doc) {
        SimpleAttributeSet align = new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), align, false);
    }

    private void updateStatsLabel() {
        double[] m = computeMetrics();
        statLabel.setText(String.format("WPM: %.0f", m[0]));
    }

    /**
     * Rumus salah
     * time = (60 - sisa) / 60
     * WPM  = (jumlah karakter benar / 5) / time
     * Akurasi = (jumlahKarakterBenar / jumlahKarakterTotal) * 100%
     */
    private double[] computeMetrics() {
        double time = (TOTAL_DURATION - sisa) / 60.0;
        if (time <= 0) time = 1.0 / 60.0;

        //  Rumus WPM yang benar
        double wpm = (correctChars / 5.0) / time;

        double acc = (totalCharsNoSpace == 0) ? 0.0
                : (100.0 * correctChars / totalCharsNoSpace);

        return new double[]{wpm, acc};
    }

    // ======================================================================
    // Loader paragraf: DB → fallback file

    private String loadParagraph() {
        // 1) Coba dari DB
        try {
            String fromDb = SoalDAO.getRandomSoal();
            if (fromDb != null) {
                String trimmed = fromDb.trim();
                if (!trimmed.isEmpty()) return trimmed;
            }
        } catch (Exception e) {
            System.err.println("loadParagraph(DB) error: " + e.getMessage());
        }

        // 2) Fallback dari file
        return loadRandomParagraphFromFile();
    }

    private String loadRandomParagraphFromFile() {
        File f = new File("assets/contoh_soal.txt");
        if (!f.exists()) return "Isi file assets/contoh_soal.txt terlebih dahulu atau tambahkan data di tabel soal.";

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; while ((line = br.readLine()) != null) sb.append(line).append('\n');
        } catch (IOException e) {
            return "Gagal membaca contoh_soal.txt";
        }
        String raw = sb.toString();

        // support format array "..." atau blok dipisah baris kosong
        List<String> picks = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuote = false;
        for (int i=0;i<raw.length();i++) {
            char ch = raw.charAt(i);
            if (ch=='"') {
                inQuote = !inQuote;
                if (!inQuote) {
                    String parag = cur.toString().trim();
                    if (!parag.isEmpty()) picks.add(parag);
                    cur.setLength(0);
                }
            } else if (inQuote) {
                cur.append(ch);
            }
        }
        if (picks.isEmpty()) {
            String[] byBlank = raw.split("\\n\\s*\\n+");
            for (String b : byBlank) {
                String t = b.replaceAll("//.*","").trim();
                if (!t.isEmpty()) picks.add(t);
            }
        }
        if (picks.isEmpty()) picks.add("Teks contoh tidak tersedia.");
        return picks.get(new Random().nextInt(picks.size()));
    }

    private String[] splitWords(String text) {
        return text.trim().isEmpty() ? new String[0] : text.trim().split("\\s+");
    }

    // ======================================================================

    public void stopGame() {
        running = false;
        if (gameThread != null) {
            try { gameThread.interrupt(); } catch (Exception ignored) {}
        }
        stopCountdownTimers();
        if (gameMusic != null) {
            try { gameMusic.stopMusic(); } catch (Exception ignored) {}
        }
    }

    private void stopCountdownTimers() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
        if (countdownGoTimer != null && countdownGoTimer.isRunning()) {
            countdownGoTimer.stop();
        }
    }
}
