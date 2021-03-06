/**************************************************************************************************************
  * Assignment: ICS Summative
  * Description of class: This class is used after play is selected in main menu, and allows the user to select
  *                       the level they wish to play 
  * 
  * Author of Class: #Akeen Zhong
  * Last Edited: June. 12, 2016
  * Course: ICS3U1
  ***************************************************************************************************************/

package game;

//import packages
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;

public class LevelSelection
{
  //#variable
  /*******************************************************************************************************************
    * Variable Dictionary
    * int xHover - contains a value of 0-4 which is changed by the left and right arrows, indicates user selection
    * boolean cheatHover - contains a boolean which is changed by the up and down arrows, indicates whether the user
    *                      is hovering over the cheat button
    * boolean cheat - contains a boolean which is changed by the enter key when cheatHover is true which turns 
    *                 the cheat option on/off
    * GameStateManager manage - This variable allows the main menu to communicate with the GameStateManager, which
    *                           selects the level class
    * BufferedImage bulletin - The background for the class, which features notes indicating the levels
    * BufferedImage whiteTack - A white pushpin that is drawn over the level screenshots, indicating the user's
    *                           selection
    * BufferedImage noCheat - The image for the button when cheats are off, says "CHEAT"
    * BufferedImage cheating - The image for the button when cheats are on, says "CHEATING"
    * 
    * BufferedImage level1 - A screenshot from the first level that is used in the selection screen
    * BufferedImage level2 - A screenshot from the second level that is used in the selection screen
    * BufferedImage level3 - A screenshot from the third level that is used in the selection screen
    * BufferedImage level4 - A screenshot from the fourth level that is used in the selection screen
    * BufferedImage level5 - A screenshot from the fifth level (word puzzle) that is used in the selection screen
    ******************************************************************************************************************/
  
  //Declare variables
  private int xHover;
  public static boolean cheatHover, cheat = false;
  private GameStateManager manage;
  
  private BufferedImage bulletin;
  private BufferedImage whiteTack;
  private BufferedImage noCheat;
  private BufferedImage cheating;
  
  private BufferedImage level1;
  private BufferedImage level2;
  private BufferedImage level3;
  private BufferedImage level4;
  private BufferedImage level5;
  
  
  //Communicates with the GameStateManager
  public LevelSelection(GameStateManager manage)
  {
    this.manage = manage;
    
    //Load images
    try
    {
      bulletin =  ImageIO.read(getClass().getResource("levelBoard.png"));
      whiteTack =  ImageIO.read(getClass().getResource("whiteTack.png"));
      
      level1 =  ImageIO.read(getClass().getResource("lilacLair.png"));
      level2 =  ImageIO.read(getClass().getResource("cobaltCavern.png"));
      level3 =  ImageIO.read(getClass().getResource("greenGrotto.png"));
      level4 =  ImageIO.read(getClass().getResource("amberAbyss.png"));
      level5 =  ImageIO.read(getClass().getResource("timeTrial.png"));
      
      noCheat = ImageIO.read(getClass().getResource("cheat.png"));
      cheating = ImageIO.read(getClass().getResource("cheating.png"));
    }
    catch (Exception e){e.printStackTrace();}//print out error message if image is not foudn
  }
  
  public void tick() {} //timer method, which is empty in this part of the game
  
  //Graphics method, where everything is drawn onto the Board class
  public void draw(Graphics g)
  {
    
    g.drawImage(bulletin, 0, 0, null); //draw the bulletin board
    
    //draw the level screenshots
    g.drawImage(level1,55+175*0,150,null);
    g.drawImage(level2,55+175*1,150,null);
    g.drawImage(level3,55+175*2,150,null);
    g.drawImage(level4,55+175*3,150,null);
    g.drawImage(level5,55+175*4,150,null);
    
    //#condition
    if(cheat) //draws "CHEAT" button if no cheats are on
      g.drawImage(cheating,375,440,null);
    else //draws "CHEATING" button if cheats are on
      g.drawImage(noCheat,375,440,null); 
    
    if(cheatHover==true) //hovers the tack over the cheat buttons if up / down arrows are pressed
    {
      g.drawImage(whiteTack,330+150,440-28+10,null); 
    }
    
    else //hovers the selectable levels otherwise
    {
      g.drawImage(whiteTack,125+175*xHover,132,null);
    }
    
  }
  
  public void keyPressed(int k) 
  {
    //#condition
    
    if (k == KeyEvent.VK_LEFT) //Subtracts 1 from xHover if the user presses left or resets if it is 0
    {
      //#error, sets the xHover to 4 if left arrow is pressed and the first option is selected,
      //otherwise nothing will be selected
      if (xHover==0)
        xHover=4;
      else
        xHover--;
    }
    if(k == KeyEvent.VK_RIGHT) //Adds 1 to xHover if the user presses right or resets it if is 4
    {  
      //#error, resets the xHover to 0 if right arrow is pressed and the last option is selected,
      //otherwise nothing will be selected
      if(xHover==4)
        xHover=0;
      else
        xHover++;
    }
    
    //Up and down arrows toggle between selectable levels and cheat button
    if(k == KeyEvent.VK_UP || k == KeyEvent.VK_DOWN) 
    {
      if (cheatHover==false) //If user was originally hovering a level
        cheatHover = true;
      else //If user was hovering the cheat button
        cheatHover = false;
    }
    
    if (k == KeyEvent.VK_ENTER) //When enter is pressed
    {
      if (xHover == 0 && cheatHover == false)
      {
        manage.setState(2,1,cheat); //Load level 1
      }
      else if (xHover == 1 && cheatHover == false)
      {
        manage.setState(2,2,cheat); //Load level 2 
      }
      else if (xHover == 2 && cheatHover == false)
      {
        manage.setState(2,3,cheat); //Load level 3
      }
      else if (xHover==3 && cheatHover == false)
      {
        manage.setState(2,4,cheat); //load level 4 
      }
      else if (xHover==4 && cheatHover == false)
      {
        manage.setState(3,0,cheat); //load level 5 (word puzzle)
      }
    }
    
    if (k == KeyEvent.VK_ESCAPE) //If esc key is pressed
    {
      GameStateManager.currentState = 0; //Bring the user to the main menu
      LevelSelection.cheat = false;
    }
  }
  
  public void keyReleased(int k) 
  {
    //#condition
    if (k == KeyEvent.VK_ENTER && cheatHover == true) //This only happens if the user is hovering the cheat button
    {
      if (cheat) //Turns cheats off if they were originally on
        cheat = false;
      else //Turns cheats on if they were originally off
        cheat = true;
    }
  }
}