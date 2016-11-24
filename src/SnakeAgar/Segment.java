package SnakeAgar;
import java.awt.Color;
import java.awt.Graphics;

// segments run as threads and are used to make up the snakes body
// when the snake eats a food object the snake adds a new segment taking
// in the colour and rating of the food object
public class Segment implements Comparable<Segment> {
    private int size;
    private int x;
    private int y;
    private int value;
    private Color colour;
    
    //takes in starting postion and colour and size of snake
    public Segment(int startX, int startY, int size, Color colour) {
        this.size = size;
        this.x = startX;
        this.y = startY;
        this.value = value;
        this.colour = colour;
    }
    //draws the segment with x,y in the middle of size value
    public void drawSegment(Graphics g) {
        g.setColor(colour);
        g.fillOval(x-(size/2),(y-size/2),size,size);
        g.setColor(Color.BLACK);
        g.drawOval(x-(size/2),(y-size/2),size,size);
    }   
    // getters and setters
    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    @Override
    public int compareTo(Segment o) {
        return (x-o.x)+(y-o.y);
    }
}