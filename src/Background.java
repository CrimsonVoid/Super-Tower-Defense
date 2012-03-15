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
	
	MP3 music = new MP3("media\\music\\background.mp3");
	MP3 achv = new MP3("media\\music\\achievement.mp3");
	Font font;
	
	Image bg;
	Image menu;
	Image lost;
	Image ach;
	
	int money;
	long startTower = System.currentTimeMillis();
	long startMunny = System.currentTimeMillis();
	long lostTime = System.currentTimeMillis();
	
	boolean lostB = false;
	boolean achPlay = false;
	
	String temp = "";
	String nrml = "nrml";
	String fast = "fast";
	String strong = "strong";
	
	private Image dbImage;
	private Graphics dbg;
	
	ArrayList<Grid> gridz = new ArrayList<Grid>();
	ArrayList<MunnyTower> munny = new ArrayList<MunnyTower>();
	ArrayList<StandardTower> standard = new ArrayList<StandardTower>();
	ArrayList<HeavyTower> heavy = new ArrayList<HeavyTower>();
	ArrayList<PowerTower> power = new ArrayList<PowerTower>();
	ArrayList<EnemyObject> enemy = new ArrayList<EnemyObject>();
	ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	
	Iterator<Grid> itrGrid;
	Iterator<MunnyTower> itrMny;
	Iterator<StandardTower> itrStd;
	Iterator<HeavyTower> itrHvy;
	Iterator<PowerTower> itrPwr;
	Iterator<EnemyObject> itrEnmy;
	Iterator<Bullet> itrBlt;
	
	
	public Background (){
		this.addMouseListener(this);
		this.setSize(1024, 795);
		new Thread(this).start();
		
		enemy.add(new EnemyObject(800, 85, 1, 10, 1, nrml));
		
		money = 5150;
		
		try {
			bg = ImageIO.read(new File("media\\images\\bg.jpg"));
			menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
		} catch (Exception e) {
			System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find bg/menu image)");
		}
		
		font = new Font("Neuropol", Font.PLAIN,  12);
		
		createGrid(gridz);
		music.play();
		setVisible(true);
	}
	
	public void paint(Graphics window){
		//Anti-Alias font. Thanks to thenewboston @ http://www.youtube.com/user/thenewboston
		if(window instanceof Graphics2D){
			//System.out.println("G-2D");
			Graphics2D g2 = (Graphics2D)window;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		window.drawImage(bg, 0, 0, 1024, 768, null);
		window.drawImage(menu, 85, 0, 653, 80, null);
		
		if(System.currentTimeMillis() - startTower >= 5000){
			System.out.println("One second");
			startTower = System.currentTimeMillis();			
		}
		
		//use for displaying money
		window.setFont(font);
		window.drawString("Munny: " + money, 125, 71);
		
		fire();
		move();
		collisionDetectionTEnA();
		collisionDetectionB();
		drawObjects(window);
		lostYet();
		
		//sleep for debuging / "double buffering"
		/*try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			System.out.println("Can't sleep...huh?!");
		}*/
		
		if(lost != null){
			window.drawImage(lost, 0, 0, 1033, 800, null);
			achvUnlocked();
		}
		if(ach != null){
			window.drawImage(ach, 350, 680, null);
			if(achPlay == false){
				achv.play();
				achPlay = true;
			}
		}
		repaint();
	}

	public void run() {
		
	}
	
	public void update(Graphics g){
		//special thanks to http://javaboutique.internet.com/tutorials/Java_Game_Programming/BildschirmflackernEng.html
		//yippie for DoubleBuffering!
		// initialize buffer
		if (dbImage == null)
		{
		dbImage = createImage (this.getSize().width, this.getSize().height);
		dbg = dbImage.getGraphics ();
		}

		// clear screen in background
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		// draw elements in background
		dbg.setColor (getForeground());
		paint (dbg);

		// draw image on the screen
		g.drawImage (dbImage, 0, 0, this);
		
		
	}
	
	public void createGrid(ArrayList<Grid> list){
		System.out.print("Creating Grid...");
		int xPos = 85;
		int yPos = 85;
		for(int y = 0; yPos < 700; y++){
			
			for(int x = 0; xPos <= 1000; x++){
				//System.out.println("xpos: "+xPos+" ypos: "+yPos);
				list.add(new Grid(xPos, yPos));
				xPos += 85;
			}
			//System.out.println("xpos: "+xPos+" ypos: "+yPos);
			xPos = 85;
			yPos += 85;
		}
		System.out.println("Done!");
	}
	
	public void collisionDetectionTEnA(){
		if(!enemy.isEmpty()){
			
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
							if(enemy.get(eny).getTowerType().equals(nrml)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getTowerType().equals(fast)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getTowerType().equals(strong)){
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
							if(enemy.get(eny).getTowerType().equals(nrml)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getTowerType().equals(fast)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getTowerType().equals(strong)){
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
							if(enemy.get(eny).getTowerType().equals(nrml)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getTowerType().equals(fast)){
								enemy.get(eny).setSpeed(3);
							}
							if(enemy.get(eny).getTowerType().equals(strong)){
								enemy.get(eny).setSpeed(3);
							}
						}
					}
				}
			}
		}
	}
	
	public void collisionDetectionB(){
		if(!bullet.isEmpty()){
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
		}
	}
	
	public void move(){
		itrEnmy = enemy.iterator();
		while(itrEnmy.hasNext()){
			itrEnmy.next().action();
		}
		
		itrBlt = bullet.iterator();
		while(itrBlt.hasNext()){
			itrBlt.next().action();
		}
	}
	
	public void drawObjects(Graphics window){
		itrGrid = gridz.iterator();
		while(itrGrid.hasNext()){
			itrGrid.next().draw(window);
		}
		
		itrMny = munny.iterator();
		while(itrMny.hasNext()){
			itrMny.next().draw(window);
		}
		
		itrStd = standard.iterator();
		while(itrStd.hasNext()){
			itrStd.next().draw(window);
		}
		
		itrPwr = power.iterator();
		while(itrPwr.hasNext()){
			itrPwr.next().draw(window);
		}
		
		itrHvy = heavy.iterator();
		while(itrHvy.hasNext()){
			itrHvy.next().draw(window);
		}
		
		itrEnmy = enemy.iterator();
		while(itrEnmy.hasNext()){
			itrEnmy.next().draw(window);
		}
		
		itrBlt = bullet.iterator();
		while(itrBlt.hasNext()){
			itrBlt.next().draw(window);
		}
	}
	
	public void fire(){
		if(System.currentTimeMillis() - startTower >= 1000){
			if(!standard.isEmpty()){
				for(int x = 0; x < standard.size(); x++){
					bullet.add(new Bullet(standard.get(x).getX()+80, standard.get(x).getY()+40, standard.get(x).getPow()));
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
		}
	}
	
	public void lostYet(){
		itrEnmy = enemy.iterator();
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
		}
	}
	
	public void achvUnlocked(){
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
	public void mouseClicked(MouseEvent e) {
		int x = 0;
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
		
		while(!( (e.getX() > gridz.get(x).getX() && e.getX() < gridz.get(x).getX()+80) &&
				(e.getY() > gridz.get(x).getY() && e.getY() < gridz.get(x).getY()+80) ) && x<87){
			
			x++;
			//System.out.println("Grid: " + x + ". X = " + gridz.get(x).getX() + ". Y = " + gridz.get(x).getY());
		}
		
		if((e.getX() > gridz.get(x).getX() && e.getX() < gridz.get(x).getX()+80) && 
			(e.getY() > gridz.get(x).getY() && e.getY() < gridz.get(x).getY()+80)){
			
			System.out.println("Grid: " + x + ". X = " + gridz.get(x).getX() + ". Y = " + gridz.get(x).getY());
			
			if(!gridz.get(x).getAvail() && !temp.equals("")){
				System.out.println("A tower is already there!");
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu image, when creating hvytwr)");
				}
			}
			if(temp.equals("mny") && gridz.get(x).getAvail() && money >= 50){
				gridz.get(x).setAvail(false);
				munny.add(new MunnyTower(gridz.get(x).getX(), gridz.get(x).getY(), 50, 0));
				money -= 50;
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu image, when creating mnytwr)");
				}
				System.out.println("Munny Tower Created!");
			}
			if(temp.equals("std") && gridz.get(x).getAvail() && money >= 100){
				gridz.get(x).setAvail(false);
				standard.add(new StandardTower(gridz.get(x).getX(), gridz.get(x).getY(), 100, 10));
				money -= 100;
				try {
					menu = ImageIO.read(new File("media\\images\\menu\\menu.jpg"));
				} catch (IOException e1) {
					System.out.println("Everything's shiny, Cap'n. Not to fret!(Can't find menu image, when creating stdtwr)");
				}
				System.out.println("Standard Tower Created!");
			}
			if(temp.equals("pwr") && gridz.get(x).getAvail() && money >= 150){
				gridz.get(x).setAvail(false);
				power.add(new PowerTower(gridz.get(x).getX(), gridz.get(x).getY(), 150, 18));
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
				heavy.add(new HeavyTower(gridz.get(x).getX(), gridz.get(x).getY(), 200, 13));
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
		}
		
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
