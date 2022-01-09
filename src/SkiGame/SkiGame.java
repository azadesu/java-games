package SkiGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SkiGame extends JFrame implements ActionListener, KeyListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    protected static SkiGame game;
    private Renderer gameRenderer = new Renderer();
    // game objects
    protected ImageObject background = new ImageObject("src/SkiGame/images/background.png", 0, 0, 800, 800);
    protected ImageObject player = new ImageObject("src/SkiGame/images/player.png", WIDTH / 2 - 75, HEIGHT - 170, 120, 120);
    // obstacles
    protected ArrayList<ImageObject> gameObstacles = new ArrayList<>();

    // game stats
    private int speed = 8;
    private int score = 0;
    private boolean start;
    private boolean gameOver;
    private int ticks;

    // code here
    public SkiGame() {
        super("Ski Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        add(gameRenderer);
        addKeyListener(this);

        Timer timer = new Timer(1, this);
        timer.start();
        setVisible(true);
    }

    // Renderer inner class
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
        background.draw(g, this);
        player.draw(g, this);
        for (ImageObject obstacle : gameObstacles) obstacle.draw(g, this);

        // game words on the screen
        g.setColor(Color.green);
        g.setFont(new Font("Monospaced", Font.BOLD, 40));

        if (!gameOver && !start) {
            g.drawString("Press SPACE to start!", 100, HEIGHT / 2);
        }
        if (!gameOver && start) {
            g.setFont(new Font("Monospaced", Font.BOLD, 30));
            g.drawString("Score: " + score, WIDTH - 300, 50);
        }
        if (gameOver && !start) {
            g.setColor(Color.red);
            g.drawString("Game Over!", 300, HEIGHT / 2);
            g.drawString("Press SPACE to restart!", 100, HEIGHT / 2 + 50);
        }
    }

    // adds obstacles to the gameObstacles array
    public void addObstacles() {
        // only add obstaclse if there are no obstacles OR
        // if the next obstacle will have space for the player to pass through
        if (!(gameObstacles.size() == 0 || gameObstacles.get(gameObstacles.size() - 1).y >= player.height)) {
            return;
        }
        // will be the x-value of the previous obstacle's right corner
        int obstacleX = 0;
        int numObstacles = 1 + new Random().nextInt(3); //1-3 obstacles per row
        boolean nothing = false;
        for (int i = 0; i < numObstacles; i++) {
            // making new obstacle. the x-value is randomised between (obstacleX) to (WIDTH - obstacleX)
            if (obstacleX >= WIDTH) return;
            int nextX = obstacleX + new Random().nextInt(WIDTH - obstacleX);
            // we get a random obstacle
            ImageObject new_obstacle = Obstacles.getRandomObstacle();
            // and set the new x and y values
            new_obstacle.x = nextX;
            new_obstacle.y =  -1* (100 + new Random().nextInt(300));

            // checks if there's enough space for the player to pass through between two obstacles
            // if there is enough space, nothing = true.
            if (nextX - obstacleX >= 200) nothing = true;

            // the next obstacle's x-value must start after the new obstacle.
            obstacleX = nextX + new_obstacle.width;

            // if there is not enough space & the new_obstacle does not provide enough space
            // we stop adding obstacles
            if (!nothing && WIDTH - new_obstacle.x + new_obstacle.width <= 200) {
                return; // ends the method
            }
            // else, we add the obstacle.
            else {
                gameObstacles.add(new_obstacle);
            }

            // if the next obstacle is added outside of the window, stop adding obstacle.
            if (obstacleX >= WIDTH) return;
        }
    }

    public void changeBackground() {
        if (background.src.contains("background2")) {
            background.setPicture("src/SkiGame/images/background3.png");
        } else {
            background.setPicture("src/SkiGame/images/background2.png");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // update your objects properties  here
        if (start) {
            ticks = ticks + 1;
            if (ticks % 10 == 0) {
                changeBackground();
            }
            // checks if the obstacles collide with the player
            for (ImageObject obstacle : gameObstacles) {
                if (player.hitBox.intersects(obstacle.hitBox)) {
                    gameOver = true;
                    start = false;
                    return;
                }
            }
            //checks if the player has passed an obstacle
            for (int index = 0; index < gameObstacles.size(); index++) {
                // If the object exists
                if (gameObstacles.get(index) != null) {
                    ImageObject obstacle = gameObstacles.get(index);
                    if (player.y + player.height <= obstacle.y) {
                        score = score + 100;
                        // removes the obstacles that have been passed by
                        gameObstacles.remove(index);
                    }
                }
            }
            // add obstacles
            addObstacles();

            //updating the y-values for each obstacle
            for (ImageObject obstacle : gameObstacles) {
                if (obstacle != null) {
                    obstacle.y += speed;
                    obstacle.setHitBox();
                }
            }
        }
        gameRenderer.repaint();
    }

    public static void main(String[] args) {
        game = new SkiGame();
    }

    // KeyListener Methods
    @Override
    public void keyPressed(KeyEvent e) {
        int stepSize = 20;
        if (start) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                // the player is at the wall
                if (player.x >= WIDTH - player.width - stepSize) {
                    player.x = WIDTH - player.width; // the player stays on the wall
                } else {
                    player.x += stepSize; // the player moves right
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                // player is at the wall
                if (player.x - stepSize < 0) {
                    player.x = 0; // player stays on the wall
                } else {
                    player.x -= stepSize; //player moves left
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // only allow reset if the game has ended/ the game has just begun
            if (!start || gameOver) reset();
        }
        player.setHitBox();
    }

    public void reset() {
        // game stats
        start = true;
        gameOver = false;
        //resetting obstacles
        gameObstacles.clear();

        // player position & score
        player.x = WIDTH / 2 - 100;
        score = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

