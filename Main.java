import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
public class Main extends JPanel {
    public final List<Point> controlPoints = new ArrayList<>();
    public Point selectedPoint = null;

    public Main() {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    controlPoints.add(new Point(e.getX(), e.getY()));
                else if (SwingUtilities.isRightMouseButton(e))
                    controlPoints.removeIf(p -> p.distance(e.getPoint()) < 10);
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                selectedPoint = getPointAt(e.getPoint());
            }

            public void mouseReleased(MouseEvent e) {
                selectedPoint = null;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selectedPoint != null) {
                    selectedPoint.setLocation(e.getX(), e.getY());
                    repaint();
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    controlPoints.clear();
                    repaint();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    public Point getPointAt(Point clickPoint) {
        for (Point p : controlPoints)
            if (p.distance(clickPoint) < 10)
                return p;
        return null;
    }

    public Point calculateBezierPoint(double t, List<Point> points) {
        if (points.size() == 1) return points.getFirst();
        List<Point> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            int x = (int) ((1 - t) * points.get(i).x + t * points.get(i + 1).x);
            int y = (int) ((1 - t) * points.get(i).y + t * points.get(i + 1).y);
            newPoints.add(new Point(x, y));
        }
        return calculateBezierPoint(t, newPoints);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.GRAY);
        for (int i = 0; i < controlPoints.size() - 1; i++)
            g2.drawLine(controlPoints.get(i).x, controlPoints.get(i).y,
                    controlPoints.get(i + 1).x, controlPoints.get(i + 1).y);
        g2.setColor(Color.RED);
        int d = 10;
        for (Point p : controlPoints)
            g2.fillOval(p.x - d / 2, p.y - d / 2, d, d);
        if (controlPoints.size() > 1) {
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            int numPoints = 100;
            for (int i = 0; i < numPoints; i++) {
                double t1 = (double) i / numPoints;
                double t2 = (double) (i + 1) / numPoints;
                Point p1 = calculateBezierPoint(t1, controlPoints);
                Point p2 = calculateBezierPoint(t2, controlPoints);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        int x = 10, y = 20;
        g2.drawString("LEGENDA:", x, y);
        y += 15;
        g2.drawString("Lewy przycisk myszy - Dodaj punkt", x, y);
        y += 15;
        g2.drawString("Prawy przycisk myszy - Usuń punkt", x, y);
        y += 15;
        g2.drawString("Przeciągnij - Przesuń punkt", x, y);
        y += 15;
        g2.drawString("R - Resetuj", x, y);
    }

    public static void main(String[] args) {
        JFrame okno = new JFrame("Krzywa Béziera");
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setSize(1920, 1080);
        okno.setLocationRelativeTo(null);
        okno.add(new Main());
        okno.setVisible(true);
    }
}