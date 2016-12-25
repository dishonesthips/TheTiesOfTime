/**************************************************************************************************************
  * Assignment: ICS Summative
  * Description of class: This class handles all aspects of the game relating to player collision and movement 
  * 
  * Author of Class: #Borna Houmani-Farahani
  * Last Edited: June. 13, 2016
  * Course: ICS3U1
  ***************************************************************************************************************/
package game;

//import required packages
import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Louis
{
  //#variable
  /***************************************************************************************************************
    * Variable Dictionary
    * boolean left - true when the left arrow key is being pressed
    * boolean right - true when the right arrow key is being pressed
    * boolean crouch - true when the down arrow key is being pressed
    * boolean inAir - true when they player is in the air
    * boolean isDead - true when the player has just hit an enemy or spike. becomes false when the death phone screen
    *                  pops up
    * boolean displayRed - true when the player is in contact with any obstacle
    * boolean hasWon - true when the player has unlocked the briefcase. becomes false when the win phone screen 
    *                  pops up
    * boolean faceRight - true when the player's most recent direction is right
    * boolean isDeadScreen - true when the phone screen showing the death message is to be on the screen
    * boolean hasWonScreen - true when the phone screen showing the win message is to be on the screen
    * boolean cheat - is true when cheats are on
    * String preface - a String containing the name of the colour for the current level (e.g. "purple","orange",etc.)
    * int mapX - x position that the first element of the map is drawn at
    * int louisY - y position of the main character
    * int hSpeed - the horizontal speed per frame of the player
    * int ySpeed - the vertical speed per frame of the player
    * int acceleration - speed to change hSpeed by every frame
    * int friction - speed to trend hSpeed to 0 by when the player is sliding
    * int gravForce - speed to increase ySpeed by every frame 
    * int maxHSpeed - maximum hSpeed (for moving right)
    * int minHSpeed - minimum hSpeed (for moving left)
    * int minYSpeed - minimum ySpeed (for moving up)
    * int maxYSpeed - maximum ySPeed (for moving down)
    * Rectangle hitBoxY - rectangle that represents the character's position if ySpeed were added to it
    * Rectangle hitBoxX - rectangle that represents the chracter's position if hSpeed was subtracted from it
    * Rectangle checkCollide - represents the position of whatever object is being checked for collision with the player
    * Rectangle currentState - represents the current position of the character
    * BufferedImage standL - stores image for the left facing, standing character
    * BufferedImage standR - stores image for the right facing, standing character
    * BufferedImage jumpL - stores image for the left facing jumping character
    * BufferedImage jumpR - stores image for the right facing jumping character
    * BufferedImage crouchL - stores image for the left facing crouching character
    * BufferedImage crouchR - stores image for the right facing crouching character
    * BufferedImage runL1 - stores image for the left facing, first running animation
    * BufferedImage runR1 - stores image for the right facing, first running animation
    * BufferedImage runL2 - stores image for the left facing, second running animation
    * BufferedImage runR2 - stores image for the right facing, second running animation
    * BufferedImage winScreen - stores image for the win screen
    * BufferedImage deathScreen - stores image for the death screen
    * BufferedImage dead - stores image of the red, dead character
    * BufferedImage toDisplay - stores whatever sprite of the main character needs to be printed
    ****************************************************************************************************************/
  
  //declaring variables
  private static boolean left, right, 
                         crouch, 
                         inAir, 
                         isDead, hasWon, 
                         displayRed, 
                         faceRight,
                         isDeadScreen, hasWonScreen, 
                         cheat;
  private String preface;
  public static int mapX;
  private static int louisY;
  private static int hSpeed = 0, ySpeed = 0;
  
    //movement constants
  public static int acceleration = 2, friction = 1, gravForce = 1;
  private static int maxHSpeed = 8, minHSpeed = -8;
  private static int minYSpeed = -16, maxYSpeed = 30;
  
    //rectangles to be used for collision intersections
  public static Rectangle hitBoxY, hitBoxX, checkCollide, currentState;
  
    //images relating to this class
  private BufferedImage standL, standR, 
                        jumpL, jumpR, 
                        crouchL, crouchR, 
                        runL1, runR1, 
                        runL2, runR2, 
                        winScreen, deathScreen,
                        dead,
                        toDisplay;
  
  
  
  public Louis(boolean cheat)
  {
    this.cheat = cheat; //sets the cheat variable in this class to the one passed in by the Gameplay class
    
    loadImages(Gameplay.level);//calls the #method to load the images needed for drawing the character in the specific level
    init(Gameplay.level);//calls method that initializes variables based on current level
  }
  
  //horizontal movement #methods
  public void moveL() 
  {
    hSpeed += acceleration;
    if (hSpeed > maxHSpeed)
      hSpeed = maxHSpeed;
  }
  public void moveR()
  {
    hSpeed -= acceleration;
    if (hSpeed < minHSpeed)
      hSpeed = minHSpeed;
  }
  public void friction()//tend hSpeed towards 0
  {
    if (hSpeed > 0) 
      hSpeed -= friction;
    else if (hSpeed < 0) 
      hSpeed += friction;
  }
  
  //vertical movement #methods
  public void jump()
  {
    ySpeed = minYSpeed;
  }
  public void gravity()
  {
    if (ySpeed >= maxYSpeed)
      ySpeed = maxYSpeed;
    else
      ySpeed += gravForce;
    inAir = true;
  }
  
  //#method to check collision with blocks
  //collision check is the #error handling in the platformer game. 
  //this stops the player from going through solid blocks
  public void checkCol()
  {
    //creates rectangles to represent where the character will be printed
    //x and y are separate because horizontal and vertical movement are independent in platformers
    hitBoxY = new Rectangle(Board.WIDTH / 2 - 16, louisY + ySpeed, 32, 32);
    hitBoxX = new Rectangle(Board.WIDTH / 2 - 16 - hSpeed, louisY, 32,32);
    
    //nested for #loop to go through the 3 wide slice of the map that the character could possibly collide with
    for (int i = 0; i < Gameplay.map.length; i++)
    {
      for (int j = Gameplay.markCollision - 1; j < Gameplay.markCollision + 2; j++)
      {
        if (Gameplay.map[i][j] == 1 || Gameplay.map[i][j] == 2)//#condition. If the block is a solid block
        {
          //create a rectangle representing the position of the block
          checkCollide = new Rectangle(mapX + j * 48, i * 48 - 48, 48, 48);          
          
          if (hitBoxX.intersects(checkCollide))//#condition. if the x rectangle intersects with the block
          {
            if (hSpeed < 0)//if the player is approaching from the left, snap them to the left side of the block
              mapX -= (mapX + j * 48) - (Board.WIDTH / 2 - 16 + 32);
            else if (hSpeed > 0)//if the player is approaching from the right, snap them to the right side of the block
              mapX += (Board.WIDTH / 2 - 16) - (mapX + j * 48 + 48);       
            
            hSpeed = 0;//halt the player's horizontal movement
          }
          
          if (hitBoxY.intersects(checkCollide))//#condition. if the y rectangle intersects with the block
          {
            if (ySpeed < 0)//if the player is moving upwards, snap them to the bottom of the block
              louisY = i * 48;
            else if (ySpeed > 0)//if the player is moving downwards, snap them to the top of the block
            {
              inAir = false;//the player is no longer in mid air
              louisY = i * 48 - 32 - 48;
            }
            ySpeed = 0;//halt the player's vertical movement
          }
        }
      }//end column loop
    }//end row loop
  }//end checkCol
  
  //#method to check all death possibliites 
  public void checkDeath()
  {
    currentState = new Rectangle(Board.WIDTH / 2 - 16, louisY, 32, 32);
    //nested for #loop checking collision with spikes in the 3-wide slice of the map the player is in
    for (int i = 0; i < Gameplay.map.length; i++)
    {
      for (int j = Gameplay.markCollision - 1; j < Gameplay.markCollision + 2; j++)
      {
        if (Gameplay.map[i][j] == 3 || Gameplay.map[i][j] == 4)//if the block is a spike #condition
        {
          checkCollide = new Rectangle(mapX + j * 48, i * 48 - 48, 48, 48);
          if (currentState.intersects(checkCollide))//if the spike intersects with the player
          {
            //death related tasks
            displayRed = true; 
            if (!cheat) //if #cheat is true, they don't die
              isDead = true;
          }
        }
      }//end column loop
    }//end row loop
    
    //collisions with horizontal ghosts
    for (int i = 0; i < Gameplay.horiEnemies.length; i++)
    {
      checkCollide = new Rectangle(mapX + Gameplay.horiEnemies[i][2], Gameplay.horiEnemies[i][3], 48, 48);
      if (currentState.intersects(checkCollide))
      {
        //death related tasks
        displayRed = true;
        if (!cheat)//if #cheat is true, they don't die
          isDead = true;
      }
    }//end loop
    
    //collisions with vertical ghosts    
    for (int i = 0; i < Gameplay.vertEnemies.length; i++)
    {
      checkCollide = new Rectangle(mapX + Gameplay.vertEnemies[i][2], Gameplay.vertEnemies[i][3], 48, 48);
      if (currentState.intersects(checkCollide))
      {
        //death related tasks
        displayRed = true;
        if (!cheat)//if #cheat is true, they don't die
          isDead = true;
      }
    }//end loop
  }//end checkDeath
  
  //#method to check if the player has gotten the key
  public void checkKey()
  {
    checkCollide = new Rectangle(mapX + Gameplay.items[0][0], Gameplay.items[0][1], 48,48);
    //#condition. if they collide with the key, change the status of the key in the items #array
    if (currentState.intersects(checkCollide))
      Gameplay.items[0][2] = 0;
  }//end checkKey
  
  //#method to check if the player is in contact with the briefcase when they crouch (check if they have won)
  public void checkBrief()
  {
    currentState = new Rectangle(Board.WIDTH / 2 - 16, louisY, 32, 32);
    checkCollide = new Rectangle(mapX + Gameplay.items[1][0], Gameplay.items[1][1], 48,48);
    //if they are in contact with the briefcase and they have the key already
    if (currentState.intersects(checkCollide) && Gameplay.items[0][2] == 0)
    {
      //winning tasks
      Gameplay.items[1][2] = 0;
      hasWon = true;
    }
  }
  
  //tick #method to update variables uses the previously declared methods.
  //this is the technical heart of the gameplay
  public void tick() 
  {
    if (hasWonScreen)//if the win screen is being shown, sleep, reset variables, and send the user back to menu
    {
      try {
        Thread.sleep(4000);
      }
      catch (Exception e){e.printStackTrace();}
      hasWonScreen = false;
      GameStateManager.currentState = 0;
      LevelSelection.cheat = false;
    }
    else if (hasWon) //if the player has won, sleep, set up variables so the win screen will be shown next
    {
      try {
        Thread.sleep(250);
      }
      catch (Exception e){e.printStackTrace();}
      hasWon = false;
      hasWonScreen = true;
    }
    else if (isDeadScreen) //if the death screen is being shown, sleep, reset variables to restart the level
    {
      try {
        Thread.sleep(2000);
      }
      catch (Exception e){e.printStackTrace();}
      displayRed = false;
      isDeadScreen = false;
      isDead = false;
      init(Gameplay.level);
    }
    else if (isDead) //if the player has died, set up the variables to show the death screen next
    {
      try {
        Thread.sleep(500);
      }
      catch (Exception e){e.printStackTrace();}
      
      isDeadScreen = true;
    }
    //#condition if the player has not died or won yet
    else
    {
      if (crouch && ySpeed == 0 && hSpeed == 0) //if the player wants to crouch and is not moving
      {
        checkBrief(); //call the checkBrief #method to see if the game is won
      }
      else if (right) //moving right
      {
        moveR();
        faceRight = true;
      }
      else if (left) //moving left
      {
        moveL();
        faceRight = false;
      }

      if (!left && !right) //if the player is not actively trying to move, apply friction
        friction();
      
      gravity(); //apply gravity
      
      checkCol(); //check collision
      
      mapX += hSpeed; //move the map by the current hSpeed
      Gameplay.backX += hSpeed / 6; //move the background by 1/6 of the current hSpeed (paralax scrolling background)
      louisY += ySpeed; //move the player vertically by ySpeed
      
      checkDeath(); //check death possibilities
      checkKey(); //check contact with key
    }
  }//end tick
  
  //draw #method draws the images relating to the player to the screen
  public void draw(Graphics g) 
  {
    //#cheat #condition
    if (cheat) //if cheating, mark the blocks that are being checked for collision with black
    {
      for (int i = 0; i < Gameplay.map.length; i++)
      {
        for (int j = Gameplay.markCollision - 1; j < Gameplay.markCollision + 2; j++)
        {
          if(Gameplay.map[i][j]==1 || Gameplay.map[i][j]==2)
            g.fillRect(mapX + j * 48,i * 48-48,48,48);
        }//end column loop
      }//end row loop
      //draw the predicted position of the character in the next frame
      g.setColor(Color.RED);
      g.fillRect(Board.WIDTH / 2 - 16 - hSpeed, louisY + ySpeed, 32,32); 
    }
    
    //#conditional statements to determine which sprite of the character to draw
    if (displayRed)
    {
      toDisplay = dead;
      if (cheat)
        displayRed = false;
    }
    else
    {
      if (inAir)
      {
        if (faceRight)
          toDisplay = jumpR;
        else
          toDisplay = jumpL;
      }
      else
      {
        if (crouch && ySpeed == 0 && hSpeed == 0)
        {
          if (faceRight)
            toDisplay = crouchR;
          else
            toDisplay = crouchL;
        }
        else if (!left && !right)
        {
          if (faceRight)
            toDisplay = standR;
          else
            toDisplay = standL;
        }
        else if ( Gameplay.animCount > 0)
        {
          if (right)
            toDisplay = runR1;
          else
            toDisplay = runL1;
        }
        else
        {
          if (right)
            toDisplay = runR2;
          else
            toDisplay = runL2;        
        }
      }
    }
    //draw whichever sprite was decided by the above condition
    g.drawImage(toDisplay,Board.WIDTH / 2 - 16, louisY, null);
    
    //if either screen is to be shown, draw it over the player
    if (isDeadScreen)
      g.drawImage(deathScreen, 0,0,null);
    if (hasWonScreen)
      g.drawImage(winScreen,0,0,null);
  }//end draw
  
  //keyPressed and keyReleased #methods control input
  public void keyPressed(int k)
  {
    if (k == KeyEvent.VK_LEFT)
      left = true;
    if (k == KeyEvent.VK_RIGHT)
      right = true;
    if (k == KeyEvent.VK_DOWN)
      crouch = true;
    if (k == KeyEvent.VK_SPACE)
    {
      if (!inAir)
        jump();
    }
  }
  public void keyReleased(int k)
  {
    if (k == KeyEvent.VK_LEFT)
      left = false;
    if (k == KeyEvent.VK_RIGHT)
      right = false;
    if (k == KeyEvent.VK_DOWN)
      crouch = false;
  }
  
  //this #method loads images based on which level is selected
  private void loadImages(int level)
  {
    //level colour #condition based on given arguments
    if (cheat)
      preface = "cheat";
    else if (level == 1)
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
      winScreen = ImageIO.read(getClass().getResource("win-screen.png"));
      deathScreen = ImageIO.read(getClass().getResource("death-screen.png"));
      
      //standing animation and horizontal flip
      standL = ImageIO.read(getClass().getResource(preface +"-louis-stand.png"));
      AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); 
      tx.translate(-standL.getWidth(null),0); 
      AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
      standR = op.filter(standL, null);
      
      //jumping animation and horizontal flip
      jumpL = ImageIO.read(getClass().getResource(preface + "-louis-jump.png"));      
      jumpR = op.filter(jumpL, null);
      
      //first running frame and horizontal flip
      runL1 = ImageIO.read(getClass().getResource(preface +"-louis-move1.png"));     
      runR1 = op.filter(runL1, null);
      
      //second running frame and horizontal flip
      runL2 = ImageIO.read(getClass().getResource(preface +"-louis-move2.png"));
      runR2 = op.filter(runL2, null);
      
      //crouch animations and horizontal flip
      crouchL = ImageIO.read(getClass().getResource(preface +"-louis-crouch.png"));
      crouchR = op.filter(crouchL, null);
      
      //dead image
      dead = ImageIO.read(getClass().getResource("louis-death.png"));
    }
    catch (Exception e){e.printStackTrace();} 
  }//end loadImages
  
  //#method to initialize variables, used when starting level and whenever it is reset 
  public static void init(int level)
  {
    //level based #conditions to reset the map, background, and the y position of the character
    if (level == 1)
    {
      mapX = -96;
      Gameplay.backX = Gameplay.backStart;
      louisY = 288;
    }
    else if (level == 2)
    {
      mapX = -96;
      Gameplay.backX = Gameplay.backStart;
      louisY = 288;
    }
    else if (level == 3)
    {
      mapX = -240;
      Gameplay.backX = Gameplay.backStart;
      louisY = 144;
    }
    else if (level == 4)
    {
      mapX = -96;
      Gameplay.backX = Gameplay.backStart;
      louisY = 384;
    }
    Gameplay.items[0][2] = 1; //reset the key
    //reset speed
    hSpeed = 0;
    ySpeed = 0;
    //reset movement and animation booleans
    faceRight = true;
    left = false;
    right = false;
    crouch = false;
    isDead = false;
    isDeadScreen = false;
    displayRed = false;
    hasWon = false;
    hasWonScreen = false;
  }//end init
  
}//end Louis