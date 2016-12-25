/**************************************************************************************************************
  * Assignment: ICS Summative
  * Description of class: This is the MainMenu class where the user can learn the controls, see the credits, quit
  *                       or play the game, if they want to
  * 
  * Author of Class: #Akeen Zhong
  * Last Edited: June. 12, 2016
  * Course: ICS3U1
  ***************************************************************************************************************/

//Bind the MainMenu class with the other classes in the folder
package game;

//Import needed modules
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class MainMenu
{
  //#variable
  /********************************************************************************************************************
    * Variable Dictionary
    * String[][]info - Holds strings that contain information that will be displayed on the paper to the right of the
    *                  screen; each row represents a player option and each column represents a new line.
    * String[][]jokes - Holds jokes / witty captions that will be displayed under the polaroid; each row represents a
    *                   player option and every three columns represent a randomly selected joke
    * int jokeChoice - This is a random integer that will cycle through the joke for the appropriate player option
    *                  This number goes up by three, because each joke is three lines long
    *int fontSize - This is an integer that determines the font size of the strings that are drawn, whether they are
    *               jokes or info
    *int newLine - This is an integer that increases after a string is drawn and represents the y coordinate where
    *              the info is drawn; the constant adding to it ensures that no lines are drawn on top of each other
    *boolean xHover - This is an boolean which tracks the left and right options of the 2D menu
    *boolean yHover - This is an boolean which tracks the up and down options of the 2D menu
    *int userOption - Works with the indexes of info and jokes to draw them onto the screen, it holds an integer that
    *                 acts as both the index of the appropriate strings to draw and the option that the user chooses
    *boolean newJoke - This boolean value ensures that the jokes to not cycle at rapid speeds across the screen, and
    *                  because jokes are only drawn when this is true, this only turns true when the user presses an
    *                  arrow key
    *private GameStateManager manage - This allows the MainMenu class to communicate with the GameStateManager
    *private BufferedImage menuScreen - This is the bulletin board which serves as the background of the MainMenu,
    *                                   it contains sticky notes, a polaroid, the title and a piece of paper
    *private BufferedImage blueTack - This contains a blue push pin that hovers over the credits option
    *private BufferedImage greenTack - This contains a green push pin that hovers over the play option
    *private BufferedImage redTack - This contains a red push pin that hovers over the controls option
    *private BufferedImage yellowTack - This contains a yellow push pin that hovers over the credits option
    *private BufferedImage blackTackL - This contains a black push pin that holds up the polaroid
    *private BufferedImage blackTackR - This contains an inverted black pin that holds up the other side
    * 
    *private BufferedImage playPic - This contains the picture of Louis that is shown when the play option is hovered
    *private BufferedImage controlsPic - This is the picture of Louis that is shown when the controls option is hovered
    *private BufferedImage creditsPic - This is the picture of Louis that is shown when the credits option is hovered
    *private BufferedImage quitPic - This is the picture of Louis that is shown when the quit option is hovered
    * 
    ******************************************************************************************************************/
  
  //Declare variables
  //#variable
  
  //#array
  private String[][] info = {
    {"Move with the arrow keys","",
      "Press space to jump","",
      "Press down to pick up items","",
      "Press backspace to restart a level","",
      "Press esc to be brought back here"},
    {"You are Louis, an agent for the Brief Missions",
      "Force. Earlier, tommorow evening, the BMF spots", 
      "a rift in time. The world needed you to travel",
      "through 4 different dimensions to find keys and",
      "open briefcases with instructions on how to save",
      "time. The BMF will equip you with special ties that",
      "that transported you to each dimension. Avoid the", 
      "spikes and spooky monsters."
    },
    {"Made by:","",
      "twinsrat & SerbianLightning","","",
      "Dedicated to:","", 
      "our TankiOnline team."},
    {"Press enter to quit the game","",
      "...but quitters suck."}};
  
  //#array
  //Jokes is dynamic, so long as a joke is three lines long anything can be added to it
  //So Borna went a little crazy...
  private String[][] jokes = 
  {
    {"   I wish I went to prom", 
      "   instead of buying 353",
      "        chicken nuggets",
      " Damn I could have bought", 
      "  353982 chicken nuggets", 
      "   with my tuition money",
      "    I could have been an",
      "    accountant...what did",
      "      I do with my life?",
      "Still don't know how to do",
      "  taxes...I wish I listened",
      "     in accounting class",
      "   I'm spending my 401k",
      "entirely on chicken nuggets",
      "",
      "    they told me i would",
      "     be like Tom Cruise...",
      "   im not like Tom Cruise",
      " I joined the BMF because",
      "     I couldn't get into ",
      "  Waterloo...FeelsBadMan",
      "       i may be an idiot",
      "      but im not stupid",
      "",
      "    i should have joined",
      "   the School of Rock :(",
      "",
      
    },
    {" Hi, would you like to buy",
      "natural enhancement pills?",
      "        *hangs up*...lol",
      "Son, we need to talk about",
      "   the birds and the bees.",
      "       *hangs up*...lol",
      "            Hey Louis...",
      "          I'm pregnant.",
      "         *hangs up*...lol",
      "",
      "          ...square up",
      "",
      "  wanna see me save time?",
      " wanna see me do it again?",
      "",
      "   like le post if you plan",
      "           on playing",
      "",
      "     hit play or else the",
      "    hash slinging slasher",
      "     will come after you",
      "",
      "     no weenies allowed",
      "",
      "   unless you think you're",
      "tough enough, head on over",
      "    to Weenie Hut Jr.'s",
      "   I'll bet 2503 chicken",
      "      nuggets that you",
      "            can't win"
      
    },
    {
      "     This one is for all",
      "   the ice rats out there",
      "        *Stay Frosty.*",
      "      Reminder to Jovan",
      "    that to win he has to",
      "      play the objective",
      " if i bring two jerseys to",
      "   every game it doesn't",
      "mean im a fairweather fan!",
      "        this episode of",
      "    yu-gi-oh is sponsored",
      "            by 4kids!",
      "       swag is for boys",
      "       class is for men",
      "",
      "  Then who was flickering",
      "          the lights?...",
      "        ...Nosferatu!",
      "   Shouts out to my good",
      "   friend George Clooney.",
      "please return my calls, man",
      " are you calling me a liar?",
      "i ain't callin you a truther",
      "",
      "     shouts out to Doug",
      " Dimmadome, the owner of",
      "the Dimmsdale Dimmadome",
      "   add me on club penguin:",
      "    rich_homie_born_67",
      "",
      "",
      "  all hail the magic conch",
      ""
    },
    {
      "",
      "I'm never letting go Jack!",
      "",
      "There's no way I can save",
      " the world when I always",
      "          am pooping.",
      "  You wouldn't leave the",
      "       BMF, would you?",
      "",
      "   if you can't stand the",
      "   heat, get out the oven!",
      "",
      "the most painful goodbye's",
      "are the ones where we dont",
      "        get 100% after.",
      "",
      "   I'll never forget you!",
      "",
      "",
      "    Hasta la vista, baby",
      "",
      "            dasvidanya,",
      "           khoda hafez,",
      "            and zaijian",
      "   fine ill flipping leave,",
      "    jesus flipping christ",
      ""
    }
  };
  
  int jokeChoice;
  int fontSize;
  int newLine;
  
  //xHover and yHover are set to true as a default so controls is the first option selected
  //where the user learns how to play
  boolean xHover = true;
  boolean yHover = true;
  
  int userOption;
  
  boolean newJoke = true;
  
  private GameStateManager manage;
  private BufferedImage menuScreen;
  
  private BufferedImage blueTack;
  private BufferedImage greenTack;
  private BufferedImage redTack;
  private BufferedImage yellowTack;
  private BufferedImage blackTackL;
  private BufferedImage blackTackR;
  
  private BufferedImage playPic;
  private BufferedImage controlsPic;
  private BufferedImage creditsPic;
  private BufferedImage quitPic;
  
  public MainMenu(GameStateManager manage)
  {
    this.manage = manage; //Communicate with the GameStateManager
    
    try //Try to load the images
    {
      menuScreen =  ImageIO.read(getClass().getResource("MainMenu.png"));
      blueTack =  ImageIO.read(getClass().getResource("blueTack.png"));
      greenTack =  ImageIO.read(getClass().getResource("greenTack.png"));
      redTack =  ImageIO.read(getClass().getResource("redTack.png"));
      yellowTack =  ImageIO.read(getClass().getResource("yellowTack.png"));
      //blackTackR =  ImageIO.read(getClass().getResource("blackTack.png"));
      //blackTackL =  ImageIO.read(getClass().getResource("blackTack.png"));
      //AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); 
      //tx.translate(-blackTackL.getWidth(null),0); 
      //AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
      //blackTackR = op.filter(blackTackL, null);
      
      playPic = ImageIO.read(getClass().getResource("playImage.png"));
      controlsPic = ImageIO.read(getClass().getResource("controlsImage.png"));
      creditsPic = ImageIO.read(getClass().getResource("creditsImage2.png"));
      quitPic = ImageIO.read(getClass().getResource("quitImage2.png"));
    }                                         //#error
    catch (Exception e){e.printStackTrace();} //Print error message if image is not found
    
  }
  
  public void tick() {} //timer method
  
  //graphics method
  public void draw(Graphics g)
  {
    g.drawImage(menuScreen, 0, 0, null); //draw the background
    
    //#condition
    if (yHover) //If the user is hovering the top 2 options
    {
      if (xHover)//controls selected
      {
        userOption = 0; //Index 0 on the arrays jokes and info are related to controls
        g.drawImage(redTack,286,87,null); //Draw the red pin on the "controls" sticky note
        g.drawImage(controlsPic,444,217,null); //Draw the image of Louis on the polaroid
      }//end if
      else //play selected
      {
        userOption = 1; //Index 1 on the arrays jokes and info are related to play
        g.drawImage(greenTack,130,78,null); //Draw the green pin on the "play" sticky note
        g.drawImage(playPic,411,204+20,null); //Draw the image of Louis on the polaroid
      }//end else
    }//end if
    
    //#condition
    else //The user is hovering the bottom 2 options
    {
      if (!xHover) //credits selected
      {
        userOption = 2; //Index 2 on the arrays jokes and info are related to credits
        g.drawImage(blueTack,139-14-14,273-14,null); //Draw the blue pin on the "credits" sticky note
        g.drawImage(creditsPic,411+24+4,204+6+4+3,null); //Draw the image of Louis on the polaroid
      } //end if
      else //there's no way they want to quit
      {
        userOption = 3; //Index 3 on the arrays jokes and info are related to quit
        g.drawImage(yellowTack,318-14,278-14,null); //Draw the yellow pin on the "quit" sticky note
        g.drawImage(quitPic,411+25,204+10,null); //Draw the image of Louis on the polaroid
      }//end else
    }//end else
    
    //#condition
    if (newJoke) //If the player has moved to another option
      jokeChoice = (int) (Math.random() * jokes[userOption].length/3) * 3; //Get the index of a #random joke
    newJoke=false; 
    //#error
    //Set it to false so jokes do not cycle quickly on the screen
    
    fontSize = 12; //All the jokes are written in 12 pt font
    g.setFont(new Font("Comic Sans MS", Font.PLAIN, fontSize)); //Set the font
    
    //#loop
    
    //#error
    //In order for Java not to return a list out of range, every joke is three lines long; the short ones are filled
    //with empty strings to ensure they do not change the uniformity
    for(int i = 0; i < 3; i++)
      g.drawString(jokes[userOption][jokeChoice+i],410,388+12*i); 
    //Draws the jokes based on the random number selected
    //end for loop
    
    //Switch statement which changes the font size for the text drawn on the paper
    switch(userOption)
    {
      case(0):
      {
        fontSize = 18; //The information in controls is drawn in 18 pt font
        break;
      }
      case(1): //The information in play is drawn in 12 pt font
      {
        fontSize = 12;
        break;
      }
      case(2): //The information in credits is drawn in 20 pt font
      {
        fontSize = 20;
        break;
      }
      case(3): //The information in quit is drawn in 20 pt font
      {
        fontSize = 20;
        break;
      }
    }//end switch statement
    
    g.setFont(new Font("Comic Sans MS", Font.PLAIN, fontSize)); //Set the font to the new size
    //#loop
    for (int i = 0; i<info[userOption].length; i++)
      //Goes through the 2D array, because userOption is already declared, only the columns need to be cycled through
      //by the means of this for loop
    {
      g.drawString(info[userOption][i],605,235+newLine); //Draw the information on the paper
      newLine+=16; //add to newLine so there is space between lines
    }//end for loop
    newLine=0; //Reset newLine to 0 in case the user selects another option
  }//End draw
  
  //Keypressed methods
  public void keyPressed(int k) 
  {
    //#condition
    if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_UP)
    { 
      if (yHover) //Change the up / down hover when the user presses the up or down arrow key
      {
        yHover = false;
        newJoke = true; //Load a new joke
      }//end if
      else
      {
        yHover = true;
        newJoke = true;
      }//end else
    }//end if
    else if (k == KeyEvent.VK_LEFT ||k == KeyEvent.VK_RIGHT)
    {
      if (xHover) //Change the left / right hover when the user presses the left or right arrow key
      {
        xHover = false;
        newJoke = true; //Load a new joke
      }//end if
      else
      {
        xHover = true;
        newJoke = true; //Load a new joke
      }//end else
    }//end else if
    
    //#condition
    else if (k == KeyEvent.VK_ENTER) //Checks if the user hits enter
    {
      if (!xHover && yHover) //If play is hovered than the level selection screen is shown
      {
        manage.setState(1,0,false);
      }//end if
      else if (xHover && !yHover)
        System.exit(0);
    }//end else if
  }//keyPressed
  public void keyReleased(int k) {}
}