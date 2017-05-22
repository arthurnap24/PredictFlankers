package neurogame.game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FrontPage extends JPanel
{
  private static final long serialVersionUID = 1L;
  
  private FlankersGame game;
  private FlankersFrame frame;
  private JButton startGame = new JButton("Start Game");;
  private JButton options = new JButton("Options");;
  private JPanel top;
  private JPanel buttonContainer;
  private JPanel cards;
  private CardLayout cL;
  private ImageIcon PREDICT_LOGO;
  
  public FrontPage(FlankersGame game, FlankersFrame frame, JPanel cards, CardLayout cL)
  { 
    PREDICT_LOGO = new ImageIcon(this.getClass().getResource("/images/PredictLogo.png"));
    this.setLayout(new BorderLayout());
    this.cards = cards; //contains the JPanels
    this.cL = cL;       //CardLayout of cards

    //predict logo will be the replacement
    this.frame = frame;
    JLabel testLabel = new JLabel("TM", PREDICT_LOGO, JLabel.CENTER);
    testLabel.setForeground(Color.WHITE);
    testLabel.setHorizontalAlignment(JLabel.CENTER);
    
    buttonContainer = new JPanel();
    buttonContainer.setLayout(new GridBagLayout());
    
    this.game = game;
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(10, 10, 10, 10);
    
    buttonContainer.add(startGame, c);
    c.gridx = 0;
    c.gridy = 2;
    buttonContainer.add(options, c);
    
    buttonContainer.setBackground(Color.BLACK);
    this.add(buttonContainer, BorderLayout.CENTER);
    this.add(testLabel, BorderLayout.NORTH);
    
    initialize();
    this.setBackground(Color.BLACK);
    this.setVisible(true);
    this.requestFocus();
  }
  
  private void initialize()
  {
    options.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent arg0)
        {
          FlankersOptionsPage popUpDialog = new FlankersOptionsPage(frame, "Options",
              Dialog.ModalityType.APPLICATION_MODAL, game);   
          popUpDialog.showOptionsPage();
         }    
       }
    );
    startGame.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          //need to put the field value assigning here
          game.setVisible(true);
          game.requestFocus();
          cL.next(cards);
          
          System.out.println("pressed");
        }        
      }
    );
  }
}
