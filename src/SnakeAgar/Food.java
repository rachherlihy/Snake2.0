package SnakeAgar;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

// food objects run as threads and move around the panel in random directions
// while remaining within the panel boundaries. Food objects are given ratings
// which are also used to dictate their size
public class Food implements Runnable, Comparable<Food> {
    private int x;
    private int y;
    private int panelWidth;
    private int panelHeight;
    private int rating;
    private Random random;
    private Color color;
    private int size = 20;
    private int numMovements;
    private int MAX_MOVES = 30;
    public enum Direction{UP,DOWN,LEFT,RIGHT};
    private Direction direction;
    public static int move=1;
    private boolean alive;
    private int SLEEP = 25;
    
    // constructor takes in panel width and height as well as rating which
    // is used to influence the food size. Direction and colour randomized.
    public Food(int panelW, int panelH, int rating) {   
        random = new Random();
        this.panelHeight = panelH;
        this.panelWidth = panelW;
        x = random.nextInt(panelW-(2*size))+size;
        y = (int)((random.nextInt(panelH-(2*size))+size)/1.5);
        this.rating = rating;
        if(rating < 0) {    
            this.rating = random.nextInt(10);
        }
        color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        size += 2*rating;
        int newDirection = random.nextInt(4);
        System.out.println("newDirection is "+newDirection);
        switch(newDirection) {
            case 0: direction = Direction.DOWN; break;
            case 1: direction = Direction.LEFT; break;
            case 2: direction = Direction.RIGHT; break;
            default:  direction = Direction.UP; break;
        }
    }
    
    @Override
    public void run() {   
        alive = true;
        while(alive) {
            moveFood();
            try {
               Thread.sleep(SLEEP);
            } catch(Exception e){}
        }
    }
    public void killFood() {
        alive = false;
    }
    
    // moves food in specified direction
    private synchronized void moveFood() {
        if(direction == Direction.UP) {
            y-=move;
        }
        else if(direction == Direction.DOWN) {
            y+=move;
        }
        else if(direction == Direction.LEFT) {
            x-=move;
        }
        else if(direction == Direction.RIGHT) {
            x+=move;
        }
        // checks if food hits wall, and moves in opposite direction
        // resets move count to 0
        if(x-(size/2) < 0) {
            direction = Direction.RIGHT;
            numMovements = 0;
        }    
        else if((x+size/2) > panelWidth) {
            direction = Direction.LEFT;
            numMovements = 0;
        }
        else if(y-(size/2) < 0) {
            direction = Direction.DOWN;
            numMovements = 0;
        }    
        else if((y+size/2) > panelHeight) {
            direction = Direction.UP;
            numMovements = 0;
        }    
        numMovements++;
        // changes food direction at random after several moves
        if(numMovements > MAX_MOVES) {
            int newDirection = random.nextInt(4);
            numMovements = 0;
            switch(newDirection) {
                case 0: direction = Direction.DOWN; break;
                case 1: direction = Direction.LEFT; break;
                case 2: direction = Direction.RIGHT; break;
                default:  direction = Direction.UP; break;
            }
        }
    }
    public synchronized void drawFood(Graphics g) { 
        g.setColor(color);
        g.fillRect(x-(size/2),(y-size/2),size,size);
        g.setColor(Color.WHITE);
        g.drawString(""+rating, x, y);
        g.setColor(Color.BLACK);
        g.drawRect(x-(size/2),(y-size/2),size,size);
    }
    // getters
    public int getRating() {
        return rating;
    }
    public Color getColour() {
        return color;
    }
    public int getSize() {
        return size;
    }
    public int getX() {
        return x;
    }
     public int getY() {
        return y;
    }
     
    @Override
    public int compareTo(Food o) {
        return rating - o.rating;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof Food) {
            Food f = (Food)o;
            return (f.getRating() == rating && f.getColour() == color);
        }
        else return false;
    }
}