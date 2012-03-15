import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class Background extends Canvas implements MouseListener, Runnable{
	private TowerAttributes towerAtr[] = {new TowerAttributes(50, 25, 2500, "moogle"),	//health, money to add, time interval
											new TowerAttributes(100, 10, 1000, "std"),
											new TowerAttributes(200, 13, 1000, "hvy"),
											new TowerAttributes(150, 18, 1000, "pwr")};
	
	private TowerAttributes enemyAtr[] = {new TowerAttributes(1, 10, 1, 1000, "nrml"),
											new TowerAttributes(1, 10, 1,  1000, "fast"),
											new TowerAttributes(1, 10, 1,  1000, "strong")};

	//private ArrayList<Grid> gridList = new ArrayList<Grid>();
	private Grid gridPlane = new Grid();
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	private ArrayList<TowerObject> towerList = new ArrayList<TowerObject>();
	private ArrayList<EnemyObject> enemyList = new ArrayList<EnemyObject>();

	/*private Iterator<Grid> itrGrid;
	private Iterator<Bullet> itrBlt;
	private Iterator<TowerObject> itrTower;
	private Iterator<EnemyObject> itrEnmy;*/
	
	private int money;
	private long startMunny;
	private long lostTime;
	
	private boolean lostB;
	private boolean achPlay;
	
	private String temp;
	
	private MP3 music;
	private MP3 achv;
	private Font font;
	
	private Image bg;
	private Image menu;
	private Image lost;
	private Image ach;
	private Image dbImage;
	private Graphics dbg;
	
	public Background(int width, int height) {
		this.addMouseListener(this);
		this.setSize(width, height);
		
		money = 500;
		lostB = achPlay = false;
		temp = "";
		startMunny = lostTime = System.currentTimeMillis();
		
		gridPlane = new Grid();
		bulletList = new ArrayList<Bullet>();
		towerList = new ArrayList<TowerObject>();
		enemyList = new ArrayList<EnemyObject>();
		
		music = new MP3("media\\music\\background.mp3");
		achv = new MP3("media\\music\\achievement.mp3");
		
		font = new Font("Neuropol", Font.PLAIN,  12);
		
		try {
			bg = ImageIO.read(new File("media\\images\\bg.jpg"));
			menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
		} catch (Exception e) {
			System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find bg/menu image)");
		}
		
		enemyList.add(new EnemyObject(800, 85, enemyAtr[0]));
		
		music.play();
		//What does this do?
		new Thread(this).start();
	}
	
	public void paint(Graphics window) {
		System.out.println("Calling paint");
		//Anti-Alias font. Thanks to thenewboston @ http://www.youtube.com/user/thenewboston
		if(window instanceof Graphics2D){
			Graphics2D g2 = (Graphics2D)window;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		
		window.drawImage(bg, 0, 0, 1024, 768, null);
		window.drawImage(menu, 85, 0, 653, 80, null);
				
		//use for displaying money
		window.setFont(font);
		window.drawString("Munny: " + money, 125, 71);
		
		fire();
		move();
		collisionDetectionTEnA();
		collisionDetectionB();
		drawObjects(window);
		lostYet();
				
		repaint();
	}
	
	public void update(Graphics g) {
		//special thanks to http://javaboutique.internet.com/tutorials/Java_Game_Programming/BildschirmflackernEng.html
		//yippie for DoubleBuffering!
		// initialize buffer
		System.out.println("Calling update");
		if (dbImage == null){
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}

		// clear screen in background
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

		// draw elements in background
		dbg.setColor (getForeground());
		paint(dbg);

		// draw image on the screen
		g.drawImage(dbImage, 0, 0, this);
	}
	
	public void run() {
		System.out.printf("%s\n", "What is this doing here?");
	}
	
	//fixed
	/*public void createGrid(ArrayList<Grid> list) {
		System.out.print("Creating Grid...");
		
		for(int yPos = 110; yPos <= 700; yPos += 110)
			for(int xPos = 110; xPos < 1000; xPos += 110)
				list.add(new Grid(xPos, yPos));
		
		System.out.println("Done!");
	}*/
	
	//fixed
	public void collisionDetectionTEnA() {
		for(TowerObject t : towerList)
			for(EnemyObject e : enemyList) {
				if(t.getRect().intersects(e.getRect())) {
					e.setSpeed(0);
					t.setHP(t.getHP() - e.attack());
					
					if(t.getHP() <= 0) {
						towerList.remove(t);
						e.resetSpeed();
					}
				}
				
				e.resetSpeed();
			}
		
		/*if(!enemy.isEmpty()){
			
			//Check Standard Tower collision
			if(!standard.isEmpty()){
				for(int twr = 0; twr < standard.size(); twr++){
					for(int eny = 0; eny < enemy.size(); eny++){
						if(standard.get(twr).getRect().intersects(enemy.get(eny).getRect())){
							enemy.get(eny).setSpeed(0);
							standard.get(twr).setHP(standard.get(twr).getHP() - enemy.get(eny).getPow());
							if(standard.get(twr).getHP() <= 0){
								for(int x = 0; x < gridz.size(); x++){
									if((standard.get(twr).getX() == gridz.get(x).getX()) &&(standard.get(twr).getY() == gridz.get(x).getY())){
										gridz.get(x).setAvail(true);
									}
								}
								standard.remove(twr);
							}
						}
						else{
							if(enemy.get(eny).getType().equals(nrml)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getType().equals(fast)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getType().equals(strong)){
								enemy.get(eny).setSpeed(3);
							}
						}
					}
				}
			}
			
			//Check Heavy Tower collision
			if(!heavy.isEmpty()){
				for(int twr = 0; twr < heavy.size(); twr++){
					for(int eny = 0; eny < enemy.size(); eny++){
						if(heavy.get(twr).getRect().intersects(enemy.get(eny).getRect())){
							enemy.get(eny).setSpeed(0);
							heavy.get(twr).setHP(heavy.get(twr).getHP() - enemy.get(eny).getPow());
							if(heavy.get(twr).getHP() <= 0){
								for(int x = 0; x < gridz.size(); x++){
									if((heavy.get(twr).getX() == gridz.get(x).getX()) &&(heavy.get(twr).getY() == gridz.get(x).getY())){
										gridz.get(x).setAvail(true);
									}
								}
								heavy.remove(twr);
							}
						}
						else{
							if(enemy.get(eny).getType().equals(nrml)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getType().equals(fast)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getType().equals(strong)){
								enemy.get(eny).setSpeed(3);
							}
						}
					}
				}
			}
			
			//Check Power Tower collision
			if(!power.isEmpty()){
				for(int twr = 0; twr < power.size(); twr++){
					for(int eny = 0; eny < enemy.size(); eny++){
						if(power.get(twr).getRect().intersects(enemy.get(eny).getRect())){
							enemy.get(eny).setSpeed(0);
							power.get(twr).setHP(power.get(twr).getHP() - enemy.get(eny).getPow());
							if(power.get(twr).getHP() <= 0){
								for(int x = 0; x < gridz.size(); x++){
									if((power.get(twr).getX() == gridz.get(x).getX()) &&(power.get(twr).getY() == gridz.get(x).getY())){
										gridz.get(x).setAvail(true);
									}
								}
								power.remove(twr);
							}
						}
						else{
							if(enemy.get(eny).getType().equals(nrml)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getType().equals(fast)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getType().equals(strong)){
								enemy.get(eny).setSpeed(3);
							}
						}
					}
				}
			}
		}*/
	}
	
	//fixed
	public void collisionDetectionB() {
		for(Bullet b : bulletList)
			for(EnemyObject e : enemyList)
				if(b.getRect().intersects(e.getRect())) {
					e.setHP(e.getHP() - b.getPow());
					
					if(e.getHP() <= 0)
						enemyList.remove(e);
					bulletList.remove(b);
				}
		
		/*if(!bullet.isEmpty()){
			if(!enemy.isEmpty()){
				for(int x = 0; x < bullet.size(); x++){
					for(int eny = 0; eny < enemy.size(); eny++){
						if(bullet.get(x).getRect().intersects(enemy.get(eny).getRect())){
							enemy.get(eny).setHP(enemy.get(eny).getHP() - bullet.get(x).getPow());
							if(enemy.get(eny).getHP() <= 0){
								enemy.remove(eny);
							}
						}
					}
				}
			}
		}*/
	}
	
	//fixed
	public void move() {
		for(EnemyObject o : enemyList)
			o.action();
		for(Bullet o : bulletList)
			o.action();
		
		/*itrEnmy = enemy.iterator();
		while(itrEnmy.hasNext()){
			itrEnmy.next().action();
		}
		
		itrBlt = bullet.iterator();
		while(itrBlt.hasNext()){
			itrBlt.next().action();
		}*/
	}
	
	//fixed
	public void drawObjects(Graphics window) {
		gridPlane.draw(window);
		
		for(EnemyObject o : enemyList)
			o.draw(window);
		
		for(TowerObject o : towerList)
			o.draw(window);
		
		for(Bullet o : bulletList)
			o.draw(window);
		
		/*itrGrid = gridz.iterator();
		while(itrGrid.hasNext()){
			itrGrid.next().draw(window);
		}
		
		itrEnmy = enemy.iterator();
		while(itrEnmy.hasNext()){
			itrEnmy.next().draw(window);
		}
		
		itrBlt = bullet.iterator();
		while(itrBlt.hasNext()){
			itrBlt.next().draw(window);
		}*/
	}
	
	//fixed
	public void fire() {
		for(TowerObject o : towerList) {
			int newObj = o.fire();
			
			if(newObj > 0) {
				if(o.getType().equals(towerAtr[0].getType()) && newObj > 0)
					money += newObj;
				else
					bulletList.add(new Bullet(o.getX()+(o.getWidth()/2), o.getY()+(o.getHeight()/2), newObj));
			}
		
		}
		
		//add money at random intervals from 8-15 sec
		
		/*if(System.currentTimeMillis() - startTower >= 1000){
			if(!standard.isEmpty()){
				for(int x = 0; x < standard.size(); x++){
					bulletList.add(new Bullet(standard.get(x).getX()+80, standard.get(x).getY()+40, standard.get(x).getPow()));
				}
			}
			if(!heavy.isEmpty()){
				for(int x = 0; x < heavy.size(); x++){
					bullet.add(new Bullet(heavy.get(x).getX()+80, heavy.get(x).getY()+40, heavy.get(x).getPow()));
				}
			}
			if(!power.isEmpty()){
				for(int x = 0; x < power.size(); x++){
					bullet.add(new Bullet(power.get(x).getX()+80, power.get(x).getY()+40, power.get(x).getPow()));
				}
			}
			startTower = System.currentTimeMillis();
		}
		
		if(System.currentTimeMillis() - startMunny >= 8000){
			if(!munny.isEmpty()){
				for(int x = 0; x < munny.size(); x++){
					money += 25;
				}
			}
			startMunny = System.currentTimeMillis();
		}*/
	}

	//fixed
	public void lostYet() {
		for(EnemyObject e : enemyList)
			if(e.getX() <= 0) {
				try {
					lost = ImageIO.read(new File("media\\images\\GO\\lost.png"));
				} catch(IOException ex) {
					System.out.println("Curse your sudden but inevitable betrayal(Can't find lost image)");
				}
				
				if(lostB == false){
					lostB = true;
					lostTime = System.currentTimeMillis();
				}
				
				return;
			}
		
		/*itrEnmy = enemy.iterator();
		while(itrEnmy.hasNext()){
			if(itrEnmy.next().getX() <= 0){
				try {
					lost = ImageIO.read(new File("media\\images\\\\GO\\lost.jpg"));
				} catch (IOException e) {
					System.out.println("Curse your sudden but inevitable betrayal(Can't find lost image)");
				}
				if(lostB == false){
					lostB = true;
					lostTime = System.currentTimeMillis();
				}
			}
		}*/
	}
	
	public void achvUnlocked() {
		if(System.currentTimeMillis() - lostTime >= 3000){
			try
			{
				ach = Toolkit.getDefaultToolkit().getImage("media\\images\\GO\\ach.png"); 
			}
			catch(Exception e)
			{
				System.out.println("Curse your sudden but inevitable betrayal(Can't find ach image)");
			}
		}
	}

	@Override
	//remove instances of string temp;
	public void mouseClicked(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		System.out.println(gridPlane.getAvail(x, y));
		
		/*int x = e.getX(), y = e.getY();
		System.out.println("X: " + e.getX() + " Y: " + e.getY());
		
		if(e.getY() <= 84){
			if(e.getX() > 257 && e.getX() < 352 && e.getY() > 8 && e.getY() < 71){
				temp = "mny";
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu-mny.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu-mny image)");
				}
				System.out.println(temp);
			}
			if(e.getX() > 376 && e.getX() < 471 && e.getY() > 8 && e.getY() < 71){
				temp = "std";
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu-std.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu-std image)");
				}
				System.out.println(temp);
			}
			if(e.getX() > 492 && e.getX() < 586 && e.getY() > 8 && e.getY() < 71){
				temp = "pwr";
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu-pwr.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu-pwr image)");
				}
				System.out.println(temp);
			}
			if(e.getX() > 612 && e.getX() < 704 && e.getY() > 8 && e.getY() < 71){
				temp = "hvy";
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu-hvy.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu-hvy image)");
				}
				System.out.println(temp);
			}
		}
		
		while(!( (e.getX() > gridList.get(x).getX() && e.getX() < gridList.get(x).getX()+80) &&
				(e.getY() > gridList.get(x).getY() && e.getY() < gridList.get(x).getY()+80) ) && x<87){
			//I have no idea what the fuck this does...
			x++;
			//System.out.println("Grid: " + x + ". X = " + gridz.get(x).getX() + ". Y = " + gridz.get(x).getY());
		}
		
		if((e.getX() > gridList.get(x).getX() && e.getX() < gridList.get(x).getX()+80) && 
			(e.getY() > gridList.get(x).getY() && e.getY() < gridList.get(x).getY()+80)){
			
			System.out.println("Grid: " + x + ". X = " + gridList.get(x).getX() + ". Y = " + gridList.get(x).getY());
			
			if(!gridList.get(x).getAvail() && !temp.equals("")){
				System.out.println("A tower is already there!");
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu image, when creating hvytwr)");
				}
			}
			if(temp.equals("mny") && gridList.get(x).getAvail() && money >= 50){
				gridList.get(x).setAvail(false);
				munny.add(new MunnyTower(gridList.get(x).getX(), gridList.get(x).getY(), 50, 0, "std"));
				money -= 50;
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu image, when creating mnytwr)");
				}
				System.out.println("Munny Tower Created!");
			}
			if(temp.equals("std") && gridList.get(x).getAvail() && money >= 100){
				gridList.get(x).setAvail(false);
				standard.add(new StandardTower(gridList.get(x).getX(), gridList.get(x).getY(), 100, 10, "std"));
				money -= 100;
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu image, when creating stdtwr)");
				}
				System.out.println("Standard Tower Created!");
			}
			if(temp.equals("pwr") && gridList.get(x).getAvail() && money >= 150){
				gridList.get(x).setAvail(false);
				power.add(new PowerTower(gridList.get(x).getX(), gridList.get(x).getY(), 150, 18, "std"));
				money -= 150;
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu image, when creating pwrtwr)");
				}
				System.out.println("Power Tower Created!");
			}
			if(temp.equals("hvy") && gridz.get(x).getAvail() && money >= 125){
				gridz.get(x).setAvail(false);
				heavy.add(new HeavyTower(gridz.get(x).getX(), gridz.get(x).getY(), 200, 13, "std"));
				money -= 125;
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu image, when creating hvytwr)");
				}
				System.out.println("Heavy Tower Created!");
			}
			
			else if(temp.equals("")){
				System.out.println("No tower selected!");
			}
			temp = "";
		}
		else if(temp.equals("")){
			System.out.println("Click inside the freaking grid!");
		}*/
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
