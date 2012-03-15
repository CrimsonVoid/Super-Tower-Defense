import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class StrongEnemy extends EnemyObject {
	
	private Image enemy;
	
	public StrongEnemy(int x, int y, int health, int pow, int spd) {
		super(x, y, health, pow, spd);
		try
		{
			enemy = ImageIO.read(new File("media\\images\\enemies\\strong.jpg")); 
		}
		catch(Exception e)
		{
			System.out.println("Curse your sudden but inevitable betrayal(Can't find strong enemy image)");
			//feel free to do something here
		}
	}

	@Override
	public void draw(Graphics window) {
		window.drawImage(enemy,getX(),getY(),80,80,null);
		
	}

}
