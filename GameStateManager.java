/**************************************************************************************************************
  * Assignment: ICS Summative
  * Description of class: This class manages the states of the games. It receives info from the panel (Board)
  *                       and calls the #methods (tick, draw, etc.) of the current state
  * 
  * Author of Class: #Borna Houmani-Farahani
  * Last Edited: June. 12, 2016
  * Course: ICS3U1
  ***************************************************************************************************************/
package game;

//importing required packages
import java.awt.*;
import java.awt.event.*;

public class GameStateManager
{
  //#variable
  /*************************************************************************************************************
    * Variable Dictionary
    * int currentState - holds an arbitrary value between 0 and 3 representing the current state
    * MainMenu mainM - instance of the MainMenu state whos methods can be called by this class
    * LevelSelection lvlS - instance of the LevelSelection state whos methods can be called by this class
    * Gameplay gameP - instance of the Gameplay state whos methods can be called by this class
    * WordPuzzle wordP - instance of the WordPuzzle state whos methods can be called by this class
    *************************************************************************************************************/
  
  //declaring #variables
  public static int currentState;
    //declaring the various states of the game
  private static MainMenu mainM; 
  private static Gameplay gameP;
  private static LevelSelection lvlS;
  private static WordPuzzle wordP;
  
  public GameStateManager()
  {
    //begins by creating an instance of the main menu which is the starting point of the game
    mainM = new MainMenu(this);
    currentState = 0;//sets currentState to 0 to represent that the main menu is active
  }
  
  //#method that creates a new state instances and changes currentState when a change of state is required
  //#condition if statements used to 
  public void setState(int state, int level, boolean cheat)
  {
    if (state == 1)//1 represents the level selection
    {
      lvlS = new LevelSelection(this);
      currentState = 1;
    }
    if (state == 2)//2 represents the gameplay state
    {
      gameP = new Gameplay(level, cheat);
      currentState = 2;
    }
    if (state == 3)//3 represents the puzzle state
    {
      wordP = new WordPuzzle(cheat);
      currentState = 3;
    }
  }//end setState 
  
  //game running #methods
  //this class calls the methods of the appropriate states based on the currentState variable
  //#condition if statements are used to identify which state to tick, draw, get input in
  public void tick()
  {
    if (currentState == 0)
      mainM.tick();
    else if (currentState == 1)
      lvlS.tick();
    else if (currentState == 2)
      gameP.tick();
    else
      wordP.tick();
  }
  public void draw(Graphics g)
  {
    if (currentState == 0)
      mainM.draw(g);
    else if (currentState == 1)
      lvlS.draw(g);
    else if (currentState == 2)
      gameP.draw(g);
    else
      wordP.draw(g);
  }
  public void keyPressed(int k)
  {
    if (currentState == 0)
      mainM.keyPressed(k);
    else if (currentState == 1)
      lvlS.keyPressed(k);
    else if (currentState == 2)
      gameP.keyPressed(k);
    else
      wordP.keyPressed(k);
  }
  public void keyReleased(int k)
  {
    if (currentState == 0)
      mainM.keyReleased(k);
    else if (currentState == 1)
      lvlS.keyReleased(k);
    else if (currentState == 2)
      gameP.keyReleased(k);
    else
      wordP.keyReleased(k);
  }
  
}//end class GameStateManager
