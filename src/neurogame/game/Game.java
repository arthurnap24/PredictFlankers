package neurogame.game;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public abstract class Game extends JPanel 
{ 
  public static final byte STIM_LL = 12;
  public static final byte STIM_LR = 13;
  public static final byte STIM_RR = 14;
  public static final byte STIM_RL = 15;
  
  public static final byte KEY_D = 16;
  public static final byte KEY_K = 17;
  
  public static final byte NOTIF_INC = 18;
  public static final byte NOTIF_SLOW = 19;
  
	private static final long serialVersionUID = 1L;
	protected Timer timer;
	protected Controller controller;
	protected boolean running;
	int width, height, timeMillis;
	
	public Game(int width, int height, int timeMillis) {
		this.width = width;
		this.height = height;
		this.timeMillis = timeMillis;
		
		try {
			Controllers.create();
		} catch(LWJGLException e) {
			e.printStackTrace();
		}
		Controllers.poll();
		for(int i=0 ; i < Controllers.getControllerCount() ; i++) {
			Controller option = Controllers.getController(i);
			if(option.getName().contains("Controller")) {
				controller = option;
			}
		}
	}
}
