import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class AbstractTowerObject {
	protected int xPos, yPos, hp, power, attackRate, speed;
	protected long timeUntilAttack;
	protected String type;
	protected BufferedImage towerImage;
	protected Rectangle rect;
	
	public AbstractTowerObject(int x, int y, TowerAttributes attrib) {
		this(x, y, attrib.getHP(), attrib.getPower(), attrib.getAttackRate(), attrib.getSpeed(), attrib.getType());
	}
	
	public AbstractTowerObject(int x, int y, int health, int pow, int atkRate, int spd, String towerImg) {
		// Tower - health, power (of each bullet), fireRate, speed (how fast the bullet travels)
		// Enemy - health, power (of each attack), attackRate, speed (how fast it moves)
		setX(x);
		setY(y);
		setHP(health);
		setPower(pow);
		setAttackRate(atkRate);
		setType(towerImg);
		setSpeed(spd);
		setTimeUntilAttack(System.currentTimeMillis());
	}
	
	public abstract int action();
	
	public void draw(Graphics window) {
		window.drawImage(towerImage, xPos, yPos, null);
	}
	
	public String toString() {
		return String.format("Tower: %s HP: %d Power: %d [%d, %d]\n", type, hp, power, xPos, yPos);
	}
	
	//* Setters and Getters *//
	
	public void setX(int x) {
		xPos = x;
	}
	
	public void setY(int y) {
		yPos = y;
	}
	
	public void setHP(int health) {
		hp = health;
	}
	
	public void setPower(int pow) {
		power = pow;
	}
	
	public void setAttackRate(int fire) {
		attackRate = fire;
	}
	
	public void setSpeed(int spd) {
		speed = spd;
	}
	
	public void setTimeUntilAttack(long timeDelay) {
		timeUntilAttack = timeDelay;
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
	
	public int getPower() {
		return power;
	}
	
	public int getAttackRate() {
		return attackRate;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public long getTimeUntilAttack() {
		return timeUntilAttack;
	}
 
	public String getType() {
		return type;
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public int getWidth() {
		return towerImage.getWidth();
	}
	
	public int getHeight() {
		return towerImage.getHeight();
	}
}
