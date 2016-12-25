/**************************************************************************************************************
  * Assignment: ICS Summative
  * Description of class: This is the panel on which all aspects of the game take place
  * 
  * Author of Class: #Borna Houmani-Farahani
  * Last Edited: June. 12, 2016
  * Course: ICS3U1
  ***************************************************************************************************************/
package game;

//importing required packages
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements Runnable, KeyListener
{ 
  //#variable
  /*************************************************************************************************************
    * Variable Dictionary
    * GameStateManager manage - creates an instance of the GameStateManager class in order to communicate
    *                           between and switch states (i.e. menu to gameplay)
    * int WIDTH - width of the panel (matches the frame)
    * int HEIGHT - height of the panel (matches the frame)
    * Thread play - thread that runs animations
    * boolean running - signals that the program has launched, while it is true, the game loop is working
    * int FPS - stores the frames per second
    * long targetTime - stores the ideal number of miliseconds between frames (based on FPS)
    * long start - stores the nanosecond at the start of the frame
    * long elapsed - stores the nanosecond at the end of the frame
    * long wait - stores the miliseconds to delay the thread in order to cap the FPS at 30
    *************************************************************************************************************/
  
  //declaring #variables
    //creates instance of GameStateManger
  private GameStateManager manage = new GameStateManager();
    //these are the window dimensions
  public static final int WIDTH = 960;
  public static final int HEIGHT = 508;
    //creating the game thread & required variables - heart of animation
  private Thread play;
  private boolean running;
  private int FPS = 30;
  private long targetTime = 1000 / FPS; 
  private long start, elapsed, wait; 
  
  public Board()
  {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));//sets the board at the same dimensions as the frame
    init();//calls initialization #method
  }
  
  //#method initializes thread, keyListener, and sets running to true
  private void init()
  {
    play = new Thread(this);
    play.start();
    addKeyListener(this);
    setFocusable(true);//gives the Board the focus for input
    running = true;
  }//ends initialization tasks
  
  /*
   * Within run() is the game loop. Implementing Runnable allows it to be called by starting the Thread play.
   * In order to have all parts of the game running from the same loop (menu, gameplay, etc.) the game loop 
   * just calls the game state manager which calls the appropriate game state
   */
  public void run()
  {
    while (running) //overarching game #loop 
    {
      start = System.nanoTime(); //retrieves the nanotime of the system at the start of the frame
      
      tick(); //calls the tick method in this class
      repaint(); //calls the draw method in the paint component method 
      
      elapsed = System.nanoTime() - start; //retrieves the nanotime of the system at the end of the frame
      wait = targetTime - elapsed / 1000000; //calculates the amount of time the program should be delayed to cap it at 30 FPS
                                             //divide elapsed by 1 million to transfer from nanoseconds to miliseconds
      
      //if the program is moving at faster than 30 FPS, delays it
      //this caps the program at 30 FPS so it doesn't run too fast on strong computers
      if (wait < 0)
        wait = 2;
      try
      {
        Thread.sleep(wait);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }//ends game loop
  }//ends run
  
  //#methods required for running the game, actions are taken based on these methods every frame
  //all these methods call the GameStateManager class which then calls whichever state is currently active
  public void tick() //tick for updating variables every frame
  {
    manage.tick();
  }
  public void paintComponent(Graphics g) //draw for redrawing the screen every frame
  {
    super.paintComponent(g);
    g.clearRect(0,0,WIDTH,HEIGHT);
    manage.draw(g);
  }
  public void keyPressed(KeyEvent e) //keys pressed
  {
    manage.keyPressed(e.getKeyCode());
  }
  public void keyReleased(KeyEvent e) //keys released
  {
    manage.keyReleased(e.getKeyCode());
  }
  public void keyTyped(KeyEvent e) {} //keys typed (not used but required to still be called while using KeyListener)
  
}//end Board
