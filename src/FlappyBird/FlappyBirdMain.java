package FlappyBird;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Font;
public class FlappyBirdMain extends JFrame implements ActionListener, KeyListener {
    protected static FlappyBirdMain flappyBird;
    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    private Renderer renderer = new Renderer();
    private Random rand = new Random(); //a randomiser that will help us generate random numbers

    //game objects
    private Rectangle bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
    private ArrayList<Rectangle> columns = new ArrayList<>(); // our columns in the game

    //game stats
    private int ticks;
    private int yMotion;
    private int score;
    private boolean started;
    private boolean gameOver;

    public FlappyBirdMain() {
        super("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);

        addKeyListener(this);
        add(renderer);

        addColumn(true);
        addColumn(true);

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
            if (flappyBird != null) flappyBird.repaint(g);
        }
    }


    public void jump() {
        // when the program first runs, started = false
        if (!started) { started = true; }
        if (gameOver) {
            // we reset all the values if gameOver
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            addColumn(true);
            addColumn(true);
            gameOver = false;
            started = false;
            score = 0;
        } else {
            // the bird is still winning
            if (yMotion > 0) {
                yMotion = 0;
            } // removing gravity
            yMotion -= 10;
        }
    }

    //adds columns and randomising column lengths
    public void addColumn(boolean start) {
        int space = 200; // space for the bird to move through
        int grassHeight = 120;
        int distance = 300;
        int width = 100; // of each column
        int lowerHeight = 50 + rand.nextInt(distance); // (50-350 height)
        int upperHeight = HEIGHT - lowerHeight - space - grassHeight; // the height of the other column
        int lowerY = HEIGHT - lowerHeight - grassHeight; // 120 from grass height

        if (start) {
            int startingLocation_x = WIDTH + width + columns.size() * 300;
            columns.add(new Rectangle(startingLocation_x, 0, width, upperHeight)); // upper
            columns.add(new Rectangle(startingLocation_x, lowerY, width, lowerHeight)); // lower
        } else {
            int newColumn_x = columns.get(columns.size() - 1).x + distance * 2;
            columns.add(new Rectangle(newColumn_x, 0, width, upperHeight)); //upper
            columns.add(new Rectangle(newColumn_x, lowerY, width, lowerHeight)); //lower
        }
    }



    // method for painting the column onto the window
    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ticks = ticks + 1;
        int speed = 10;

        if (started) {
            // moving all the rectangles leftwards
            for (Rectangle column : columns) {
                column.x -= speed;
            }

            //moving the bird downwards (gravity)
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion = yMotion + 2; //velocity
            }
            bird.y += yMotion;

            // checking columns
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                //remove the column once the columns exit the window
                if (column.x < 0) {
                    columns.remove(column);
                    // makes sure the method is called onnly once
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }

                // Scoring
                // checks if the bird passes the right corner of the upper column.
                if (column.y == 0 && bird.x == column.x + column.width) {
                    score = score + 1;
                }

                // GameOver mechanics
                // if the bird collides with the column
                if (column.intersects(bird)) {
                    gameOver = true;
                    if (bird.x <= column.x) bird.x = column.x - bird.width;
                    else {
                        if (column.y != 0) bird.y = column.y - bird.height;
                        else if (bird.y < column.height) bird.y = column.height;
                    }
                }
                //if the bird goes upwards
                if (bird.y < 0) {
                    gameOver = true;
                }
                // if the bird touches the ground
                if (bird.y + bird.height >= HEIGHT - 120) {
                    bird.y = HEIGHT - 120 - bird.height;
                    gameOver = true;
                }
            }
        }
        renderer.repaint(); // will run every <delay> ms
    }



    public void repaint(Graphics g) {
        //drawing the background
        g.setColor(Color.cyan); //setting the brush to a certain color
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.orange.darker()); //soil
        g.fillRect(0, HEIGHT - 100, WIDTH, 100);
        g.setColor(Color.green); //grass
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        // drawing the bird
        g.setColor(Color.red); //bird
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        // paint column
        for (Rectangle column : columns) {
            paintColumn(g, column);
        }

        // game words on the screen
        g.setColor(Color.white);
        g.setFont(new Font("Sans Serif", Font.BOLD, 100));
        if (!started) {
            g.setFont(new Font("Sans Serif", Font.BOLD, 60));
            g.drawString("Press SPACE to start!", 50, HEIGHT / 2);
        }
        if (gameOver) {
            g.drawString("Game Over!", 75, HEIGHT / 2);
        }
        if (!gameOver && started) {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }

    public static void main(String[] args){ flappyBird = new FlappyBirdMain();   }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
}








