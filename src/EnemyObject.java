import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EnemyObject {
	private int xPos, yPos, hp, power, speed, defaultSpeed, attackRate;
	private long timeUntilAttack;
	private String type;
	private BufferedImage enmy;
	private Rectangle rect;
	
	public EnemyObject(int x, int y, int health, int pow, int spd, int attack, String enemyImg) {
		xPos = x;
		yPos = y;
		hp = health;
		power = pow;
		speed = defaultSpeed = spd;
		attackRate = attack;
		timeUntilAttack = System.currentTimeMillis();
		type = enemyImg;
		
		try {
			enmy = ImageIO.read(new File("media\\images\\enemies\\" + enemyImg + ".png"));
		} catch (IOException e1) {
			System.out.println("Curse your sudden but inevitable betrayal (Can't find " + enemyImg + " image)");
			e1.printStackTrace();
		}
		
		rect = new Rectangle(xPos, yPos, enmy.getWidth(), enmy.getHeight());
	}
	
	public EnemyObject(int x, int y, TowerAttributes atrib) {
		xPos = x;
		yPos = y;
		hp = atrib.getHP();
		power = atrib.getPow();
		speed = defaultSpeed = atrib.getSpeed();
		attackRate = atrib.getAttackRate();
		type = atrib.getType();
		
		try {
			enmy = ImageIO.read(new File("media\\images\\enemies\\" + type + ".png"));
		} catch (IOException e) {
			System.out.println("Curse your sudden but inevitable betrayal (Can't find " + type + " image)");
			e.printStackTrace();
		}
		
		rect = new Rectangle(xPos, yPos, enmy.getWidth(), enmy.getHeight());
	}
	
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
	
	public void setSpeed(int spd) {
		speed = spd;
	}
	
	public void resetSpeed() {
		speed = defaultSpeed;
	}
	
	public void setType(String enemyT) {
		type = enemyT;
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
	
	public int getSpeed() {
		return speed;
	}
	
	public String getType() {
		return type;
	}
	
	public Rectangle getRect() {
		return rect;
	}	
	
	public int attack() {
		if (System.currentTimeMillis()-timeUntilAttack >= attackRate) {
			timeUntilAttack = System.currentTimeMillis();
			return power;
		}
		
		return 0;
	}
	
	public void action() {
		//System.out.println("action outside loop");
		if(xPos != 0){
			//System.out.println("in loop before setX");
			xPos -= speed;
			//System.out.println("after setX");
		}
		//System.out.println("oustide loop");
		rect.setLocation(xPos, yPos);
	}
	
	public void draw(Graphics window) {
		window.drawImage(enmy, xPos, yPos, null);
		//window.drawImage(enmy, getX(), getY(), enmy.getWidth(), enmy.getHeight(), null);
	}
	
	//public boolean collides()
	
	public String toString() {
		/*System.out.println("xpos: " + xPos + " ypos: " + yPos + " hp: " + hp +
			" power: " + power + " speed: " + speed);*/
		return getX() + " " + getY() + " " + getHP() + " " + getPow();
	}
}
