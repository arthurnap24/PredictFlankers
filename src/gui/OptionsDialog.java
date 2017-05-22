package gui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import neurogame.game.FlankersFrame;
import neurogame.game.FlankersGame;

public class OptionsDialog extends JMenuBar
{
  private static final long serialVersionUID = 1L;
  FlankersGame connectedGame;
  FlankersFrame connectedFrame;
  
  public OptionsDialog(FlankersFrame frame)
  {
    connectedGame = frame.getGame();
    connectedFrame = frame;
    
    JMenu fileMenu = new JMenu("File");
    JMenu customMenu = new JMenu("Customize");
    
    JMenuItem setITI = new JMenuItem("Customize ITI");
    customMenu.add(setITI);
    
    setITI.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            //popUp window in Customize JMenuItem
            JFrame popUp = new JFrame();
            JDialog popUpDialog = new JDialog(connectedFrame,
                                              "setITI",
                                              Dialog.ModalityType.APPLICATION_MODAL);
            popUpDialog.setLayout(new FlowLayout());
            popUpDialog.setBackground(Color.GRAY);
            
            //Button to save changes for the current run
            JButton confirmButton = new JButton("Confirm");
            confirmButton.setPreferredSize(new Dimension(80, 20));

            
            //Labels for the tex fields
            JLabel minLabel = new JLabel("From: ");
            JLabel maxLabel = new JLabel("To: ");
            JLabel flankerDurationLabel = new JLabel("Flanker Duration: ");
            
            JTextField minInterval = new JTextField();
            minInterval.setPreferredSize(new Dimension(50,20));
            
            JTextField maxInterval = new JTextField();
            maxInterval.setPreferredSize(new Dimension(50,20));
            
            JTextField flankerDuration = new JTextField();
            flankerDuration.setPreferredSize(new Dimension(50,20));
            
            
            confirmButton.addActionListener(new ActionListener()
            {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                System.out.println("Send the ITI to the Game.");
                connectedGame.intervalMin = Integer.
                    parseInt(minInterval.getText());
                connectedGame.intervalMax = Integer.
                    parseInt(maxInterval.getText());
                connectedGame.stimulusDuration = Integer.
                    parseInt(flankerDuration.getText());
                popUpDialog.dispose(); //closes when confirm button is clicked
              }
            });
            
            minLabel.setLabelFor(minInterval);
            maxLabel.setLabelFor(maxInterval);
            
            popUpDialog.add(minLabel);
            popUpDialog.add(minInterval);
            
            popUpDialog.add(maxLabel);
            popUpDialog.add(maxInterval);
            
            popUpDialog.add(flankerDurationLabel);
            popUpDialog.add(flankerDuration);
            
            popUpDialog.add(confirmButton);
            
            popUpDialog.setResizable(false);
            popUpDialog.setSize(200, 150 );
            popUpDialog.setTitle("Set ITI");
            popUpDialog.setDefaultCloseOperation(1); //so that the pop up window is the only thing that closes
            popUpDialog.setVisible(true);
          }
        });
    
    this.add(fileMenu);
    this.add(customMenu);
    
    frame.setJMenuBar(this);
  }
}
