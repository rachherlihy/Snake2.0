package SnakeAgar;
import java.awt.Color;
import static java.awt.Color.*;
import java.awt.Graphics;
import java.util.Iterator;

public class Snake implements Runnable, Comparable<Snake> {
    public int movement = 2;
    private int numElements = 1;
    private Deque segments;
    private boolean alive;
    private Color c;
    private int x;
    private int y;
    private int SIZE;
    private int SLEEP = 25;
    public enum Direction{UP,DOWN,LEFT,RIGHT};
    private Direction direction;
    
    // constructor creates the snake and set its position 
    // in the lower centre of the panel
    public Snake(int panelWidth, int panelHeight) {
        segments = new Deque();
        alive = true;
        direction = Direction.UP;
        movement = movement; 
        x = panelWidth/2;
        y = 750;
        SIZE = 25;
        c = pink;
    }
    
    public void run() {   
        alive = true;
        while(alive) {
            moveSnake();
            try {
               Thread.sleep(SLEEP);
            } catch(Exception e){}
        }
    }
    // moving all snake segments smoothly based on user input of direction
    private synchronized void moveSnake() {
        Iterator iterator = segments.iterator(true);
        int prevX = getX();
        int prevY = getY();
        
        // checks to see snake is touching edge of the panel
        // kills snake if so
        if((getX()-SIZE < 0) || (getX()+SIZE > 800) ||
          (getY()-SIZE < 0) || (getY()+SIZE > 800))   {
            killSnake();
        }
        // updates snakes movement based on direction
        if(direction == Direction.UP) {
            y-=movement;
        }
        else if(direction == Direction.DOWN) {
            y+=movement;
        }
        else if(direction == Direction.LEFT) {
            x-=movement;
        }
        else if(direction == Direction.RIGHT) {
            x+=movement;
        }
        
        // iterate over body updating each position with that of
        // previous body position)    
        while(iterator.hasNext()) {
            // set segment to previous x and y positions
            Segment segment = (Segment) iterator.next();  
            int segX = segment.getX();
            int segY = segment.getY();
            segment.setX(prevX);
            segment.setY(prevY);
            prevX = segX;
            prevY = segY;
        }  
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void killSnake() {
        alive = false;
    }
    
    public synchronized int getX() {
        return this.x;
    }
    
    public synchronized int getY() {
        return this.y;
    }
    
    public synchronized void setDirection(Direction direction) {
        this.direction = direction;
    }
    // checks to see if head of snake hit food
    public boolean hitFood(Food food) {
        if((Math.pow((getX() - food.getX()),2)) + (Math.pow((getY() - food.getY()),2)) 
            < (Math.pow(((food.getSize() + (double) SIZE)/2),2))) {
            System.out.println("hitFood called on: "+food.getRating());
            return true;
        } else
        return false;
    }
    // checks if snake can eat food within range based on rating
    // adds to snakes body if so
    public boolean eatFood(Food food) {
        if(numElements >= food.getRating()) {
            Color c = food.getColour();
            for(int i = 0; i < food.getRating(); i++) {
                Segment segment = new Segment(getX(), getY(), SIZE, c);
                segments.enqueueFront(segment);
            }
            numElements++;
            return true;    
        }
        else
            return false;            
    }
    
    public synchronized void drawSnake(Graphics g) {
        Iterator iterator = segments.iterator(true);
        g.setColor(c);
        g.fillOval(x-(SIZE/2),(y-SIZE/2),SIZE,SIZE);
        g.drawOval(x-(SIZE/2),(y-SIZE/2),SIZE,SIZE);
        while(iterator.hasNext()) {
            Segment segment = (Segment) iterator.next();
            segment.drawSegment(g);
        }
    }
    
    @Override
    public int compareTo(Snake o) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
