package ui;

import java.awt.*;
import javax.swing.text.*;

public class Theme {
    public static final Color COLOR_BG = new Color(18,18,18);
    public static final Color COLOR_PANEL = new Color(28,28,28);
    public static final Color COLOR_ACCENT = new Color(245,166,35);
    public static final Color COLOR_TEXT = new Color(220,220,220);
    public static final Font FONT_MAIN = new Font("Segoe UI", Font.PLAIN, 16);

    public static final SimpleAttributeSet STYLE_DEFAULT = new SimpleAttributeSet();
    public static final SimpleAttributeSet STYLE_CORRECT = new SimpleAttributeSet();
    public static final SimpleAttributeSet STYLE_WRONG = new SimpleAttributeSet();

    static {
        StyleConstants.setForeground(STYLE_DEFAULT, COLOR_TEXT);
        StyleConstants.setForeground(STYLE_CORRECT, new Color(60,200,110));
        StyleConstants.setForeground(STYLE_WRONG, new Color(220,80,80));
    }
}
