import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class Button extends JButton {
    Color bg = new Color(240, 240, 240);
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(bg);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50,50);
        super.paintComponent(g);
    }
    public Button(String text) {
        super(text);
        setOpaque(false);
        setForeground(Color.BLACK);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}