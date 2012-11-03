import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TowerObject extends AbstractTowerObject{
	public TowerObject(int x, int y, TowerAttributes attrib) {
		this(x, y, attrib.getHP(), attrib.getPower(), attrib.getAttackRate(), attrib.getSpeed(), attrib.getType());
	}
	
	public TowerObject(int x, int y, int health, int pow, int atkRate, int spd, String towerImg) {
		super(x, y, health, pow, atkRate, spd, towerImg);
		
		try {
			towerImage = ImageIO.read(new File("media//images//towers//" + type + ".png"));
		} catch (IOException e) {
			System.out.printf("Unable to find %s in TowerObject\n", type);
		}
		
		rect = new Rectangle(xPos, yPos, towerImage.getWidth(), towerImage.getHeight());
	}

	public int action() {
		// if the cool down has expired return 'power' otherwise 0
		
		if (System.currentTimeMillis()-timeUntilAttack >= attackRate) {
			timeUntilAttack = System.currentTimeMillis();
			return power;
		}
		
		return 0;
	}
}
