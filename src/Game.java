import java.awt.Component;
import javax.swing.JFrame;

public class Game extends JFrame {
	private static final long serialVersionUID = -7803629994015778818L;
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;

	public Game() {
		super("Super UnExtreme Tower Defense X"); 
		setSize(WIDTH,HEIGHT);
		
		Background theGame = new Background(WIDTH, HEIGHT);
		((Component)theGame).setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		getContentPane().add(theGame);
		
		setVisible(true);
	}
	
	public static void main(String args[]) {
		new Game();
	}
}