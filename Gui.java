import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    UpperPanel sterowanie;
    MainPanel tresc;
    Gui(){
        setSize(Globals.FRAME_WIDTH, Globals.FRAME_HEIGHT);
        getContentPane().setBackground(Globals.BACKGROUND_COLOR);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        sterowanie = new UpperPanel(this);
        tresc = new MainPanel(this);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.EAST;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0;
        add(sterowanie, gc);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridy++;
        gc.weighty = 1;
        gc.weightx = 1;
        add(tresc, gc);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Gui();
    }
}
