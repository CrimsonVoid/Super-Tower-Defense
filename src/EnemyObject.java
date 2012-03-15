import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class EnemyObject {
	private int xPos;
	private int yPos;
	private int speed;
	private int hp;
	private int power;
	private String type;
	private Image enmy;
	private Rectangle rect;
	
	public EnemyObject(int x, int y, int health, int pow, int spd, String s){
		setPos(x, y, health, pow, spd, s);
		rect = new Rectangle(x, y, 80, 80);
		try
		{
			enmy = Toolkit.getDefaultToolkit().getImage("media\\images\\enemies\\" + s + ".jpg"); 
		}
		catch(Exception e)
		{
			System.out.println("Curse your sudden but inevitable betrayal(Can't find enemy image)");
		}
	}
	
	public void setPos(int x, int y, int health, int pow, int spd, String s){
		setX(x);
		setY(y);
		setHP(health);
		setPow(pow);
		setSpeed(spd);
		setEnemyType(s);
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
	
	public void setSpeed(int spd){
		speed = spd;
	}
	
	public void setEnemyType(String s){
		type = s;
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
	
	public int getHP(){
		return hp;
	}
	
	public int getPow(){
		return power;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public String getTowerType(){
		return type;
	}
	
	public Rectangle getRect(){
		return rect;
	}	
	
	public void action(){
		//System.out.println("action outside loop");
		if(getX() != 0){
			//System.out.println("in loop before setX");
			setX(getX()-getSpeed());
			//System.out.println("after setX");
		}
		//System.out.println("oustide loop");
		setRect();
	}
	
	public void draw(Graphics window){
		window.drawImage(enmy, getX(), getY(), 80, 80, null);
	}
	
	
	public String toString(){
		/*System.out.println("xpos: " + xPos + " ypos: " + yPos + " hp: " + hp +
			" power: " + power + " speed: " + speed);*/
		return getX() + " " + getY() + " " + getHP() + " " + getPow();
	}

}
