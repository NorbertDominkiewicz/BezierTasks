import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class DrawingPanel extends JPanel {
    private int indexButton = 0;
    private boolean drawable = false;
    public int clickCount = 0;
    public ArrayList<PointButton> points;
    public ArrayList<ArrayList<PointButton>> curves;
    public Color drawingColor = Color.BLACK;
    public PointButton selectedPoint;
    MainPanel mainPanel;
    KeyAdapter klawiatura = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (selectedPoint != null && selectedPoint.getActive()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        selectedPoint.moveButton(-2, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        selectedPoint.moveButton(2, 0);
                        break;
                    case KeyEvent.VK_UP:
                        selectedPoint.moveButton(0, -2);
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedPoint.moveButton(0, +2);
                        break;
                }
                repaint();
            }
        }
    };

    DrawingPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        points = new ArrayList<>();
        curves = new ArrayList<>();
        setPreferredSize(new Dimension(1100, 600));
        setBackground(Color.WHITE);
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        setFocusable(true); // Ustaw panel jako focusowalny
        addKeyListener(klawiatura); // Dodaj KeyListener do panelu

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (canDraw()) {
                    if (clickCount < 4) {
                        System.out.println("Punkt: " + (clickCount + 1));
                        PointButton newPoint = new PointButton(mainPanel, e.getX(), e.getY(), indexButton);
                        points.add(newPoint);
                        add(newPoint);
                        repaint();
                        revalidate();

                        clickCount++;
                        indexButton++;

                        if (clickCount == 4) {
                            clickCount = 0;
                            setDrawable(false);
                            repaint();
                        }
                    } else {
                        System.out.println("Nie możesz dodać więcej niż 4 punkty.");
                    }
                } else {
                    System.out.println("Musisz dodać krzywą jeśli chcesz rysować");
                }
            }
        });
    }

    public void deactivateAllButtons() {
        for (PointButton point : points) {
            point.setActive(false);
            point.repaint();
        }
        selectedPoint = null;
    }

    public void setSelectedPoint(PointButton point) {
        deactivateAllButtons();
        selectedPoint = point;
        selectedPoint.setActive(true);
        requestFocusInWindow();
    }

    public boolean canDraw() {
        return drawable;
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (curves.size() > 0) {
            for (ArrayList<PointButton> curve : curves) {
                drawCurve(g, curve);
            }
        }

        if (points.size() == 4) {
            curves.add(new ArrayList<>(points));
            drawCurve(g, points);
            points.clear();
        }

    }

    public void drawCurve(Graphics g, ArrayList<PointButton> points) {
        g.setColor(drawingColor);
        for (double i = 0.0; i < 1.0; i += 0.0005) {
            double x = Math.pow(1 - i, 3) * points.get(0).getX() + 3 * i * Math.pow(1 - i, 2) * points.get(1).getX()
                    + 3 * Math.pow(i, 2) * (1 - i) * points.get(2).getX() + Math.pow(i, 3) * points.get(3).getX();
            double y = Math.pow(1 - i, 3) * points.get(0).getY() + 3 * i * Math.pow(1 - i, 2) * points.get(1).getY()
                    + 3 * Math.pow(i, 2) * (1 - i) * points.get(2).getY() + Math.pow(i, 3) * points.get(3).getY();

            g.fillRect((int) x, (int) y, 4, 4);
        }
    }

    public void drawAuthorCurve() {
        ArrayList<PointButton> allPoints = new ArrayList<>();

        allPoints.add(new PointButton(mainPanel, 98, 441, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 97, 456, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 145, 295, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 110, 107, ++indexButton));

        allPoints.add(new PointButton(mainPanel, 121, 94, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 337, 218, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 382, 392, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 372, 381, ++indexButton));

        allPoints.add(new PointButton(mainPanel, 372, 381, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 380, 399, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 329, 234, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 395, 100, ++indexButton));

        allPoints.add(new PointButton(mainPanel, 569,73, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 587,261, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 616,412, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 566,397, ++indexButton));

        allPoints.add(new PointButton(mainPanel, 570,71, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 570,56, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 996,207, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 565,395, ++indexButton));

        for (int i = 0; i < allPoints.size(); i += 4) {
            ArrayList<PointButton> authorPoints = new ArrayList<>();
            for (int j = 0; j < 4 ; j++) {
                authorPoints.add(allPoints.get(i + j));
            }
            for (PointButton point : authorPoints) {
                add(point);
            }
            curves.add(authorPoints);
        }
        repaint();
    }
}

public class MainPanel extends JPanel {
    DrawingPanel dp;
    MainPanel(Gui mainFrame) {
        dp = new DrawingPanel(this);
        setLayout(new BorderLayout());
        System.out.println(getWidth());
        setOpaque(true);
        setBackground(Globals.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(50, 50, 50, 50));
        add(dp);
    }
}
