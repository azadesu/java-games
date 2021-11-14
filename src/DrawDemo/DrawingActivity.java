package DrawDemo;

import javax.swing.JFrame;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
public class DrawingActivity extends JFrame{
    private Rectangle tears = new Rectangle(100,150,5,125);
    public DrawingActivity() {
        super("Drawing Activity");
        setSize(400,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.LIGHT_GRAY);
    }

    // paint() get called automatically at runtime.
    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.fillOval(75, 100, 50, 50); // eyes (left) 125+275
        g.fillOval(275, 100, 50, 50); // eyes (right)
        g.fillRect(190, 150, 25, 100); // nose
        g.fillRect(100, 300, 200, 25); // mouth

        g.setColor(Color.BLUE);

        g.setFont(new Font("Sans Serif", Font.BOLD, 30));
        g.drawString("i'm sad", 150,360);

        g.fillRect(tears.x, tears.y, tears.width, tears.height); // left tear
        tears.x = tears.x+200;
        g.fillRect(tears.x, tears.y, tears.width, tears.height); // right tear
    }

    public static void main(String[] args){
        new DrawingActivity();
    }
}
