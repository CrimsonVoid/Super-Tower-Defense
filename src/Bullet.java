import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet {
	
	private int xPos, yPos, power, speed;
	private BufferedImage bullet;
	private Rectangle rect;
	
	public Bullet(int x, int y, int pow, int spd, String bulletType) {
		setX(x);
		setY(y);
		setPower(pow);
		setSpeed(spd);
		
		try {
			bullet = ImageIO.read(new File("media//images//bullet//" + bulletType + ".png"));
		} catch (IOException e) {
			System.out.printf("Unable to find %s image in Bullet. Attempting to load default image\n", bulletType);
			try {
				bullet = ImageIO.read(new File("media//images//bullet//bullet.png"));
			} catch (IOException e2) {
				System.out.printf("Unable to find default bullet image in Bullet\n");
			}
		}
		
		rect = new Rectangle(xPos, yPos, bullet.getWidth(), bullet.getHeight());
	}
	
	public void action() {
		xPos += speed;
		rect.setLocation(xPos, yPos);
	}
	
	public void draw(Graphics window) {
		window.drawImage(bullet, xPos, yPos, null);
	}
	
	public String toString() {
		return String.format("Power: %d Speed: %d [%d, %d]\n", power, speed, xPos, yPos);
	}
	
	//* Setters and Getters *//
	
	public void setX(int x) {
		xPos = x;
	}
	
	public void setY(int y) {
		yPos = y;
	}
	
	public void setPower(int pow) {
		power = pow;
	}
	
	public void setSpeed(int spd) {
		speed = spd;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public int getPower() {
		return power;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public Rectangle getRect() {
		return rect;
	}
}
