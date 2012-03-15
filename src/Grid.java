import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

public class Grid{
	private int xPos;
	private int yPos;
	private Image grid;
	private boolean isAvailable;
	
	public Grid(int x, int y){
		setPos(x, y);
		isAvailable = true;
	}
	
	public void setPos(int x, int y){
		setX(x);
		setY(y);
		try
		{
			grid = ImageIO.read(new File("media\\images\\lawn.jpg")); 
		}
		catch(Exception e)
		{
			System.out.println("Curse your sudden but inevitable betrayal(Can't find grid image)");
			//feel free to do something here
		}
	}
	
	public void setX(int x){
		xPos = x;
	}
	
	public void setY(int y){
		yPos = y;
	}
	
	public void setAvail(boolean change){
		isAvailable = change;
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}
	
	public boolean getAvail(){
		return isAvailable;
	}
	
	public void draw(Graphics window) {
		window.drawImage(grid,getX(),getY(),80,80,null);
	}
	
	public String toString(){
		System.out.println("xpos: " + xPos + " ypos: " + yPos);
		return getX() + " " + getY();
	}
	
}
