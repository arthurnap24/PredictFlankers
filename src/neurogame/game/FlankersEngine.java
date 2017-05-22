package neurogame.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.UIManager;

import neurogame.io.Logger;

public class FlankersEngine 
{
  
  public static Logger logger = new Logger();
  
  public static void main(String[] args) 
  {
    Timer t = new Timer(5, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        logger.updateSocket();
      }
    });
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {}
    FlankersFrame f = new FlankersFrame(800, 800);
    f.start();
    t.start();
  }
}
