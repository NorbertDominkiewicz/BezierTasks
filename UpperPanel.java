import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class InputRGB extends JPanel {
    public InputRGB(String text) {
        super();
        setLayout(new GridLayout(1, 2));
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Monospaced", Font.PLAIN, 30));
        JTextField value = new JTextField(5);
        value.setForeground(Color.BLACK);
        value.setFont(new Font("Monospaced", Font.PLAIN, 30));
        add(label);
        add(value);
    }
}

class ChangeColor extends JFrame {
    ChangeColor() {
        setTitle("Change Color");
        setSize(500, 300);
        setBackground(Globals.BACKGROUND_COLOR);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setVisible(true);
        InputRGB inputRed = new InputRGB("R: ");
        InputRGB inputGreen = new InputRGB("G: ");
        InputRGB inputBlue = new InputRGB("B: ");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        add(inputRed, gbc);
        gbc.gridx = 1;
        add(inputGreen, gbc);
        gbc.gridx = 2;
        add(inputBlue, gbc);
    }
}

class ShowForumla extends JFrame {
    ShowForumla() {
        setTitle("Wzór matematyczny");
        setSize(500, 300);
        setLayout(new BorderLayout());
        ImageIcon zdjecie = new ImageIcon("src/wzor.jpg");
        JLabel zdjecieLabel = new JLabel(zdjecie);
        add(zdjecieLabel, BorderLayout.CENTER);
        setBackground(Globals.BACKGROUND_COLOR);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class AddCurve extends Button {
    public AddCurve(Gui mainFrame) {
        super("Dodaj krzywą");
        addActionListener(e->{
            mainFrame.tresc.dp.setDrawable(true);
        });
    }
}

class Autor extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50,50);
    }
    public Autor() {
        setOpaque(true);
        JLabel autor = new JLabel("\tKrzywe Beziera 3-ciego stopnia\t");
        autor.setFont(new Font("Arial", Font.ITALIC, 14));
        autor.setForeground(Color.BLACK);
        add(autor);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}

public class UpperPanel extends JPanel {
    Autor autor;
    Button zmienKolor;
    Button deleteCurves;
    Button formulas;
    AddCurve dodajKrzywa;
    Button rysujInicjaly;
    UpperPanel(Gui mainFrame) {
        setOpaque(true);
        setBackground(new Color(25, 25, 25));
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(25, 0, 25, 0));
        autor = new Autor();
        zmienKolor = new Button("Zmien kolor");
        deleteCurves = new Button("Wyczyść paletę");
        formulas = new Button("Pokaż wzór");
        rysujInicjaly = new Button("Rysuj inicjaly");
        rysujInicjaly.bg = Color.BLACK;
        rysujInicjaly.setForeground(Color.yellow);
        rysujInicjaly.setFont(new Font("Monospaced", Font.BOLD, 15));
        rysujInicjaly.addActionListener(e->{
            mainFrame.tresc.dp.drawAuthorCurve();
        });
        formulas.addActionListener(e->{
            new ShowForumla();
        });
        deleteCurves.addActionListener(e->{
            mainFrame.tresc.dp.curves.clear();
            mainFrame.tresc.dp.points.clear();
            mainFrame.tresc.dp.removeAll();
            mainFrame.tresc.dp.setDrawable(false);
            mainFrame.tresc.dp.clickCount = 0;
            mainFrame.tresc.dp.repaint();
            mainFrame.tresc.dp.revalidate();
        });
        zmienKolor.addActionListener(e->{
           new ChangeColor();
        });
        dodajKrzywa = new AddCurve(mainFrame);
        add(autor);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        add(autor,c);
        c.gridx++;
        add(zmienKolor,c);
        c.gridx++;
        add(dodajKrzywa,c);
        c.gridx++;
        add(deleteCurves,c);
        c.gridx++;
        add(rysujInicjaly,c);
        c.gridx++;
        add(formulas,c);
    }
}
