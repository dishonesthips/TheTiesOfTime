/**************************************************************************************************************
  * Assignment: ICS Summative
  * Description of game:  This program is a sidescrolling platformer game with a menu system in which the goal 
  *                       is to acquire keys to unlock briefcases to win the levels. The final level is a phrase
  *                       unscrambling puzzle
  * Description of class: This is the #main class that creates the frame that the Board class (panel) is put onto
  * 
  * Author of Class: #Borna Houmani-Farahani
  * Last Edited: June. 11, 2016
  * Course: ICS3U1
  ***************************************************************************************************************/


package game;

//imports JFrame module which is the base of the GUI
import javax.swing.JFrame;

public class Platformer extends JFrame
{
  //the following code is run when this class is called
  public Platformer()
  {
    add(new Board());//adds the JPanel Board to the frame
    setTitle("The Ties of Time");//titles the window 
    setResizable(false);//disallows the user to resize the window
    setDefaultCloseOperation(EXIT_ON_CLOSE);//exit when the frame closes
    setSize(960, 508);//sets the size of the frame
    setLocationRelativeTo(null);//place the frame at the center of the screen
    setVisible(true);//makes the frame visible
  }
  //#main method creates a new JFrame with the above specifications 
  public static void main(String[] args) 
  {
    new Platformer();
  }//end main
}//end Platformer class
