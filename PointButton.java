import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class PointButton extends JButton {
    private boolean isActive = false;
    int index;
    Cords cords;

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!isActive) {
            g2d.setColor(new Color(255, 0, 0));
        } else {
            g2d.setColor(new Color(0, 255, 0));
        }
        g2d.fillOval(0, 0, 10, 10);
        super.paintComponent(g);
    }

    PointButton(MainPanel mainPanel, int x, int y, int index) {
        this.index = index;
        cords = new Cords(x, y);
        setLayout(null);
        setForeground(Color.WHITE);
        setBounds(cords.getX(), cords.getY(), 10, 10);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!getActive()) {
                    mainPanel.dp.setSelectedPoint(PointButton.this);
                } else {
                    setActive(false);
                    mainPanel.dp.deactivateAllButtons();
                }
            }
        });
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        repaint();
    }

    public void moveButton(int x, int y) {
        cords.setX(cords.getX() + x);
        cords.setY(cords.getY() + y);
        setBounds(cords.getX(), cords.getY(), 10, 10);
    }
}
