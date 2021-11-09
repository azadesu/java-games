package SkiGame;

import java.util.*;

public class Obstacles {
    protected static ImageObject rock = new ImageObject("src/SkiGame/images/rock.png",0, 0, 100, 70);
    protected static ImageObject tree = new ImageObject("src/SkiGame/images/tree.png",0, 0, 100, 140);
    protected static ImageObject box = new ImageObject("src/SkiGame/images/box.png", 0, 0, 120, 120);
    protected final static ArrayList<ImageObject> obstacles = new ArrayList<>(Arrays.asList(rock, tree, box));


    public static ImageObject getRandomObstacle() {
        int index = new Random().nextInt(obstacles.size());
        return obstacles.get(index).newCopy();
    }
}
