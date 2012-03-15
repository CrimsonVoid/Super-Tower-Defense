import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;


public class NormalEnemy extends EnemyObject {
	
	private Image enemy;
	
	public NormalEnemy(int x, int y, int health, int pow, int spd) {
		super(x, y, health, pow, spd);
		try
		{
			enemy = ImageIO.read(new File("media\\images\\enemies\\nrml.jpg")); 
		}
		catch(Exception e)
		{
			System.out.println("Curse your sudden but inevitable betrayal(Can't find normal enemy image)");
		}
	}
}
