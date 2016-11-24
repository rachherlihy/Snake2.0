package SnakeAgar;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import javax.swing.*;

public class SnakeGameGui extends JPanel implements ActionListener,KeyListener {
    private Deque foodDeque = new Deque();
    Deque snakeDeque = new Deque();
    private Food food;
    private Snake snake;
    public final static int PANEL_WIDTH = 800;
    public final static int PANEL_HEIGHT = 800;
    public final static int NUM_FOOD = 15;
    private final DrawPanel drawPanel;
    private final JButton restartButton;
    private final JRadioButton normal, hard, extreme;
    private static JFrame frame;
    public Timer timer;

    public SnakeGameGui() {
        super(new BorderLayout());
        drawPanel = new DrawPanel();
        timer = new Timer(50,this);
        
        // create grided panel for control options
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(5,1));
        
        // create buttons to adjust speed
        normal = new JRadioButton("normal");
        normal.setActionCommand("normal");
        normal.setSelected(true);
        hard = new JRadioButton("hard");
        hard.setActionCommand("hard");
        extreme = new JRadioButton("extreme");
        extreme.setActionCommand("extreme");             
        ButtonGroup buttons = new ButtonGroup();
        buttons.add(normal);
        buttons.add(hard);
        buttons.add(extreme);
        // add action listeners to update based on user selections        
        normal.addActionListener(this);
        hard.addActionListener(this);
        extreme.addActionListener(this);
        optionsPanel.add(new Label("Difficulty:"));
        optionsPanel.add(normal);
        optionsPanel.add(hard);
        optionsPanel.add(extreme);
        
        // create restart button and action listener
        restartButton = new JButton("Restart");
        restartButton.addActionListener(this);
        optionsPanel.add(restartButton);
        
        timer.start();
        add(drawPanel,BorderLayout.CENTER);
        add(optionsPanel,BorderLayout.EAST);
        start();  
    }
   
    public void start() {
        foodDeque.clear();
        snakeDeque.clear();
        
        for(int i = 1; i < 25; i++) {
            food = new Food(PANEL_WIDTH, PANEL_HEIGHT, i);
            Thread thread = new Thread(food);
            foodDeque.enqueueFront(food);
            thread.start();
        }  
        snake = new Snake(PANEL_WIDTH, PANEL_HEIGHT);
        Thread snakeThread = new Thread(snake);
        snakeDeque.enqueueFront(snake);
        snakeThread.start(); 
    }
   
   @Override
   public synchronized void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source == timer) {  
            drawPanel.repaint();
            Iterator iterator = foodDeque.iterator(true);
            while(iterator.hasNext()) {
                Food foodIt = (Food) iterator.next();
                
                // checks if snake can eat food, if true removes from food list
                // adds to snake in the eatFood method
                if(snake.hitFood(foodIt)) {
                    if(snake.eatFood(foodIt)) {
                        foodIt.killFood();
                        if(foodDeque.numElements > 1) {
                            foodDeque.dequeueRear();
                        }
                        else {
                            snake.killSnake();
                            foodDeque.clear();
                            JOptionPane.showMessageDialog(drawPanel, "You Win!");
                            return;
                        }
                    }
                    // if snake cannot eat food hit snake is killed, game ends
                    else {
                        snake.killSnake();
                        foodDeque.clear();
                        JOptionPane.showMessageDialog(drawPanel, "Game Over!");
                        return;
                    }  
                }
                // snake has gone out of bounds
                else if(!snake.isAlive()) {
                    foodDeque.clear();
                    JOptionPane.showMessageDialog(drawPanel, "Game Over!");
                    return;
                }
            }            
            drawPanel.repaint();             
        }
      
        if(source == restartButton) {   
            // gives focus back to the keyboard and clears lists to start new game
            JOptionPane.showMessageDialog(drawPanel, "Restarting Game",
                                    "SNAKE GAME" , JOptionPane.INFORMATION_MESSAGE);
            frame.setFocusable(true);
            frame.requestFocusInWindow(); 
            foodDeque.clear();
            snakeDeque.clear();
            start();
        }
      
        if(source == normal) {
            frame.setFocusable(true);
            frame.requestFocusInWindow(); 
            food.move = 1;
        }
        if(source == hard) {
            frame.setFocusable(true);
            frame.requestFocusInWindow(); 
            food.move = 2;  
        }
        if(source == extreme) {
            frame.setFocusable(true);
            frame.requestFocusInWindow(); 
            food.move = 4;          
        }
    }
    // draws the gui panel
    private class DrawPanel extends JPanel {    
        public DrawPanel(){
            super();
            setPreferredSize(new Dimension(PANEL_WIDTH ,PANEL_HEIGHT));
            setBackground(Color.WHITE);    
        }
        // used to draw the snake and food
        @Override
        public void paintComponent(Graphics g) {    
            super.paintComponent(g); 
            Iterator iterator = foodDeque.iterator(true); 
            while(iterator.hasNext()) {
                food = (Food) iterator.next();
                food.drawFood(g);  
            }
            if(snake.isAlive()) {
                snake.drawSnake(g); 
            }
        } 
    }
    // checking for user input and updating snake direction
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W) {   
             System.out.println("UP PRESSED");
             snake.setDirection(Snake.Direction.UP);
             drawPanel.repaint();     
        }
        else if(e.getKeyCode() == KeyEvent.VK_S) {  
            System.out.println("DOWN PRESSED");
            snake.setDirection(Snake.Direction.DOWN);
            drawPanel.repaint();
        }
        else if(e.getKeyCode() == KeyEvent.VK_A) { 
            System.out.println("LEFT PRESSED");
            snake.setDirection(Snake.Direction.LEFT);
            drawPanel.repaint();
        }
        else if(e.getKeyCode() == KeyEvent.VK_D) {  
            System.out.println("RIGHT PRESSED");
            snake.setDirection(Snake.Direction.RIGHT);
            drawPanel.repaint();
        }
        else System.out.println("SOME DIFFERENT KEY!");
    }
    // ignore
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
    public static void main(String[] args) {   
        System.out.println("============SNAKE===============");
        SnakeGameGui game = new SnakeGameGui();
        frame = new JFrame("SNAKE MEETS AGAR.IO GAME");
        frame.setFocusable(true);
        frame.addKeyListener(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(game);
        // use to get screen dimensions and calculate positions
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int screenHeight = dimension.height;
        int screenWidth = dimension.width;
        // resize frame
        frame.pack();
        // position frame in center of screen
        frame.setLocation(new Point((screenWidth/2)-(frame.getWidth()/2),
           (screenHeight/2)-(frame.getHeight()/2)));
        frame.setVisible(true);
    }
}