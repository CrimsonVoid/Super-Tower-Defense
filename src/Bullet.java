import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet {
	
	private int xPos, yPos, power;
	private BufferedImage bullet;
	private Rectangle rect;
	
	public Bullet(int x, int y, int pow) {
		xPos = x;
		yPos = y;
		power = pow;
		
		try {
			bullet = ImageIO.read(new File("media\\images\\bullet.png"));
		} catch (IOException e1) {
			System.out.println("Curse your sudden but inevitable betrayal(Can't find bullet image)");
			e1.printStackTrace();
		}
		
		rect = new Rectangle(xPos, yPos, bullet.getWidth(), bullet.getHeight());
	}
	
	public void setX(int x) {
		xPos = x;
	}
	
	public void setY(int y) {
		yPos = y;
	}
	
	public void setPow(int pow) {
		power = pow;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public int getPow() {
		return power;
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public void action() {
		//TowerAttributes.bulletSpeed effect speed of bullet?
		xPos += 10;
		rect.setLocation(xPos, yPos);
	}
	
	public void draw(Graphics window) {
		window.drawImage(bullet, xPos, yPos, null);
		//window.drawImage(bullet, xPos, yPos, bullet.getWidth(), bullet.getHeight(), null);
	}
}
