import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class EnemyObject extends AbstractTowerObject{
	private int defaultSpeed;
	
	public EnemyObject(int x, int y, TowerAttributes attrib) {
		this(x, y, attrib.getHP(), attrib.getPower(), attrib.getAttackRate(), attrib.getSpeed(), attrib.getType());
	}
	
	public EnemyObject(int x, int y, int health, int pow, int atkRate, int spd, String towerImg) {
		super(x, y, health, pow, atkRate, spd, towerImg);
		defaultSpeed = speed;
		
		try {
			towerImage = ImageIO.read(new File("media//images//enemies//" + type + ".png"));
		} catch (IOException e) {
			System.out.printf("Unable to find %s in EnemyObject\n", type);
		}
		
		rect = new Rectangle(xPos, yPos, towerImage.getWidth(), towerImage.getHeight());
	}
	
	public int action() {
		// Returns 0 if it is moving or 'power' if the cool down has expired
		
		if(speed > 0) {
			xPos -= speed;
			rect.setLocation(xPos, yPos);
		}
		
		else if (System.currentTimeMillis()-timeUntilAttack >= attackRate) {
				timeUntilAttack = System.currentTimeMillis();
				return power;
		}
		
		return 0;
	}
	
	public void resetSpeed() {
		setSpeed(defaultSpeed);
	}
}
