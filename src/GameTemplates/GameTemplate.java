package GameTemplates;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GameTemplate extends JFrame implements ActionListener, KeyListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    protected static GameTemplate game;
    private Renderer gameRenderer = new Renderer();
    // game objects
    // code here

    // game stats
    // code here
    public GameTemplate() {
        super("GameTemplate Title");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        add(gameRenderer);
        addKeyListener(this);

        Timer timer = new Timer(20, this);
        timer.start();
        setVisible(true);
    }

    public class Renderer extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            // paints the component, g
            super.paintComponent(g);
            // paints g onto the window
            if (game != null) game.repaint(g);
        }
    }
    public void repaint(Graphics g) {
        //draw your objects here
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // update your objects properties  here
        gameRenderer.repaint();
    }

    public static void main(String[] args) { game = new GameTemplate(); }

    // KeyListener Methods
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
