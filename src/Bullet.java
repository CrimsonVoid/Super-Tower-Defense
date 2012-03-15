import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;

import javax.imageio.ImageIO;

public class Bullet {
	
	private int xPos;
	private int yPos;
	private int power;
	private Image bullet;
	private Rectangle rect;
	
	public Bullet(int x, int y, int pow){
		set(x, y, pow);
		rect = new Rectangle(x, y, 28, 24);
		try
		{
			bullet = ImageIO.read(new File("media\\images\\bullet.png"));
		}
		catch(Exception e)
		{
			System.out.println("Curse your sudden but inevitable betrayal(Can't find bullet image)");
		}
	}
	
	public void set(int x, int y, int pow){
		setX(x);
		setY(y);
		setPow(pow);
	}
	
	public void setX(int x){
		xPos = x;
	}
	
	public void setY(int y){
		yPos = y;
	}
	
	public void setPow(int pow){
		power = pow;
	}
	
	public void setRect(){
		rect = new Rectangle(getX(), getY(), 80, 80);
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}
	
	public int getPow(){
		return power;
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
	public void action() {
		setX(getX() + 10);
		setRect();
	}
	
	public void draw(Graphics window) {
		window.drawImage(bullet,getX(),getY(),28,24,null);
	}
}
