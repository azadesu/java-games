package BrickBreakerGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BrickBreakerGame extends JFrame implements ActionListener, KeyListener {
    private final int WIDTH = 700;
    private final int HEIGHT = 600;
    protected static BrickBreakerGame game;
    private Renderer gameRenderer = new Renderer();

    // game objects
    private MapGenerator map;
    // player & ball objects
    private Rectangle player = new Rectangle(310, 550, 100, 8);
    private Rectangle ball = new Rectangle(120, 350, 20, 20);

    // game stats
    private boolean start = false;
    private boolean gameOver = false;
    private int score = 0;

    // game properties
    private final int gameSpeed = 1; // normal speed
    private final int rows = 6;
    private final int columns = 8;
    private int totalBricks;

    //ball stats
    private int ballXdir = -gameSpeed;
    private int ballYdir = -gameSpeed * 2;

    public BrickBreakerGame() {
        super("BrickBreaker Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        add(gameRenderer);
        addKeyListener(this);

        map = new MapGenerator(rows, columns);
        totalBricks = map.getTotalBricks();

        Timer timer = new Timer(8, this);
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
        // drawing game objects
        // background
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // drawing map
        map.draw(g);

        // the player
        g.setColor(Color.green);
        g.fillRect(player.x, player.y, player.width, player.height);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);

        // draws the score if the game is in motion
        if (start){
            g.setColor(Color.white);
            g.setFont(new Font("Serif", Font.BOLD, 25));
            g.drawString(String.valueOf(score), 590, 30);
        }

        // drawing the text
        if (totalBricks <= 0 || gameOver) {
            start = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setFont(new Font("Serif", Font.BOLD, 30));
            // when the player wins the game
            if (totalBricks <= 0) {
                g.setColor(Color.BLUE);
                g.drawString("You Won,  Score: " + score, 190, 300);

            } else {
                // when the player loses the game
                g.setColor(Color.RED);
                g.drawString("Game Over, Score: " + score, 190, 300);
            }
            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press (SPACE) to Restart", 230, 350);
        } else if (!start) {
            // when the player just opened the app
            g.setColor(Color.BLUE);
            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press (SPACE)  to start the game", 220, 350);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (start) {
            ballBounce(ball, player);

            // check map collision with the ball
            boolean hitBrick = false; //denotes whether a ball has hit a brick
            // loops every brick in the array. stops looping when the ball...
            // hits a brick OR when the end of the array is reached
            for (int row = 0; row < map.getRows() && !hitBrick; row++) {
                for (int col = 0; col < map.getColumns() && !hitBrick; col++) {
                    // if the brick is still on the map
                    if (map.map[row][col] == MapGenerator.ON_SCREEN) {
                        // making a brick Rectangle object to check intersect
                        int brickWidth = map.getBrickWidth();
                        int brickHeight = map.getBrickHeight();
                        int brickX = col * brickWidth + 80;
                        int brickY = row * brickHeight + 50;
                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        //if the ball hits the brick
                        if (ball.intersects(brickRect)) {
                            // the brick is removed from the array
                            map.setBrickValue(MapGenerator.OFF_SCREEN, row, col);
                            // score is added
                            score = score + 5;
                            // total bricks -1
                            totalBricks = totalBricks - 1;

                            ballBounce(ball, brickRect);

                            hitBrick = true; //we make both for loops end
                            // only one brick can intersect the ball at any time
                        }
                    }
                }
            }

            // if ball hits left/right
            if (ball.x <= 0 || ball.x >= WIDTH-ball.width) {
                ballXdir = -ballXdir;
            }
            // if ball hits ceiling
            if (ball.y <= 0) {
                ballYdir = -ballYdir;
            }
            // if ball hits the ground
            if (ball.y > HEIGHT) {
                gameOver = true;
            }

            // updating ball location
            ball.x += ballXdir;
            ball.y += ballYdir;
        }
        gameRenderer.repaint();
    }

    public static void main(String[] args) { game = new BrickBreakerGame(); }

    // KeyListener Methods
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            // the player is at the wall
            if (player.x >= WIDTH - player.width - 20) {
                player.x = WIDTH - player.width; // the player stays on the wall
            } else {
                player.x += 20; // the player moves right
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // player is at the wall
            if (player.x - 20 < 0) {
                player.x = 0; // player stays on the wall
            } else {
                player.x -= 20; //player moves left
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // only allow reset if the game has ended/ the game has just begun
            if (!start) reset();
        }
    }

    // resets the game values
    public void reset() {
        // game stats
        start = true;
        gameOver = false;
        // ball stats
        ball.x = 120;
        ball.y = 350;
        ballXdir = -gameSpeed;
        ballYdir = -gameSpeed * 2;
        // player position & score
        player.x = 310;
        score = 0;
        // map
        map = new MapGenerator(rows, columns);
        totalBricks = map.getTotalBricks();
    }


    public void ballBounce(Rectangle ball, Rectangle object){
        int objectPiece = object.width/4;
        // left side of the player bar
        if (ball.intersects(new Rectangle(object.x, object.y, objectPiece, object.height))) {
            ballYdir = -ballYdir; //goes opposite
            ballXdir = -gameSpeed; // goes left
        }
        // right side of the player bar
        else if (ball.intersects(new Rectangle(object.x + objectPiece*3, object.y, objectPiece, object.height))) {
            ballYdir = -ballYdir; // goes opposite
            ballXdir = gameSpeed; // goes right
        }
        // middle of the bar
        else if (ball.intersects(new Rectangle(object.x + objectPiece, object.y, objectPiece*2, object.height))) {
            ballYdir = -ballYdir; //goes opposite
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
