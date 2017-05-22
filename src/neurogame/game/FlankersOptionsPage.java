package neurogame.game;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class FlankersOptionsPage extends JDialog
{
  private static final long serialVersionUID = 1L;

  private FlankersGame game;
  //Labels:
  private JLabel L1 = new JLabel("Stimulus Duration (ms):");
  private JLabel L2 = new JLabel("Response Timeout (ms):");
  private JLabel L3 = new JLabel("Intertrial Interval");
  private JLabel L4 = new JLabel("from:");
  private JLabel L5 = new JLabel("to:");
  private JLabel L6 = new JLabel("Stimuli Sequence Distribution");
  private JLabel L7 = new JLabel("Uniform");
  private JLabel L8 = new JLabel("Gaussian");
  private JLabel L9 = new JLabel("User-Input");
  private JLabel L10 = new JLabel("Number of Trials:");
  private JLabel L11 = new JLabel("% Incongruent:");
  private JLabel L13 = new JLabel("Show error feedback");
  private JLabel L14 = new JLabel("Feedback delay:");
  private JLabel L15 = new JLabel("Delay Duration:");
  private JLabel L16 = new JLabel("# Practice Trials:");
  private JLabel L17 = new JLabel("Feedback during practice:");
  private JLabel L18 = new JLabel("Save Protocol As:");
  //TextFields:
  Dimension textFieldSize = new Dimension(50, 20);
  private JTextField T1, T2, T4, T5, T10, T11, T12, T13,
                     T15, T16, T18;
  {
    T1 = new JTextField();
    T1.setPreferredSize(textFieldSize);
    T2 = new JTextField();
    T2.setPreferredSize(textFieldSize);
    T4 = new JTextField();
    T4.setPreferredSize(textFieldSize);
    T5 = new JTextField();
    T5.setPreferredSize(textFieldSize);
    T10 = new JTextField();
    T10.setPreferredSize(textFieldSize);
    T11 = new JTextField();
    T11.setPreferredSize(textFieldSize);
    T13 = new JTextField();
    T13.setPreferredSize(textFieldSize);
    T15 = new JTextField();
    T15.setPreferredSize(textFieldSize);
    T16 = new JTextField();
    T16.setPreferredSize(textFieldSize);
    T18 = new JTextField();
    T18.setPreferredSize(new Dimension(400, 20));
  }
  //Radio Buttons for gridBag3():
  JRadioButton R1 = new JRadioButton();
  JRadioButton R2 = new JRadioButton();
  JRadioButton R3 = new JRadioButton();
  //CheckBox for gridBag4():
  JCheckBox CB13 = new JCheckBox();
  JCheckBox CB14 = new JCheckBox();
  JCheckBox CB17 = new JCheckBox();
  //Accept the changes:
  JButton acceptButton = new JButton("Accept Changes");
  
  //Constraints for all the gridBag() calls
  GridBagConstraints c = new GridBagConstraints();
  
  public FlankersOptionsPage(FlankersFrame owner, String title,
                     Dialog.ModalityType modalityType,
                     FlankersGame game)
  {
    super(owner, title, modalityType);
    this.game = game;
    this.setLayout(new GridLayout(8,1)); 
    c.anchor = GridBagConstraints.LINE_START;
    c.insets = new Insets(2,5,2,5);

    this.add(gridBag1());
    this.add(gridBag2());
    this.add(gridBag3());
    this.add(gridBag4());
    this.add(gridBag5());
    this.add(gridBag6());
    this.add(gridBag7());
    this.add(gridBag8());
    setUpAcceptButton();
    
    initializeGameFieldValues();
    //this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.requestFocus();
    this.setSize(600, 750);
    this.setResizable(false);
  }
  
  public void showOptionsPage()
  {
    this.setVisible(true);
  }
  
  private JPanel gridBag1()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    
    c.gridx = 0;
    c.gridy = 0;
    panel.add(L1, c);
    c.gridx = 1;
    panel.add(T1, c);

    c.gridx = 0;
    c.gridy = 1;
    panel.add(L2, c);
    c.gridx = 1;
    panel.add(T2, c);
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    return panel;
  }
  
  private JPanel gridBag2()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    
    c.anchor = GridBagConstraints.CENTER;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 4;
    panel.add(L3, c);
    
    c.anchor = GridBagConstraints.LINE_START;
    c.gridwidth = 1;
    
    c.gridx = 0;
    c.gridy = 1;
    panel.add(L4, c);
    c.gridx = 1;
    panel.add(T4, c);
    c.gridx = 2;
    panel.add(L5, c);
    c.gridx = 3;
    panel.add(T5, c);
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    return panel;
  }
  
  JLabel numBlocks = new JLabel("Number of Blocks:");
  JTextField numBlockField = new JTextField();
  private JPanel gridBag3()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    
    c.gridx = 0;
    c.gridy = 0;
    panel.add(L10, c);
    c.gridx = 1;
    panel.add(T10, c);
    c.gridx = 2;
    
    panel.add(L16, c);
    c.gridx = 3;
    panel.add(T16, c);
    
    c.gridx = 0;
    c.gridy = 1;
    panel.add(L11, c);
    c.gridx = 1;
    panel.add(T11, c);
    
    numBlockField.setPreferredSize(textFieldSize);
    c.gridx = 2;
    panel.add(numBlocks, c);
    c.gridx = 3;
    panel.add(numBlockField, c);
    
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    return panel;
  }
  
  private JPanel gridBag4()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    
    CB13.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (!CB13.isSelected())
        {
          CB14.setSelected(false);
          CB14.setEnabled(false);
          T15.setText("0");
          T15.setEnabled(false);
        }
        else if (CB13.isSelected())
        {
          CB14.setSelected(true);
          CB14.setEnabled(true);
          T15.setEnabled(true);
        }
      } 
    });
    
    c.gridx = 0;
    c.gridy = 0;
    panel.add(L13, c);
    c.gridx = 1;
    panel.add(CB13, c);

    c.gridx = 2;
    panel.add(L17, c);
    c.gridx = 3;
    panel.add(CB17, c);

    c.gridy = 1;
    c.gridx = 0;
    panel.add(L14, c);
    c.gridx = 1;
    panel.add(CB14, c);
    c.gridx = 2;
    panel.add(L15, c);
    c.gridx = 3;
    panel.add(T15, c);
    
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    return panel;
  }
  
  JLabel instructionFile = new JLabel("Instruction File:");
  JButton openInstructionButton = new JButton("Find");
  JTextField instructionFileName = new JTextField();
  JFileChooser instructionFileChooser = new JFileChooser();
  JTextField saveProtDir = new JTextField();
  JTextField saveProtName = new JTextField();
  
  private JPanel gridBag5()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    instructionFileName.setPreferredSize(new Dimension(300,20));
    
    openInstructionButton.setPreferredSize(new Dimension(60,20));
    
    instructionFileChooser.setCurrentDirectory(new File("C:/"));
    instructionFileChooser.setDialogTitle("Find Instruction File");
    
    openInstructionButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (instructionFileChooser.showOpenDialog(openInstructionButton) ==
            JFileChooser.APPROVE_OPTION)
        {
          instructionFileName.setText(instructionFileChooser.getSelectedFile().getAbsolutePath());
        }
      }
    });
    
    c.gridx = 0;
    c.gridy = 0;
    panel.add(instructionFile, c);
    c.gridx = 1;
    panel.add(instructionFileName, c);
    c.gridx = 2;
    panel.add(openInstructionButton, c);
    
    JLabel l = new JLabel("Save Protocol in: ");
    JButton findDir = new JButton("Find Where");
    JFileChooser dirs = new JFileChooser();
    JLabel l2 = new JLabel("Protocol Name: ");
    
    saveProtDir.setPreferredSize(new Dimension(300,20));
    saveProtName.setPreferredSize(new Dimension(300, 20));
    
    findDir.setPreferredSize(new Dimension(100, 20));
    dirs.setCurrentDirectory(new File("C:/"));
    dirs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    c.gridy = 1;
    c.gridx = 0;
    panel.add(l, c);
    c.gridx = 1;
    panel.add(saveProtDir, c);
    c.gridx = 2;
    panel.add(findDir, c);
    
    findDir.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        
        if (dirs.showOpenDialog(findDir) == JFileChooser.APPROVE_OPTION)
        {
          saveProtDir.setText(dirs.getSelectedFile().getAbsolutePath());
        }
      }
    });
    
    c.gridx = 0;
    c.gridy = 2;
    panel.add(l2, c);
    c.gridx = 1;
    panel.add(saveProtName, c);
    
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    return panel;
  }
  
  JCheckBox useExistingProtocol = new JCheckBox();
  JTextField existingProtocolName = new JTextField();
  private JPanel gridBag6()
  {
    JButton findExistingProt = new JButton("Find");
    
    JPanel panel = new JPanel(new GridBagLayout());
    JPanel panel1 = new JPanel(new GridBagLayout());
    JPanel panel2 = new JPanel(new GridBagLayout());
    
    JLabel l1 = new JLabel("OR Use Exisiting Protocol");
    JLabel l2 = new JLabel("Existing Protocol:");
    c.gridx = 0;
    c.gridy = 0;
    panel1.add(l1, c);
    c.gridx = 1;
    panel1.add(useExistingProtocol, c);
    
    useExistingProtocol.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (useExistingProtocol.isSelected())
        {
          existingProtocolName.setEnabled(true);
          findExistingProt.setEnabled(true);
        }
        else if (!useExistingProtocol.isSelected())
        {
          existingProtocolName.setEnabled(false);
          findExistingProt.setEnabled(false);
        }
      }
    });
    
    if (!useExistingProtocol.isSelected())
    {
      existingProtocolName.setEnabled(false);
      findExistingProt.setEnabled(false);
    }
    
    JFileChooser exProtChooser = new JFileChooser();
    exProtChooser.setCurrentDirectory(new File("C:/"));
    
    findExistingProt.setPreferredSize(new Dimension(60, 20));
    
    existingProtocolName.setPreferredSize(new Dimension(300, 20));
    
    findExistingProt.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (exProtChooser.showOpenDialog(findExistingProt) == 
            JFileChooser.APPROVE_OPTION)
        {
          existingProtocolName.setText(exProtChooser.getSelectedFile().getAbsolutePath());
        }
      }
    });
    
    c.gridx = 0;
    c.gridy = 1;
    panel2.add(l2,c);
    c.gridx = 1;
    panel2.add(existingProtocolName, c); 
    c.gridx = 2;
    panel2.add(findExistingProt, c);
    
    c.gridx = 0;
    c.gridy = 0;
    panel.add(panel1, c);
    c.gridy = 1;
    panel.add(panel2, c);
    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    return panel;
  }
  JTextField logFileDir = new JTextField();
  JTextField logFileName = new JTextField();
  private JPanel gridBag7()
  {
    JLabel l = new JLabel("Save Log File in: ");
    JLabel l2 = new JLabel("Log File Name:");
    
    JFileChooser jfc = new JFileChooser();
    jfc.setCurrentDirectory(new File("C:/"));
    jfc.setDialogTitle("Save Log File in");
    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    //TODO: create a field for Dimension(300, 20), creating too many Dimension "objects"
    logFileDir.setPreferredSize(new Dimension(300, 20));
    logFileName.setPreferredSize(new Dimension(300, 20));
    
    JButton find = new JButton("Find Where");
    find.setPreferredSize(new Dimension(100, 20));
    find.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (jfc.showOpenDialog(find) == JFileChooser.APPROVE_OPTION)
        {
          logFileDir.setText(jfc.getSelectedFile().getAbsolutePath());
        }
      }
    });
    
    JPanel panel = new JPanel(new GridBagLayout());
    c.gridx = 0;
    c.gridy = 0;
    panel.add(l,c);
    c.gridx = 1;
    panel.add(logFileDir, c);
    c.gridx = 2;
    panel.add(find, c);
    
    c.gridy = 1;
    c.gridx = 0;
    panel.add(l2, c);
    c.gridx = 1;
    panel.add(logFileName, c);
    
    return panel;
  }
  
  private JPanel gridBag8()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    c.gridx = 0;
    c.gridy = 0;
    panel.add(acceptButton);
    return panel;
  }
  
  private void setUpAcceptButton()
  {
    acceptButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        accept();
      }
    });
  }
  
  private void accept()
  {
    //read the protocol file if wanted
    try
    {
      if (useExistingProtocol.isSelected())
      {
        BufferedReader bR = new BufferedReader(new FileReader(existingProtocolName.getText()));
        String entry = bR.readLine();
        T1.setText(entry);
        entry = bR.readLine();
        T2.setText(entry);
        entry = bR.readLine();
        T4.setText(entry);
        entry = bR.readLine();
        T5.setText(entry);
        entry = bR.readLine();
        T10.setText(entry);
        entry = bR.readLine();
        T16.setText(entry);
        entry = bR.readLine();
        T11.setText(entry);
        entry = bR.readLine();
        numBlockField.setText(entry);
        
        entry = bR.readLine();
        if (entry.equals("WEF")) CB13.setSelected(true);
        else if (entry.equals("NEF")) CB13.setSelected(false);
        
        entry = bR.readLine();
        if (entry.equals("WPEF")) CB17.setSelected(true);
        else if (entry.equals("NPEF")) CB17.setSelected(false);
        
        entry = bR.readLine();
        if (entry.equals("HFD")) CB14.setSelected(true);
        else if (entry.equals("NFD")) CB14.setSelected(false);
        
        entry = bR.readLine();
        T15.setText(entry);
        bR.close();
      }
    }
    catch (Exception e)
    {}
    errorCheck();
    if (!errorOnFields)
    {
      writeToProtocolFile();
      game.stimulusDuration = Integer.parseInt(T1.getText());
      game.responseTimeOut = Integer.parseInt(T2.getText());
      game.intervalMin = Integer.parseInt(T4.getText());
      game.intervalMax = Integer.parseInt(T5.getText());

      game.gameTrials = Integer.parseInt(T10.getText());
      game.percentIncongruent = Double.parseDouble(T11.getText());
      game.practiceTrials = Integer.parseInt(T16.getText());

      game.delayOnFeedback = CB14.isSelected();
      if(!CB14.isSelected())
      {
        game.feedBackDelay = 0;
      }
      else
      {
        game.feedBackDelay = Integer.parseInt(T15.getText());
      }
      //for the checkbox (CB13) show error feedback
      game.showErrorFeedback = CB13.isSelected();
      game.showErrFBackOnPractice = CB17.isSelected();

      game.numBlocks = Integer.parseInt(numBlockField.getText());
      game.logFile = logFileDir.getText() +"/"+ logFileName.getText()+".txt";
      System.out.println("showErrorFeedback in FlankersGame: " + game.showErrorFeedback);
      try
      {
        game.instructionFile = ImageIO.read(new File(instructionFileName.getText()));
      } 
      catch (Exception e) {}
      //reinitialize the game:
      game.initialize();
      closeOptionsPage();
    }
    errorOnFields = false;
  }
  
  private boolean errorOnFields = false;
  private void errorCheck()
  {
    //for the trials
    int nTrials = Integer.parseInt(T10.getText());
    double percentInc = Double.parseDouble(T11.getText());
    int pracTrials = Integer.parseInt(T16.getText());
        
    //for the stim duration and ITI
    int stimulusDuration = Integer.parseInt(T1.getText());
    int responseTimeOut = Integer.parseInt(T2.getText());
    int intervalMin = Integer.parseInt(T4.getText());
    int intervalMax = Integer.parseInt(T5.getText());
    int delayDur = Integer.parseInt(T15.getText());
    int numBlocks = Integer.parseInt(numBlockField.getText());
    
    if (nTrials < 0) 
    {
      showError("Number of Trials must be larger than or equal to 0");
      errorOnFields = true;
      return;
    }
    if (stimulusDuration < 0)
    {
      showError("Stimulus Duration(ms) must be larger than 0");
      errorOnFields = true;
      return;
    }
    if (responseTimeOut < 0)
    {
      showError("Response Timeout(ms) must be larger than 0");
      errorOnFields = true;
      return;
    }
    if (intervalMin < 0)
    {
      showError("ITI min(ms) must be larger than 0");
      errorOnFields = true;
      return;
    }
    if (intervalMax < 0)
    {
      showError("ITI max(ms) must be larger than 0");
      errorOnFields = true;
      return;
    }
    if (delayDur < 0)
    {
      showError("Delay Duration(ms) must be larger than 0");
      errorOnFields = true;
      return;
    }
    
    if (percentInc < 0 || percentInc > 100)
    {
      showError("% Incongruent must be between 0 and 100 (inclusive)");
      errorOnFields = true;
      return;
    }
    
    if (numBlocks < 1)
    {
      showError("Number of Blocks must be larger than 1");
      errorOnFields = true;
      return;
    }
 
    if (stimulusDuration > responseTimeOut)
    {
      showError("Stimulus Duration(ms) must be less than or equal to Response Timeout(ms)");
      errorOnFields = true;
      return;
    }
    if (intervalMin > intervalMax)
    {
      showError("ITI min(ms) must be less than or equal to ITI max(ms)");
      errorOnFields = true;
      return;
    }   
  }
  
  private void showError(String errMessage)
  {
    JOptionPane.showMessageDialog(this, 
        errMessage, 
        "Error", JOptionPane.ERROR_MESSAGE);
  }
  
  
  private void writeToProtocolFile()
  {
    String filePath = saveProtDir.getText()+"/"+saveProtName.getText();
    try
    {
      PrintWriter protFile = new PrintWriter(filePath);
      protFile.println(T1.getText());
      protFile.println(T2.getText());
      protFile.println(T4.getText());
      protFile.println(T5.getText());
      protFile.println(T10.getText());
      protFile.println(T16.getText());
      protFile.println(T11.getText());
      protFile.println(numBlockField.getText());
      
      if (CB13.isSelected()) protFile.println("WEF");
      else if (!CB13.isSelected()) protFile.println("NEF");
      
      if (CB17.isSelected()) protFile.println("WPEF");
      else if (!CB17.isSelected()) protFile.println("NPEF");
      
      if (CB14.isSelected()) protFile.println("HFD");
      else if (!CB14.isSelected()) protFile.println("NFD");
      
      protFile.println(T15.getText());
      protFile.close();
    }
    catch (FileNotFoundException e)
    {
      System.out.println("No such file!");
    }
  }
  
  private void initializeGameFieldValues()
  {
    //concatenate "" to make the numerical values as strings
    T1.setText(game.stimulusDuration + "");
    T2.setText(game.responseTimeOut + "");
    T4.setText(game.intervalMin + "");
    T5.setText(game.intervalMax + "");
    
    T10.setText(game.gameTrials + "");
    T11.setText(game.percentIncongruent+"");
    
    CB13.setSelected(game.showErrorFeedback);//show error feedback by default
    CB14.setSelected(game.delayOnFeedback);
    //so that the text field for feed back delay turns grey when the checkbox is not checked
    CB14.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        T15.setEnabled(CB14.isSelected());
      }
    });   
    T15.setText(game.feedBackDelay + "");
    if (!CB14.isSelected()) T15.setEnabled(false);
    else if (CB14.isSelected()) T15.setEnabled(true);
    
    T16.setText(game.practiceTrials + "");
    CB17.setSelected(game.showErrFBackOnPractice);
    numBlockField.setText(game.numBlocks+"");
    
  }
  
  private void closeOptionsPage()
  {
    this.dispose();
    System.out.println("should be closed now");
  }
}
