import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class TowerObject {
	private int xPos;
	private int yPos;
	private int hp;
	private int power;
	private Rectangle rect;
	
	public TowerObject(int x, int y, int health, int pow){
		setPos(x,y,health,pow);
		rect = new Rectangle(x, y, 80, 80);
	}
	
	public void setPos(int x, int y, int health, int pow){
		setX(x);
		setY(y);
		setHP(health);
		setPow(pow);
	}
	
	public void setX(int x){
		xPos = x;
	}
	
	public void setY(int y){
		yPos = y;
	}
	
	public void setHP(int health){
		hp = health;
	}
	
	public void setPow(int pow){
		power = pow;
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}
	
	public int getHP(){
		return hp;
	}
	
	public int getPow(){
		return power;
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
	public abstract void draw(Graphics window);
	
	public String toString(){
		//System.out.println("xpos: " + xPos + " ypos: " + yPos + " hp: " + hp + " power: " + power);
		return getX() + " " + getY() + " " + getHP() + " " + getPow();
	}

}
