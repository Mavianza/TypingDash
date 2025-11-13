package ui.panels;

import javax.swing.*;
import java.awt.*;

import db.KoneksiDatabase;
import ui.Theme;

public class LoginPanel extends JPanel {
    public interface LoginListener { void onLoginSuccess(int userId, String username); }

    private static final int COLUMN_WIDTH = 320;
    private static final int LABEL_WIDTH = 110;
    private static final int FIELD_GAP = 12;
    private static final Dimension LABEL_SIZE = new Dimension(LABEL_WIDTH, 34);
    private static final Dimension FIELD_SIZE = new Dimension(COLUMN_WIDTH - LABEL_WIDTH - FIELD_GAP, 34);
    private static final Dimension BUTTON_SIZE = new Dimension(COLUMN_WIDTH, 42);

    private final LoginListener listener;
    private final JTextField userField = new JTextField(18);
    private final JPasswordField passField = new JPasswordField(18);

    public LoginPanel(LoginListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());
        setBackground(Theme.COLOR_BG);

        styleTextField(userField);
        styleTextField(passField);

        JLabel title = new JLabel("Welcome to Typing Dash");
        title.setForeground(Theme.COLOR_ACCENT);
        title.setFont(Theme.FONT_MAIN.deriveFont(Font.BOLD, 32f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = buildForm();

        JButton login = accentButton("Login");
        JButton reg   = accentButton("Register");
        login.addActionListener(e -> doLogin());
        reg.addActionListener(e -> doRegister());

        JPanel content = new JPanel();
        content.setBackground(Theme.COLOR_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(32, 24, 32, 24));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.setAlignmentX(Component.CENTER_ALIGNMENT);
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        reg.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(24));
        content.add(form);
        content.add(Box.createVerticalStrut(26));
        content.add(login);
        content.add(Box.createVerticalStrut(12));
        content.add(reg);

        add(content);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel();
        form.setBackground(Theme.COLOR_BG);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.setMaximumSize(new Dimension(COLUMN_WIDTH, Integer.MAX_VALUE));

        form.add(buildRow("Username:", userField));
        form.add(Box.createVerticalStrut(12));
        form.add(buildRow("Password:", passField));

        return form;
    }

    private JPanel buildRow(String labelText, JComponent field) {
        JPanel row = new JPanel();
        row.setBackground(Theme.COLOR_BG);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(COLUMN_WIDTH, FIELD_SIZE.height + 8));

        JLabel label = formLabel(labelText);
        label.setPreferredSize(LABEL_SIZE);
        label.setMinimumSize(LABEL_SIZE);
        label.setMaximumSize(LABEL_SIZE);

        row.add(label);
        row.add(Box.createHorizontalStrut(FIELD_GAP));
        row.add(field);

        return row;
    }

    private JLabel formLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Theme.COLOR_TEXT);
        label.setFont(Theme.FONT_MAIN);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(Theme.COLOR_PANEL);
        field.setForeground(Theme.COLOR_TEXT);
        field.setCaretColor(Theme.COLOR_ACCENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.COLOR_ACCENT, 2),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        field.setPreferredSize(FIELD_SIZE);
        field.setMinimumSize(FIELD_SIZE);
        field.setMaximumSize(FIELD_SIZE);
    }

    private JButton accentButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Theme.COLOR_ACCENT);
        button.setForeground(Color.BLACK);
        button.setFont(Theme.FONT_MAIN.deriveFont(Font.BOLD, 18f));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        return button;
    }

    private void doLogin(){
        String u=userField.getText().trim();
        String p=new String(passField.getPassword());
        if(u.isEmpty()||p.isEmpty()){
            JOptionPane.showMessageDialog(this,"Isi username & password.","Login Gagal",JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = KoneksiDatabase.loginUser(u,p);
        if(id>0){ JOptionPane.showMessageDialog(this,"Login berhasil!!","Message",JOptionPane.INFORMATION_MESSAGE);
            listener.onLoginSuccess(id,u);
        } else {
            JOptionPane.showMessageDialog(this,"Username atau password salah.","Login Gagal",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void doRegister(){
        String u=userField.getText().trim();
        String p=new String(passField.getPassword());
        if(u.isEmpty()||p.isEmpty()){
            JOptionPane.showMessageDialog(this,"Isi username & password untuk registrasi.","Registrasi Gagal",JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean ok = KoneksiDatabase.registerUser(u,p);
        if(ok) JOptionPane.showMessageDialog(this,"Registrasi berhasil! Silakan login.","Message",JOptionPane.INFORMATION_MESSAGE);
        else   JOptionPane.showMessageDialog(this,"Registrasi gagal. Username mungkin sudah digunakan.","Registrasi Gagal",JOptionPane.ERROR_MESSAGE);
    }
}
