import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TowerObject {
	private int xPos, yPos, hp, power, fireRate;
	private long timeUntilFire;
	private String type;
	private BufferedImage tower;
	private Rectangle rect;
	
	public TowerObject(int x, int y, int health, int pow, int fire, String towerImg) {
		xPos = x;
		yPos = y;
		hp = health;
		power = pow;
		fireRate = fire;
		type = towerImg;
		timeUntilFire = System.currentTimeMillis();
		
		try {
			tower = ImageIO.read(new File("media\\images\\towers\\" + towerImg + ".png"));
		} catch (IOException e) {
			System.out.println("Curse your sudden but inevitable betrayal (Can't find " + towerImg + " image)");
			e.printStackTrace();
		}
		
		rect = new Rectangle(xPos, yPos, tower.getWidth(), tower.getHeight());
	}
	
	public TowerObject(int x, int y, TowerAttributes atrib) {
		xPos = x;
		yPos = y;
		hp = atrib.getHP();
		power = atrib.getPow();
		fireRate = atrib.getSpeed();
		type = atrib.getType();
		timeUntilFire = System.currentTimeMillis();
		
		try {
			tower = ImageIO.read(new File("media\\images\\towers\\" + type + ".png"));
		} catch (IOException e) {
			System.out.println("Curse your sudden but inevitable betrayal (Can't find " + type + " image)");
			e.printStackTrace();
		}
		
		rect = new Rectangle(xPos, yPos, tower.getWidth(), tower.getHeight());
	}

	public int fire() {
		if (System.currentTimeMillis()-timeUntilFire >= fireRate) {
			timeUntilFire = System.currentTimeMillis();
			return power;
		}
		
		return 0;
	}

	public void draw(Graphics window) {
		window.drawImage(tower, xPos, yPos, null);
		//window.drawImage(tower, xPos, yPos, tower.getWidth(), tower.getHeight(), null);
	}

	public String toString() {
		//System.out.println("xpos: " + xPos + " ypos: " + yPos + " hp: " + hp + " power: " + power);
		return getX() + " " + getY() + " " + getHP() + " " + getPow();
	}
	
	//* Setter and Getter *//
	
	public void setX(int x) {
		xPos = x;
	}
	
	public void setY(int y) {
		yPos = y;
	}
	
	public void setHP(int health) {
		hp = health;
	}
	
	public void setPow(int pow) {
		power = pow;
	}
	
	public void setFireRate(int fire) {
		fireRate = fire;
	}
	
	public void setType(String newType) {
		type = newType;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public int getHP() {
		return hp;
	}
	
	public int getPow() {
		return power;
	}
	
	public int getFireRate() {
		return fireRate;
	}
	
	public int getWidth() {
		return tower.getWidth();
	}
	
	public int getHeight() {
		return tower.getHeight();
	}
 
	public String getType() {
		return type;
	}
	
	public Rectangle getRect() {
		return rect;
	}
}
