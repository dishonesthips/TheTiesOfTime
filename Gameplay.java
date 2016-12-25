/**************************************************************************************************************
  * Assignment: ICS Summative
  * Description of class: This class is the gameplay state of the game. It controls all platforming levels and 
  *                       the enemies/items within.
  * 
  * Author of Class: #Borna Houmani-Farahani
  * Last Edited: June. 13, 2016
  * Course: ICS3U1
  ***************************************************************************************************************/
package game;

//importing required packages
import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Gameplay
{
  //#variable
  /***************************************************************************************************************
    * Variable Dictionary
    * Louis louis - creates an instance of the Louis class which controls the main character
    * int markCollision - value representing the index of the column of the map that the player is in. This
    *                     is used to check collision in the slice of the map that the player is in
    * int backX - x value to print the background image
    * int level - value between 1-4 representing which level the player is on
    * int animCount - a counter that goes from -4 to 5 to mark which animation sprites to draw
    * BufferedImage slab - stores the image for the slab of the current level
    * BufferedImage rock - stores the image for the rock of the current level
    * BufferedImage spikeU - stores the image for the upwards facing spike of the current level
    * BufferedImage spikeD - stores the image for the downwards facing spike of the current level
    * BufferedImage ghostL1 - stores the image for the first animation of the left facing ghost of the level
    * BufferedImage ghostL2 - stores the image for the second animation of the left facing ghost of the level
    * BufferedImage ghostR1 - stores the image for the first animation of the right facing ghost of the level
    * BufferedImage ghostR2 - stores the image for the second animation of the right facing ghost of the level
    * BufferedImage key - stores the image for the key
    * BufferedImage briefcase - stores the image for the briefcase
    * BufferedImage back - stores the background image of the current level
    * String preface - a String containing the name of the colour for the current level (e.g. "purple","orange",etc.)
    * int [][] horiEnemies - a 2D rectangular array containing the left limit, right limit, x position, y position, 
    *                        and horizontal speed for each horizontally moving enemy in the level
    * int [][] vertEnemies - a 2D rectangular array containing the upper limit, lower limit, x position, y position,
    *                        and vertical speed for each vertically moving enemy in the level
    * int [][] items - a 2D rectangular array containing coordinates and a 1 or 0 representing the status of the key
    *                  and briefcase in the level
    * byte [][] map - a 2D rectangular array containing the information for the map of the current level
    *                 -1 = black square, 0 = air, 1 = rock, 2 = slab, 3 = upwards spike, 4 downwards spike
    ****************************************************************************************************************/
 
  //declaring #variables
    //player class
  private Louis louis;
    //map and level related variables
  public static int level;
  public static int markCollision = 1;
  public static int [][] horiEnemies;  //left lim, right lim, x, y, speed
  public static int [][] vertEnemies;
  public static int [][] items;  //x, y, 1 or 0
  public static byte [][] map;  
    //animation related variables
  public static int backX, backStart;
  public static int animCount;
  private String preface;
  private BufferedImage slab, rock, 
                        spikeU, spikeD, 
                        ghostL1, ghostL2, 
                        ghostR1, ghostR2, 
                        key, briefcase, 
                        back;
  
  public Gameplay(int level, boolean cheat)
  {
    this.level = level; //sets the level variable in this class to the one passed in by the GameStateManager
    
    loadImages(level); //calls #method to load the images of the current level
    init(level); //calls method to initialize all the variables
    
    louis = new Louis(cheat); //creates the player class and passes in the cheat boolean
  }
  
  //the tick #method is called to update variables
  public void tick() 
  {
    louis.tick(); //this calls the same method in the louis class so that the player can be updated
    //#condition moves the animation counter from -4 to 5
    if (animCount == 5)
      animCount = -4;
    else
      animCount += 1;
  }//end tick
  
  //the draw #method is responsible for drawing the images
  public void draw(Graphics g)
  {
    g.drawImage(back, backX , 0, null);//draw the background
    
    //nested for loops used to #loop through every element in the map #array
    for (int i = 0; i < map.length; i++)
    {
      for (int j = 0; j < map[0].length; j++)
      {
        //if the element is a solid block
        if (map[i][j] == 1 || map[i][j] == 2)
        {
          //if #condition to see if the block is directly above the player
          //if this is true, mark the column index of that block as the collision reference
          if (louis.mapX+j*48 >= Board.WIDTH / 2 - 48 && louis.mapX+j*48 <= Board.WIDTH / 2)
            markCollision = j;
          
          //#condition. if the value represents a rock, draw a rock in the appropriate position
          if (map[i][j] == 1)
            g.drawImage(rock,louis.mapX+j*48, i*48 - 48, null);
          //else, draw the slab image in the same position
          else
            g.drawImage(slab,louis.mapX+j*48, i*48 - 48, null);
        }
        else if (map[i][j] == 3)//if the element represents an upwards spike, draw one
          g.drawImage(spikeU,louis.mapX+j*48, i*48 - 48, null);
        else if (map[i][j] == 4)//if the element represents a downwards spike, draw one
          g.drawImage(spikeD,louis.mapX+j*48, i*48 - 48, null);
        else if (map[i][j] == -1)//if the element represents a non interactable block, draw a black box
        {
          g.setColor(Color.BLACK);
          g.fillRect(louis.mapX+j*48,i*48 - 48,48,48);
        }
      }//end column loop
    }//end row loop
    
    //#loop to go through every horizontal enemy
    for (int i = 0; i < horiEnemies.length; i++)
    {
      //format of the information for each enemy: {left lim, right lim, current x, current y, speed}
      //if the x position of the enemy is outside either of the limits
      if (horiEnemies[i][2] <= horiEnemies[i][0] || horiEnemies[i][2] >= horiEnemies[i][1])
        horiEnemies[i][4] *= -1;//multiply the speed by -1 (begin moving in other direction)
      
      //add the speed to the x position
      horiEnemies[i][2] += horiEnemies[i][4];
      
      //#condition. if the enemy is moving right 
      if (horiEnemies[i][4] > 0)
      {
        if (animCount > 0)//if the animation count is between -4 and 0, draw the first animated sprite
          g.drawImage(ghostR1, Louis.mapX + horiEnemies[i][2], horiEnemies[i][3], null);
        else //if the animation count is between 1 and 5, draw the second animated sprite
          g.drawImage(ghostR2, Louis.mapX + horiEnemies[i][2], horiEnemies[i][3], null);
      }
      //else, the enmy is moving left
      else
      {
        if (animCount > 0)//if the animation count is between -4 and 0, draw the first animated sprite
          g.drawImage(ghostL1, Louis.mapX + horiEnemies[i][2], horiEnemies[i][3], null);
        else//if the animation count is between 1 and 5, draw the second animated sprite
          g.drawImage(ghostL2, Louis.mapX + horiEnemies[i][2], horiEnemies[i][3], null);
      }
    }//end loop
    
    //#loop going through every vertically moving enemy
    for (int i = 0; i < vertEnemies.length; i++)
    {
      //format: {up lim, down lim, current x, current y, speed}
      //#condition. if the y position of the enemy is outside of the limits
      if (vertEnemies[i][3] <= vertEnemies[i][0] || vertEnemies[i][3] >= vertEnemies[i][1])
        vertEnemies[i][4] *= -1;//reverse direction
      
      //adds the speed to the y position of the enemy
      vertEnemies[i][3] += vertEnemies[i][4];
      
      //#condition based on the animation count that decides which animated sprite to draw
      if (animCount > 0)
        g.drawImage(ghostR1, Louis.mapX + vertEnemies[i][2], vertEnemies[i][3], null);
      else
        g.drawImage(ghostR2, Louis.mapX + vertEnemies[i][2], vertEnemies[i][3], null);
    }//end loop
    
    //draw the key if the status shows that it has not yet been picked up. #condition
    if (items[0][2] == 1)
      g.drawImage(key, Louis.mapX + items[0][0], items[0][1], null);
    else //if the key has been picked up, draw it in the top right, indicating that it has been picked up
      g.drawImage(key, 912, -10, null);
    
    //draw the briefcase if it has not been picked up. #condition
    if (items[1][2] == 1)
      g.drawImage(briefcase, Louis.mapX + items[1][0], items[1][1], null);
    
    //call the draw #method in the Louis class
    //responsible for drawing the main character
    louis.draw(g);
  }//end draw
  
  public void keyPressed(int k) 
  {
    //call the keyPressed #method in the Louis class, controls button presses relating to the character
    louis.keyPressed(k);

    if (k == KeyEvent.VK_ESCAPE) //if they pressed escape #condition
    {
      GameStateManager.currentState = 0; //set the currentState to 0 (send them back to the menu)
      LevelSelection.cheat = false; //reset the cheat toggle button to false
    }
    
    if (k == KeyEvent.VK_BACK_SPACE) //if they press backspace
    {
      init(level); //reinitialize the level by calling the #method
      Louis.init(level); //reinitialize the variables in the Louis class by calling the #method
    }
  }//end keyPressed
  
  public void keyReleased(int k) 
  {
    louis.keyReleased(k); //calls the same #method in the Louis class
  }//end keyReleased
  
  
  //this #method loads images based on which level is selected
  private void loadImages(int level)
  {
    //level colour #condition based on given arguments
    if (level == 1) 
      preface = "purple";
    else if (level == 2)
      preface = "blue";
    else if (level == 3)
      preface = "green";
    else 
      preface = "orange";
    
    //loads images with the preface colour ahead of the image name
    try
    {
      rock = ImageIO.read(getClass().getResource(preface + "-rock.png"));
      slab = ImageIO.read(getClass().getResource(preface + "-slab.png"));
      back = ImageIO.read(getClass().getResource(preface + "-back.png"));
      spikeU= ImageIO.read(getClass().getResource(preface + "-spike.png"));
      
      //sets up filter that vertically flips the upward spike image to create the downward spike image
      AffineTransform vert = AffineTransform.getScaleInstance(1, -1); 
      vert.translate(0, -spikeU.getWidth(null)); 
      AffineTransformOp vFlip = new AffineTransformOp(vert, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
      spikeD = vFlip.filter(spikeU, null);
      
      key = ImageIO.read(getClass().getResource("big-key.png"));
      briefcase = ImageIO.read(getClass().getResource("briefcase.png"));
      
      ghostR1 =ImageIO.read(getClass().getResource(preface + "-ghost1.png"));
      
      //filter to horizontally flip the ghost animated sprites
      AffineTransform hori = AffineTransform.getScaleInstance(-1, 1); 
      hori.translate(-ghostR1.getWidth(null),0); 
      AffineTransformOp hFlip = new AffineTransformOp(hori, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
      ghostL1 = hFlip.filter(ghostR1, null);
      
      ghostR2 = ImageIO.read(getClass().getResource(preface + "-ghost2.png"));
      ghostL2 = hFlip.filter(ghostR2, null);
    }
    catch (Exception e){e.printStackTrace();} 
  }//end loadImages
  
  //#method to initialize variables, used when starting level and whenever it is reset 
  private void init(int level)
  {
    //level based #conditions set the enemies #array, items array, and map to whatever they should be based on that level
    if (level == 1)
    {
      horiEnemies = new int[][]{{816,1152, 1032, 48*8, 6},{526,1200, 1032, 192, 9},{574,872, 630, 96, 6}};
      vertEnemies = new int[][]{{0, 96, 1152, 80, -4},{0, 384, 1440, 192, -6},{0, 384, 1536, 192, 6},
                                {0, 384, 1632, 192, -6},{0, 384, 1728, 192, 6},{0, 192, 1968, 192, 5},
                                {0, 192, 2208, 0, -5}};
      items = new int[][]{{960,48*2,1},{2352,48,1}};
      map = new byte[][] 
      { {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,4,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,3,0,2,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,2,2,2,2,2,2,2,2,2,2,0,2,2,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,2,2,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,1,0,0,0,2,0,0,0,2,0,0,0,0,0,2,2,0,4,4,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,4,4,0,4,4,0,0,0,1,0,0,0,4,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,3,0,0,0,3,0,0,0,3,0,1,1,2,2,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,2,0,0,0,2,0,0,0,2,0,3,0,2,2,3,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
    }
    else if (level == 2)
    {
      horiEnemies = new int[][]{{574,872, 630, 96, 6},{720,1056,800,384,6},{1632,2112, 1700, 240,9},
                                {1728,2160, 1800, 0,6}};
      vertEnemies = new int[][]{{144,240,912, 200, -4}, {0, 384, 1296, 192, 6},{0,240, 1536, 192, 4}};
      items = new int[][]{{1392,96,1},{1776,48,1}};
      map = new byte[][] 
      { {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,4,4,4,4,2,2,1,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,2,1,1,1,2,2,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,2,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,2,1,2,1,1,0,0,0,2,3,3,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,3,0,0,0,0,0,0,0,0,0,0,0,4,2,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,2,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,2,0,1,0,3,0,2,0,1,0,0,0,4,0,1,0,2,0,0,0,0,2,2,1,1,1,2,0,4,0,2,2,2,2,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,2,2,1,0,1,0,2,2,0,0,0,3,2,0,4,0,0,3,0,3,0,0,0,0,0,0,1,0,0,0,0,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,4,0,0,0,1,1,4,0,0,0,2,0,0,0,0,0,2,2,2,0,0,2,2,2,1,1,1,1,0,0,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,1,1,1,1,0,0,4,4,1,4,2,4,4,2,0,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,2,2,0,0,0,0,0,0,0,0,3,4,4,0,0,0,3,2,1,1,1,2,0,0,0,0,0,0,0,0,0,0,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,1,1,1,0,3,2,3,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,0,1,1,0,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
    }
    else if (level == 3)
    {
      horiEnemies = new int[][]{{1104,1632, 1200, 144,6},{1584,1920, 1600, 96, 10},{2016,2352, 2300, 96,6},
                                {1872,2064,2000, 144,6}};
      vertEnemies = new int[][]{{0,336, 576, 200, 6}};
      items = new int[][]{{2352,96,1},{672,48,1}};
      map = new byte[][] 
      { {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,4,0,0,4,1,0,0,0,0,0,0,0,0,0,0,0,0,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,1,2,2,0,1,2,0,0,1,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,1,4,0,0,2,2,0,0,0,0,3,0,0,0,0,0,0,0,2,3,0,1,0,0,0,0,0,2,0,0,0,1,2,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,2,0,0,1,2,1,0,0,0,0,1,2,0,1,0,0,2,2,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,1,1,0,0,0,0,0,0,1,1,0,4,2,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,4,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,2,2,1,1,2,3,0,0,0,1,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,3,3,3,3,1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
    }
    else if (level == 4)
    {
      horiEnemies = new int[][]{{528,912, 600, 336,6},{528,768, 672, 240,-6},{1104,1440,1200,384,6},
                                {1920,2352,2000,384,6},{1584,1920,1700,48,6}};
      vertEnemies = new int[][]{{96,192, 912, 160, 4}};
      items = new int[][]{{2112,384,1},{1584,48,1}};
      map = new byte[][] 
      { {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,2,4,2,1,2,2,2,0,0,4,0,0,0,0,0,0,0,0,1,1,0,0,2,1,2,2,2,1,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,2,0,0,4,0,1,4,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,1,0,0,0,0,0,2,0,0,0,0,0,0,3,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,1,2,2,2,1,1,0,1,0,2,2,0,0,1,1,2,0,0,0,1,2,1,2,1,1,2,1,1,0,0,0,2,1,2,0,0,2,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,2,0,0,0,0,0,0,2,0,1,0,0,0,0,0,3,0,3,0,0,0,2,0,0,0,0,0,0,0,0,2,0,0,0,2,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,1,0,0,0,3,0,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,2,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,2,1,2,2,1,0,0,0,1,0,0,0,4,0,4,0,0,2,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,1,0,4,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,0,0,0,0,0,1,3,0,0,0,0,2,1,2,1,0,3,2,0,0,0,3,0,0,4,0,1,2,2,1,2,0,0,2,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0,0,0,0,1,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,1,2,3,3,0,2,2,1,2,0,0,0,0,0,0,0,0,0,0,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,0,4,0,2,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1,2,2,1,1,1,0,2,0,1,1,0,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
    }
    //resets the x position of the background image and animation counter
    backX = -10;
    animCount = 0;
  }//end init
}//end Gameplay
