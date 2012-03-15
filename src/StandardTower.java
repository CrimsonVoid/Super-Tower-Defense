import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;


public class StandardTower extends TowerObject {
	
	private Image tower;
	
	public StandardTower(int x, int y, int health, int pow) {
		super(x, y, health, pow);
		try
		{
			tower = ImageIO.read(new File("media\\images\\towers\\std.png")); 
		}
		catch(Exception e)
		{
			System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find standard tower image)");
		}
	}

	@Override
	public void draw(Graphics window) {
		window.drawImage(tower,getX(),getY(),80,80,null);
	}
}
