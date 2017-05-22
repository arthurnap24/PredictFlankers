package neurogame.game;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FlankersFrame extends JFrame
{
  private static final long serialVersionUID = 1L;
  private FlankersGame flankersGame;
  private FrontPage frontPage;
  
  public FlankersFrame(int width, int height)
  {
    JPanel cards = new JPanel();
    CardLayout cardLayout = new CardLayout();
    cards.setLayout(cardLayout);
    
    flankersGame = new FlankersGame();
    flankersGame.initialize();
    
    frontPage = new FrontPage(flankersGame, this, cards, cardLayout);
    
    this.setLayout(new BorderLayout());

    cards.add(frontPage, "front");
    cards.add(flankersGame, "game");
    
//    this.add(flankersGame);
    cardLayout.show(cards, "front");
    
    this.add(cards, BorderLayout.CENTER);
    
//    this.setSize(width, height);
    //TODO: undecorated for debugging purposes only
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setUndecorated(true);
    this.setResizable(true);
  }
  
  public void start()
  {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
//    flankersGame.start();
  }
  
  public FlankersGame getGame()
  {
    return flankersGame;
  }
}
