package GameTemplates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameInputDemo extends JFrame implements ActionListener, KeyListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    protected static GameInputDemo game;

    private Renderer gameRenderer = new Renderer();
    private final int STEP_SIZE = 30;

    // game objects
    private Rectangle player = new Rectangle(WIDTH / 2, HEIGHT / 2, STEP_SIZE, STEP_SIZE);
    private Rectangle bluePaint = new Rectangle(WIDTH / 3, HEIGHT / 2 - STEP_SIZE, STEP_SIZE, STEP_SIZE);
    private Rectangle pinkPaint = new Rectangle(WIDTH * 2 / 3, HEIGHT / 2 - STEP_SIZE, STEP_SIZE, STEP_SIZE);

    //game stats
    private boolean blue;

    public GameInputDemo() {
        super("GameInput Demo");
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
        g.setColor(Color.pink);
        g.fillRect(pinkPaint.x, pinkPaint.y, pinkPaint.width, pinkPaint.height);
        g.setColor(Color.blue);
        g.fillRect(bluePaint.x, bluePaint.y, bluePaint.width, bluePaint.height);

        if (blue == false) {
            g.setColor(Color.pink);
        }
        g.fillRect(player.x, player.y, player.width, player.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (player.intersects(bluePaint)) {
            blue = true;
        } else if (player.intersects(pinkPaint)) {
            blue = false;
        }
        gameRenderer.repaint();
    }

    public static void main(String[] args) { game = new GameInputDemo(); }

    // KeyListener Methods
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.x = player.x - STEP_SIZE;

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.x = player.x + STEP_SIZE;

        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            player.y = player.y - STEP_SIZE;

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player.y = player.y + STEP_SIZE;

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
