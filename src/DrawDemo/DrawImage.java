package DrawDemo;

import java.awt.*;
import javax.swing.JFrame;

public class DrawImage extends JFrame {

    public DrawImage() {
        super();
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.red);
    }

    public void paint(Graphics g) {
        g.fillRect(0,0,400,400);
        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = t.getImage("src/SkiGame/heart.png");
        g.drawImage(i, 20, 20, this);
    }

    public static void main(String[] args) {
        new DrawImage();
    }

}  