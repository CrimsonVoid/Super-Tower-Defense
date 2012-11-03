import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FinalStand {
	private int xPosLauncher, yPosLauncher, xPosBullet, yPosBullet;
	private boolean isActivated;
	private Rectangle rect;
	private BufferedImage launcherImage, launcherBullet;
	
	public FinalStand(int xP, int yP) {
		setXLauncher(xP);
		setYLauncher(yP);
		isActivated = false;
		
		try {
			launcherImage = ImageIO.read(new File("media//images//launcher.png"));
			launcherBullet = ImageIO.read(new File("media//images//bullet.png"));
		} catch (IOException e) {
			System.out.printf("Unable to find launcher/bullet in FinalStand\n");
		}
		

		setXBullet(xP / 2);
		setYBullet(yP + launcherImage.getWidth());
		
		rect = new Rectangle(xPosLauncher, yPosLauncher, launcherImage.getWidth(), launcherImage.getHeight());
	}
	
	public void action() {
		// Moves the bullet if launcher was hit

		if(xPosBullet >= Background.width) {
			rect.setLocation(-1, -1);
			rect.setSize(0, 0);
		}
		
		else if(isActivated && xPosBullet <= Background.width)
			rect.setLocation(xPosBullet += 2, yPosBullet);
	}
	
	public void draw(Graphics window) {
		// Draws the 'launcherImage' or 'bulletImage'
		
		if(isActivated && xPosBullet <= Background.width) {
			window.drawImage(launcherBullet, xPosBullet, yPosBullet, null);
			return;
		}
		
		else if (!isActivated)
			window.drawImage(launcherImage, xPosLauncher, yPosLauncher, null);
	}
	
	public String toString() {
		return String.format("isActive: %b [%d, %d]\n", isActivated, xPosLauncher, yPosLauncher);
	}

	//* Setters and Getters *//
	
	public void setXLauncher(int xP) {
		xPosLauncher = xP;
	}
		
	public void setYLauncher(int yP) {
		yPosLauncher = yP;
	}
	
	public void setXBullet(int xP) {
		xPosBullet = xP;
	}
	
	public void setYBullet(int yP) {
		yPosBullet = yP;
	}
	
	public void setActivated(boolean change) {
		isActivated = change;
		
		if(isActivated)
			rect.setSize(launcherBullet.getWidth(), launcherBullet.getHeight());
		else
			rect.setSize(launcherImage.getWidth(), launcherBullet.getHeight());
	}

	public int getXLauncher() {
		return xPosLauncher;
	}
	
	public int getYLauncher() {
		return yPosLauncher;
	}
	
	public int getXBullet() {
		return xPosBullet;
	}
	
	public int getYBullet() {
		return yPosBullet;
	}
	
	public boolean isActivated() {
		return isActivated;
	}

	public Rectangle getRect() {
		return rect;
	}
}
