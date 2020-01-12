import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String args[]){
        JFrame f = new JFrame();
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setTitle("Shooting Spaceships");
        f.setLayout(new BorderLayout());
        f.add(new Fase(), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }
}
