import java.awt.Component;
import javax.swing.JFrame;

public class Game extends JFrame
{
	private static final int WIDTH = 1033;
	private static final int HEIGHT = 800;

	public Game()
	{
		super("Super UnExtreme Tower Defense X"); 
		setSize(WIDTH,HEIGHT);
		
		Background theGame = new Background(WIDTH, HEIGHT);
		((Component)theGame).setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().add(theGame);
		
		setVisible(true);
	}
	
	public static void main( String args[] )
	{
		Game run = new Game();
	}
}