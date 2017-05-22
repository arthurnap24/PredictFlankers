package neurogame.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

//import neurogame.game.Game.GameStatus;

public class FlankersGame extends JPanel implements KeyListener, ActionListener
{
  enum GameStatus
  {
    SHOW_INSTRUCTION,
    IN_PRACTICE,
    PRACTICE_OVER,
    IN_BLOCK,
    BLOCK_OVER,
    END_OF_BLOCKS,
  }
  
  private static final long serialVersionUID = 1L;

  private boolean inputReceived = false;
  //first delay is 1000 ms
  private int flankerSize; //will be set in the option panel
  private int notificationSize; //will be set in the option panel

  int currentInstructionIndex = 0;
  private JLabel currentInstruction;      //shown on the screen
  private JLabel arrows;                  //flankers

  private Timer timer;  //used for the game logic

  //names: <flankersDirection><middleArrowDirection><flankersDirection>
  private int middleArrowDirection; //0=left, 1=right, 2=paused
  //&lt = < , &gt = >
  private final String leftLeft = "&lt &lt &lt &lt &lt";
  private final String leftRight = "&lt &lt &gt &lt &lt";
  private final String rightRight = "&gt &gt &gt &gt &gt";
  private final String rightLeft = "&gt &gt &lt &gt &gt";

  //flanker lists
  private ArrayList<String> practiceSet = new ArrayList<>();
  private ArrayList<String> gameSet = new ArrayList<>();
  private int trialNumber;

  //fields for the game logic
  private boolean gameRunning;
  private long startTime = System.currentTimeMillis();
  private long currentTime = System.currentTimeMillis();
  private boolean paused = false;
  private boolean arrowShown = false;
//  private boolean inActualGame = false;
//  private boolean initialized = false;

  //the notifications, customizable fields
  private JLabel notification = new JLabel("TOO SLOW!");

  private boolean firstTime = true;
  private boolean middleArrowShown = false;

  //User modifiable inputs
  public int stimulusDuration = 1000; //stim. duration
  public int responseTimeOut = 1500;
  public int intervalMin = 800;
  public int intervalMax = 1800;

  public int middleArrowDelay = 32; 
  public int gameTrials = 4;
  public int practiceTrials = 4;
  public double percentIncongruent = 0;

  boolean showErrorFeedback = true; //default to true, can be false
  boolean showErrFBackOnPractice = true;

  boolean pauseCalculated = false;
  boolean delayOnFeedback = true;
  int feedBackDelay = 500; //FEED BACK DELAY MUST BE 
  int pauseDuration = 0;

  int numBlocks = 1;
  int currentBlock = 0;
  GameStatus gameStatus = GameStatus.SHOW_INSTRUCTION;
  
  String logFile = "";
  private BufferedImage countDown1;
  private BufferedImage countDown2;
  private BufferedImage countDown3;
  
  public FlankersGame()
  {
    timer = new Timer(1, this); 
    //for now, let's set the flanker size to 60
    //and set notification size to 40
    flankerSize = 90;
    notificationSize = 40;

    currentInstruction = new JLabel();
    currentInstruction.setHorizontalAlignment(JLabel.CENTER);
    currentInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);
    currentInstruction.setForeground(Color.WHITE);
    currentInstruction.setFont(new Font("Serif", Font.BOLD, 17));

    try
    {
      instructionFile = ImageIO.read(this.getClass()
                               .getResourceAsStream("/images/defaultInstruction.png"));
      practiceOver = ImageIO.read(this.getClass()
                            .getResourceAsStream("/images/practiceOver.png"));
      blockOver = ImageIO.read(this.getClass()
                         .getResourceAsStream("/images/blockOver.png"));
      allBlocksDone = ImageIO.read(this.getClass()
                             .getResourceAsStream("/images/allBlocksDone.png"));
      countDown1 = ImageIO.read(getClass().getResourceAsStream("/logos/cd1.png"));
      countDown2 = ImageIO.read(getClass().getResourceAsStream("/logos/cd2.png"));
      countDown3 = ImageIO.read(getClass().getResourceAsStream("/logos/cd3.png"));
      
    }
    catch (Exception e) {}
    
    arrows = new JLabel();
    arrows.setHorizontalAlignment(JLabel.CENTER);
    this.setBackground(Color.BLACK);
    this.addKeyListener(this);
    this.setPreferredSize(new Dimension(500, 500));

    this.setLayout(new BorderLayout());
    this.add(currentInstruction, BorderLayout.CENTER);
  }

  //generates the whole sequence for a flankers test. this works!
  private ArrayList<String> generateSequence(int totalNumTrials, 
      double percentIncongruent)
  {
    ArrayList<String> output = new ArrayList<>();
    //take max number of incongruent flanker arrows

    int incongruentCount = 0; //leftRight and rightLeft
    int congruentCount = 0;   //leftLeft and rightRight

    if(totalNumTrials%4 != 0)
    {
      System.out.println("total number of trials must be divisible by 4");
      return output;
    }
    if(percentIncongruent < 0)
    {
      //smallest is 0%
      percentIncongruent = 0;
    }
    if(percentIncongruent > 100)
    {
      //maxes out to 100%
      percentIncongruent = 100;
    }
    System.out.printf("In FlankersGame: percentIncongruent + %f", percentIncongruent);
    int maxIncongruent = (int) (totalNumTrials * (percentIncongruent/100));
    int maxCongruent = totalNumTrials - maxIncongruent;

    int i=0;
    while(i < totalNumTrials)
    {
      int flankerType = (int)(Math.random()*4)+1;
      if(flankerType == 1 && congruentCount < maxCongruent)
      {
        output.add(leftLeft);
        i++;
      }
      if(flankerType == 2 && incongruentCount < maxIncongruent)
      {
        output.add(leftRight);
        incongruentCount++;
        i++;
      }
      if(flankerType == 3 && congruentCount < maxCongruent)
      {
        output.add(rightRight);
        i++;
      }
      if(flankerType == 4 && incongruentCount < maxIncongruent)
      {
        output.add(rightLeft);
        incongruentCount++;
        i++;
      }
    }
    return output;
  }

  PrintWriter log;
  //fill up the instructions
  public void initialize()
  {
    try
    {
//      System.out.println("In Flanker's initialize: logFile="+logFile);
      log = new PrintWriter(new FileWriter(logFile));
      log.println("ID:");
      log.println("Date:");
      log.println("Protocol Name:");
      log.println("Trial,Trigger,RT,Acc");
      log.close();
    }
    catch(Exception e)
    {}
    repaint();
    practiceSet = generateSequence(practiceTrials, percentIncongruent);
    gameSet = generateSequence(gameTrials, percentIncongruent);
    
  }

  //*******************************************************
  // Description:
  //  actionPerformed serves as a game loop. Although
  //  
  //*******************************************************
  @Override
  public void actionPerformed(ActionEvent arg0)
  {
    if (gameStatus == GameStatus.IN_PRACTICE)
    {
      if (trialNumber >= practiceSet.size())
      {
        gameRunning = false;
        arrows.setForeground(Color.BLACK);
        gameStatus = GameStatus.PRACTICE_OVER;
//        timer.stop();
//        firstTrial = false;
      }
      else
      {
        gameLogic(practiceSet);
      }
     }
    if (gameStatus == GameStatus.PRACTICE_OVER || gameStatus == GameStatus.BLOCK_OVER)
    {
      endGame();
    }
    if(gameStatus == GameStatus.IN_BLOCK)
    {
//      System.out.println("gameSet.size()="+gameSet.size());
      if (trialNumber >= gameSet.size())
      {
        System.out.println("actionPerformed! gameStatus = IN_BLOCK");
        gameRunning = false;
        if (currentBlock < numBlocks)
          gameStatus = GameStatus.BLOCK_OVER;
        if (currentBlock >= numBlocks)
          gameStatus = GameStatus.END_OF_BLOCKS;
        repaint();
      }
      else
      {
        gameLogic(gameSet);
      }
    }
  }
  //prepares everything before the game or practice starts
  //MISSING: generateSequence must be called to set the value of 
  //practiceSet before going through the practice and 
  //gameSet before going through the real task.
  private void prepareGame()
  {
    currentInstruction.setText(""); //check here
    this.add(Box.createRigidArea(new Dimension(100,200)));
    arrows.setFont(new Font("Serif", Font.BOLD, flankerSize));
    arrows.setForeground(Color.WHITE);

    notification.setFont(new Font("Serif", Font.BOLD, notificationSize));
    notification.setHorizontalAlignment(JLabel.CENTER);
    notification.setAlignmentX(Component.CENTER_ALIGNMENT);
    notification.setForeground(Color.BLACK);

    this.add(arrows);
    this.add(Box.createRigidArea(new Dimension(10,10)));
    this.add(notification, BorderLayout.CENTER);

    //    practiceStartTime = System.currentTimeMillis(); //for the practice
    gameRunning = true;
    //a little hack so hat notification don't show up first thing in the
    //game logic.
  }

  //blacks out the notification in the end of the game
  private void endGame()
  {
    notification.setForeground(Color.BLACK);
    inputReceived = true;
    trialNumber = 0; //so the actual gameSet will work properly
    firstTrial = true;
  }

  @Override
  public void keyPressed(KeyEvent arg0)
  {
    //send triggers instead of printing out 
    int keyCode = arg0.getKeyCode();
   
    if (keyCode == KeyEvent.VK_D && gameStatus != GameStatus.IN_PRACTICE) FlankersEngine.logger.scheduleSocketOut(Game.KEY_D);
    else if (keyCode == KeyEvent.VK_K && gameStatus != GameStatus.IN_PRACTICE) FlankersEngine.logger.scheduleSocketOut(Game.KEY_K);
    
    if (keyCode == KeyEvent.VK_SPACE)
    {
      System.out.println("gameStatus="+gameStatus+", gameRunning="+gameRunning);
      if (gameStatus == GameStatus.SHOW_INSTRUCTION && !gameRunning)
      {
        gameStatus = GameStatus.IN_PRACTICE;
        repaint();
        prepareGame();
        start();
        return;
      }
      else if ((gameStatus == GameStatus.PRACTICE_OVER || gameStatus == GameStatus.BLOCK_OVER)
                && !gameRunning)
      {
        gameStatus = GameStatus.IN_BLOCK;
        repaint();
        System.out.println("repainted");
        prepareGame();
        currentBlock++;
        return;
      }
      else if (gameStatus == GameStatus.END_OF_BLOCKS)
      {
        if (log != null) log.close();
        System.exit(0);
      }
    }
    if(gameStatus == GameStatus.IN_PRACTICE || gameStatus == GameStatus.IN_BLOCK)
    {
      if(!inputReceived)
      {
        String acc = "0";
        long respTime = System.currentTimeMillis() - startTime;
        if(keyCode == KeyEvent.VK_D && middleArrowDirection == 0) 
        {
          System.out.println("input is given");
          userIsCorrect();
          acc = "1";
        }
        if(keyCode == KeyEvent.VK_K && middleArrowDirection == 0)
        {
          System.out.println("input is given");
          userIsIncorrect();
          acc = "0";
        }
        if(keyCode == KeyEvent.VK_K && middleArrowDirection == 1) 
        {
          System.out.println("input is given");
          userIsCorrect();
          acc = "1";
        }
        if(keyCode == KeyEvent.VK_D && middleArrowDirection == 1) 
        {
          System.out.println("input is given");
          userIsIncorrect();
          acc = "0";
        }
        
        String logEntry = (trialNumber+",")+getStimTrigger(arrowBeingShown)+","+(respTime+",")+acc;
        if (gameStatus == GameStatus.IN_BLOCK)
        {
          if (!logFile.equals("/.txt"))
          {
            try
            {
              log = new PrintWriter(new FileWriter(logFile, true));
              log.println(logEntry);
              log.close();
            }
            catch (Exception e)
            {}
          }
        }
      }
      
    }
  }
  
  //*******************************************************
  // Description:
  //  Never show that the user is correct.  
  //  Must call for proper behavior!
  //*******************************************************
  private void userIsCorrect()
  {
    arrows.setText("");
    inputReceived = true;
    notification.setText("CORRECT!");
    notification.setForeground(Color.BLACK);
    startTime = System.currentTimeMillis();
  }
  
  //*******************************************************
  // Description:
  //  Sets the boolean flags that describes if the user
  //  input is correct or incorrect when the user reacted
  //  incorrectly.
  //*******************************************************
  boolean userIncorrect = false;
  private void userIsIncorrect()
  {
    //depending on show error feedback status in game and practice
    arrows.setText("");
    System.out.println("In userIsIncorrect: arrows is set to nothing.");
    inputReceived = true;
    userIncorrect = true;
    Thread delayer = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        long startDelay = System.currentTimeMillis();
        long currDelay = System.currentTimeMillis();
        while (currDelay - startDelay < feedBackDelay)
        {
          currDelay = System.currentTimeMillis();
          continue;
        }
        userIncorrect = false;
        if (showErrorFeedback)
        {
          notification.setText("INCORRECT!"); 
          notification.setForeground(Color.RED);
          if (gameStatus != GameStatus.IN_PRACTICE)
          {
            FlankersEngine.logger.scheduleSocketOut(Game.NOTIF_INC);
          }
        }
      }
    });
    delayer.start();
    startTime = System.currentTimeMillis();
  }

  //*******************************************************
  // Description:
  //  This method refreshes the booleans that are used for
  //  the game logic. 
  //*******************************************************
  private void refresh()
  {
    if(arrowShown)
    {
      if(!inputReceived)
      {
        //put the notification back to too slow
        notification.setText("TOO SLOW!");
        notification.setForeground(Color.BLACK);
        this.add(notification);
      }
    }
  }


  //*******************************************************
  // Description:
  //  This method marks the beginning of the game in which
  //  the whole frame is shown.
  //*******************************************************
  public void start()
  {
    this.setFocusable(true);
    this.requestFocus();
    this.setVisible(true);
    timer.start();
  }

  
  boolean stimSent = false;
  //*******************************************************
  // Description:
  //  Contains the game logic of the game.
  //*******************************************************
  private void gameLogic(ArrayList<String> flankerSet)
  {
//    System.out.println("gameLogic() might have done something.");
    //System.out.println("in gameLogic(): trialNumber is " + trialNumber);
    if (userIncorrect) return;
    if(trialNumber >= flankerSet.size())
    {
      gameRunning = false;
      firstTrial = true; //so the very first trial will not be incremented (see showPlusSignWhileOnPause())
    }
    if(gameRunning)
    {
      refresh(); //always show tell the user if he/she is slow
      //Hide arrows when there's no button press
      if(!inputReceived)
      {
        arrows.setForeground(Color.WHITE);
      }
      else if (inputReceived)
      {
//        arrows.setForeground(Color.BLACK);
        arrows.setText(""); //seems like this fixed the "first trial repeated" problem
      }
      //get the currentTime every run on the game logic
      currentTime = System.currentTimeMillis();
      if(!arrowShown)
      {
        setUpTrial(flankerSet); //show the flankers only
        //        System.out.println("trial is set up!");
      }
      //flanker show time is the duration of showing the flanker
      if(!middleArrowShown && currentTime - startTime >= middleArrowDelay)
      {
        middleArrowShown = true;
        arrows.setText("<html><font color=white>" + flankerSet.get(trialNumber) + "</font></html>");//show the middle arrow
        //        System.out.println("middle arrow is shown now!");
      }
      if(paused == false && currentTime - startTime >= stimulusDuration) { 
        paused = true;
      }
      //if the arrows are supposed to be hidden
      if(paused)
      {
        arrows.setText("");
        arrowShown = false;
        //only check for being slow when currentime-startTime >= responseTimeOut
        if(currentTime - startTime >= responseTimeOut)
        {
          //          System.out.println("paused now, just waiting for response time out");
          showNotificationWhenUserIsSlow();
          hideArrowsWhileOnPause();
          middleArrowShown = false;
        }
      }
    }
  }
  //*************************************************************
  // Description:
  //  if the input is not received by the time of invocation of this method, 
  //  then the user is too slow.
  //*************************************************************
  private void showNotificationWhenUserIsSlow()
  {
    if(!inputReceived)
    {
      inputReceived = true; //user keyPress is not register anymore.
      if(firstTime)
      {
        notification.setForeground(Color.BLACK);
        firstTime = false;
      }
      else
      {
        //Show the Too Slow notification in red
        notification.setForeground(Color.RED);
        if (gameStatus != GameStatus.IN_PRACTICE)
        {
          FlankersEngine.logger.scheduleSocketOut(Game.NOTIF_SLOW);
        }
        System.out.println("Too Slow.");
        //Resetting startTime is important! This marks the
        //startTime used for the logic of hideArrowsWhileOnPause
        //Log too slow here
        if (gameStatus == GameStatus.IN_BLOCK)
        {
          if (!logFile.equals("/.txt"))
          {
            try
            {
              log = new PrintWriter(new FileWriter(logFile, true));
              log.println(trialNumber+","+getStimTrigger(arrowBeingShown)+",NaN,NaN");
              log.close();
            }
            catch (Exception e)
            {}
          }
        }
        startTime = System.currentTimeMillis();
      }
    }
  }

  private boolean firstTrial = true;
  //*************************************************************
  // Description:
  //  if the game is paused, the logic within this method is used.
  //*************************************************************
  private void hideArrowsWhileOnPause()
  {
    if(!pauseCalculated)
    {
      pauseDuration= (int)(Math.random()*
          (intervalMax-intervalMin))+intervalMin;
      pauseCalculated = true;
      System.out.println("Trial Number " + trialNumber);
      System.out.printf("pause duration: %d\n", pauseDuration);
    }
    if(currentTime - startTime >= pauseDuration)
    {
      pauseCalculated = false; 
      paused = false;
      inputReceived = false; //Make arrow's foreground white again
      startTime = System.currentTimeMillis(); 
      this.remove(notification);
      this.add(arrows);
      if(firstTrial)
      {
        System.out.println("firstTrial is true and gameStatus="+gameStatus);
        firstTrial = false;
      }
      else
      {
        stimSent = false;
        trialNumber++;
      }
    }
  }
  String arrowBeingShown = "";
  //no middle arrows
  private void setUpTrial(ArrayList<String> flankerSet)
  {
    String currentArrows = flankerSet.get(trialNumber);
    String currentFlankers = "<html>" 
        + "<font color=white>" + currentArrows.substring(0,8) + "</font>" 
        + "<font color=black>&lt</font>"
        + "<font color=white>" + currentArrows.substring(11,19) + "</font>"
        + "</html>";
    //send the trigger to the EEG
    if (!stimSent)
    {
      System.out.println("sending stim trigger");
      if (gameStatus != GameStatus.IN_PRACTICE)
      {   
        FlankersEngine.logger.scheduleSocketOut(getStimTrigger(currentArrows));
        arrowBeingShown = currentArrows;
      }
      stimSent = true;
    }
    if(currentArrows.substring(8,11).equals("&lt"))
    {
      //      System.out.println(currentArrows.substring(8,11));
      middleArrowDirection = 0;
    }
    if(currentArrows.substring(8,11).equals("&gt"))
    {
      //      System.out.println(currentArrows.substring(8,11));
      middleArrowDirection = 1;
    }
    arrowShown = true;
    arrows.setText(currentFlankers);
  }

  //*******************************************************
  // Irrelevant or Inheritance Required Methods
  //*******************************************************
  public void keyReleased(KeyEvent arg0)
  {
  }
  public void keyTyped(KeyEvent arg0)
  {
  }
  
  BufferedImage instructionFile = null;
  BufferedImage practiceOver = null;
  BufferedImage blockOver = null;
  BufferedImage allBlocksDone = null;
  
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    if (intermission == 3) 
    {
      g.drawImage(countDown3, centerX(countDown3), centerY(countDown3), null);
      return;
    }
    else if (intermission == 2)
    {
      g.drawImage(countDown2, centerX(countDown2), centerY(countDown2), null);
      return;
    }
    else if (intermission == 1)
    {
      g.drawImage(countDown1, centerX(countDown1), centerY(countDown1), null);
      return;
    }
    
    if (gameStatus == GameStatus.SHOW_INSTRUCTION)
    {
      g.drawImage(instructionFile, centerX(instructionFile), centerY(instructionFile), null);
    }
    else if (gameStatus == GameStatus.IN_PRACTICE ||
             gameStatus == GameStatus.IN_BLOCK)
    {
      g.drawImage(null, 0, 0, null);
    }
    else if (gameStatus == GameStatus.PRACTICE_OVER)
    {
      g.drawImage(practiceOver, centerX(practiceOver), centerY(practiceOver), null);
    }
    else if (gameStatus == GameStatus.BLOCK_OVER)
    {
      g.drawImage(blockOver, centerX(blockOver), centerY(blockOver), null);
    }
    else if (gameStatus == GameStatus.END_OF_BLOCKS)
    {
      g.drawImage(allBlocksDone, centerX(allBlocksDone), centerY(allBlocksDone), null);
    }
  }
  
  /**
   * @param image
   * @return the x coordinate of the top left box in which the
   *      image will be printed
   */
  protected int centerX(BufferedImage image) {
    return (this.getWidth() - image.getWidth(null)) / 2;
  }
  /**
   * @param image
   * @return the y coordinate of the top left box in which the
   *      image will be printed
   */
  protected int centerY(BufferedImage image) {
    return (this.getHeight() - image.getHeight(null)) / 2;
  }
  
  int intermission = 0;
  boolean inIntermission = true;
  public void countDown()
  {
    inIntermission = true;
    
    long timeSinceIntermission = System.currentTimeMillis();
    long currTime = System.currentTimeMillis();
    
    intermission = 3;
    repaint();
    while (inIntermission)
    {
      currTime = System.currentTimeMillis();
      if (currTime - timeSinceIntermission == 1000)
      {
        intermission--;
        if (intermission == 0)
        {
          inIntermission = false;
          break;
        }
        repaint();
        timeSinceIntermission = System.currentTimeMillis();
      }
    }
  }
  
  //used to find what trigger to send for the stimulus
  private byte getStimTrigger(String trial)
  {
    if (trial.equals(leftLeft)) return Game.STIM_LL;
    else if (trial.equals(leftRight)) return Game.STIM_LR;
    else if (trial.equals(rightRight)) return Game.STIM_RR;
    else
      return Game.STIM_RL; 
  }
  
}
