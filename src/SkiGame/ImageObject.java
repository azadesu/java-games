package SkiGame;

import javax.swing.*;
import java.awt.*;

public class ImageObject {
    // attributes
    protected Image i;
    protected String src;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Rectangle hitBox;

    public ImageObject(String src, int x, int y, int width, int height) {
        Toolkit t = Toolkit.getDefaultToolkit();
        this.i = t.getImage(src);
        this.src = src;
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        // we add padding to allow some errors made by the player
        int padding = 20;
        this.hitBox = new Rectangle(x+padding, y+padding, width-padding*2, height-padding*2);
    }

    public void draw(Graphics g, JFrame frame) {
        g.drawImage(i, x, y, frame);
    }

    public ImageObject newCopy() {
        return new ImageObject(src, x, y, width, height);
    }

    public void setHitBox(){
        hitBox.x = x+10;
        hitBox.y = y+10;
    }

    public void setPicture(String src){
        Toolkit t = Toolkit.getDefaultToolkit();
        this.i = t.getImage(src);
        this.src = src;
    }
}
