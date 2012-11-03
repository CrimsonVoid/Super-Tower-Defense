import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Background extends Canvas implements MouseListener, Runnable{
	private static final long serialVersionUID = 9066447519364005185L;
	
	public static int width, height;
	// time range in milliseconds to add money or spawn enemies
	private int minMoneySpawnInterval = 7000, maxMoneySpawnInterval = 15000, minEnemySpawnInterval = 10000, maxEnemySpawnInterval = 15000;
	// set to System.currentTimeMillis() for generating money and spawning enemies
	private long moneyGen, enemyGen;
	private int money, towerCost, selIndex, moneySpawnInterval, enemySpawnInterval;
	
	private Image bg;
	private Image dbImage;
	private Graphics dbg;
	
	private MP3 music;
	
	private Grid gridPlane;
	private MenuFrame menu;
	private FinalStand fStand[];
	private TowerAttributes towerAttr[], enemyAttr[];
	private ArrayList<Bullet> bulletList;
	private ArrayList<TowerObject> towerList;
	private ArrayList<EnemyObject> enemyList;
	private Iterator<Bullet> iB;
	private Iterator<TowerObject> iT;
	private Iterator<EnemyObject> iE;
	
	public Background(int width, int height) {
		this.addMouseListener(this);
		this.setSize(width, height);
		Background.width = width;
		Background.height = height;
		
		readCSV();
		
		money = 500;
		selIndex = towerCost = -1;
		moneySpawnInterval = new Random().nextInt(maxMoneySpawnInterval - minMoneySpawnInterval) + (maxMoneySpawnInterval - minMoneySpawnInterval);
		enemySpawnInterval = new Random().nextInt(maxEnemySpawnInterval - minEnemySpawnInterval) + (maxEnemySpawnInterval - minEnemySpawnInterval);
		moneyGen = enemyGen = System.currentTimeMillis();
		
		gridPlane = new Grid();
		menu = new MenuFrame(towerNames());
		fStand = new FinalStand[Grid.gridColumn];
		bulletList = new ArrayList<Bullet>();
		towerList = new ArrayList<TowerObject>();
		enemyList = new ArrayList<EnemyObject>();
		
		for(int i = 0; i < fStand.length; ++i) {
			fStand[i] = new FinalStand(0, 90 + 105*i);
		}
		
		music = new MP3("media//music//background.mp3");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		try {
			bg = ImageIO.read(new File("media//images//bg.jpg"));
		} catch (Exception e) {
			System.out.printf("Unable to find bg image in Background\n");
		}
				
		music.play();
		new Thread(this).start();
	}
	
	public void paint(Graphics window) {
		// Sets anti-aliasing if JRE supports it
		if(window instanceof Graphics2D){
			Graphics2D g2 = (Graphics2D)window;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		
		action();
		collisionDetection();
		
		window.drawImage(bg, 0, 0, width, height, null);
		menu.draw(window);
		window.drawString(String.format("Munny: %d", money), 125, 70);
		drawObjects(window);
		
		repaint();
	}
	
	public void update(Graphics window) {
		// Initialize buffer
		if (dbImage == null) {
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}
		
		// Clear screen in background
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
		
		// Draw elements in background
		dbg.setColor(getForeground());
		paint(dbg);
		
		// Draw image on the screen
		window.drawImage(dbImage, 0, 0, this);
	}

	public void run() {
		// TODO - find use for threading; perhaps to spawn enemies and generate money
	}

	private void action() {
		// Generate money on set interval between minMoneySpawnInterval and maxMoneySpawnInterval milliseconds
		if(System.currentTimeMillis() - moneyGen >= moneySpawnInterval) {
			money += 50;
			moneySpawnInterval = new Random().nextInt(maxMoneySpawnInterval - minMoneySpawnInterval) + (maxMoneySpawnInterval - minMoneySpawnInterval);
			moneyGen = System.currentTimeMillis();
		}
		
		// Spawn enemies on set intervals between minEnemySpawnInterval and maxEnemySpawnInterval milliseconds
		if(System.currentTimeMillis() - enemyGen >= enemySpawnInterval) {
			enemyList.add(new EnemyObject(width, 90 + new Random().nextInt(6) * 105, enemyAttr[new Random().nextInt(enemyAttr.length)]));
			enemySpawnInterval = new Random().nextInt(maxEnemySpawnInterval - minEnemySpawnInterval) + (maxEnemySpawnInterval - minEnemySpawnInterval);
			enemyGen = System.currentTimeMillis();
		}
		
		// Fires a bullet if the towers' cool down has ended 
		for(TowerObject t : towerList) {
			// TowerObject.action() returns the power of the TowerObject if it can attack (cool down), or 0 if it can't
			int power = t.action();
			
			if(power > 0) {
				// if the attacking tower generates money, it adds 'power' to 'money'
				if(t.getType().equals(towerAttr[0].getType()))
					money += power;
				
				// Otherwise it adds a bullet on (the right edge of 't', centered on the Y-axis, power and speed of 't', and the bullet image to be used)  
				else
					bulletList.add(new Bullet(t.getX()+t.getWidth(), t.getY()+(t.getHeight()/2), power, t.getSpeed(), t.getType()));
			}
		}
		
		// Iterate through each item in fStand, enemyList, and bulletList calling action() and removing the object when necessary
		
		for(FinalStand fS : fStand)
			fS.action();
		
		iE = enemyList.iterator();
		EnemyObject e;
		while(iE.hasNext()) {
			e = iE.next();
			e.action();
			// Checks whether the enemy has reached the left edge of the map
			if(e.getX() <= 0) {
				gameOver();
				return;
			}
		}
		
		iB = bulletList.iterator();
		Bullet b;
		while(iB.hasNext()) {
			b = iB.next();
			b.action();
			// Checks and removes the bullet if it is off screen
			if(b.getX() >= Background.width)
				iB.remove();
		}
	}
	
	private void collisionDetection() {
		TowerObject t;
		EnemyObject e;
		Bullet b;
		iE = enemyList.iterator();
		
		// Checks collision of enemyList against fStand, bulletList and towerList
		while(iE.hasNext()) {
			iT = towerList.iterator();
			iB = bulletList.iterator();
			e = iE.next();
			
			for(FinalStand fS : fStand)
				if(fS.getRect().intersects(e.getRect())) {
					fS.setActivated(true);
					iE.remove();
				}
			
			while(iB.hasNext()) {
				b = iB.next();
				
				if(b.getRect().intersects(e.getRect())) {
					e.setHP(e.getHP() - b.getPower());
					
					if(e.getHP() <= 0)
						iE.remove();
					
					iB.remove();
				}
			}
			
			while(iT.hasNext()) {
				t = iT.next();
				
				if(e.getRect().intersects(t.getRect())) {
					t.setHP(t.getHP() - e.action());
					
					if(t.getHP() <= 0) {
						gridPlane.setAvail(t.getX(), t.getY(), true);
						iT.remove();
					}
				}
			}
		}
		
		// Checks for collision between enemyList and towerList and sets speed accordingly
		boolean collision;
		iE = enemyList.iterator();
		while(iE.hasNext()){
			collision = false;
			e = iE.next();
			e.resetSpeed();
			
			iT = towerList.iterator();
			while(iT.hasNext() && !collision) {
				t = iT.next();
				
				if(e.getRect().intersects(t.getRect())) {
					e.setSpeed(0);
					collision = true;
				}
			}
		}
	}

	private void drawObjects(Graphics window) {
		gridPlane.draw(window);
		
		for(FinalStand fS : fStand)
			fS.draw(window);
		
		for(EnemyObject e : enemyList)
			e.draw(window);
		
		for(TowerObject t : towerList)
			t.draw(window);
		
		for(Bullet b : bulletList)
			b.draw(window);
	}
	
	private synchronized void gameOver() {
		// Yes: 0	No: 1	Close: -1
		if(JOptionPane.showConfirmDialog(null, "Would you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION) == 0)
			reset();
		else {
			JOptionPane.showMessageDialog(null, "Thanks for playing!", "Game Over", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
	}
	
	private void reset() {
		music.close();
		money = 500;
		towerCost = -1;
		moneySpawnInterval = new Random().nextInt(maxMoneySpawnInterval - minMoneySpawnInterval) + (maxMoneySpawnInterval - minMoneySpawnInterval);
		enemySpawnInterval = new Random().nextInt(maxEnemySpawnInterval - minEnemySpawnInterval) + (maxEnemySpawnInterval - minEnemySpawnInterval);
		moneyGen = enemyGen = System.currentTimeMillis();
		
		gridPlane.resetAll();
		menu.resetMenuImages();
		bulletList.clear();
		towerList.clear();
		enemyList.clear();
		
		for(int i = 0; i < fStand.length; ++i) {
			fStand[i] = null;	// for garbage collection, because I don't know how Java's GC works
			fStand[i] = new FinalStand(0, 90 + 105*i);
		}
		
		System.gc();
		music.play();
	}
	
	private void readCSV() {
		BufferedReader csvFile;
		String line, attr[] = new String[6];
		int len;
		
		try {
			csvFile = new BufferedReader(new FileReader("media//Towers.csv"));
			
			try {
				towerAttr = new TowerAttributes[Integer.parseInt(csvFile.readLine())];
				len = 0;
				
				while((line = csvFile.readLine()) != null) {
					attr = line.split(",");
					towerAttr[len++] = new TowerAttributes(attr[0], Integer.parseInt(attr[1]), Integer.parseInt(attr[2]), Integer.parseInt(attr[3]), Integer.parseInt(attr[4]), Integer.parseInt(attr[5]));
				}
				
				csvFile.close();
				csvFile = new BufferedReader(new FileReader("media//Enemies.csv"));
				
				enemyAttr = new TowerAttributes[Integer.parseInt(csvFile.readLine())];
				len = 0;
				
				while((line = csvFile.readLine()) != null) {
					attr = line.split(",");
					enemyAttr[len++] = new TowerAttributes(attr[0], Integer.parseInt(attr[1]), Integer.parseInt(attr[2]), Integer.parseInt(attr[3]), Integer.parseInt(attr[4]));
				}
			} catch (IOException e) {
				System.out.printf("Read line error\n");
			}
		} catch (FileNotFoundException e) {
			System.out.printf("Cannot find towers.csv\n");
		}
	}
	
	private String[] towerNames() {
		String towerNames[] = new String[towerAttr.length];
		int len = 0;
		
		for(TowerAttributes tA : towerAttr)
			towerNames[len++] = tA.getType();
		
		return towerNames;
	}
	
	private void clickEvent(int x, int y) {
		
		// Checks if the mouse click was within the tower menu
		if(x >= 85 && x <= 85+menu.getWidth() && y <= menu.getHeight()) {
			
			String selImage = menu.getSelection(x, y);
			
			// Sets 'selIndex' to the matching index of towerAttr and 'towerCost' to the cost of the tower
			for(int i = 0; i < towerAttr.length; ++i)
				if(towerAttr[i].getType().equals(selImage)) {
					towerCost = towerAttr[i].getCost();
					selIndex = i;
					return;
				}
		}
		
		// Checks is mouse click was in the grid area, if there is a tower selected, and if you have enough money to buy the tower
		else if(x >= 60 && x <= Background.width && y >= 90 && y <= Background.height && money >= towerCost) {
			// if selected tower is remove tower
			if(selIndex == towerAttr.length-1) {
				gridPlane.setAvail(x, y, true);
				
				iT = towerList.iterator();
				TowerObject t;
				while(iT.hasNext()) {
					t = iT.next();
					
					if(x >= t.getX() && x <= t.getX() + t.getWidth() && y >= t.getY() && y <= t.getY() + t.getHeight()) {
						iT.remove();
						selIndex = -1;
						menu.resetMenuImages();
						break;
					}
				}
			}
			
			else if(selIndex != -1) {
				// imgPos is used to avoid redundant operations; it stores the xPos and yPos of the grid image {xPos, yPos}
				int imgPos[] = new int[2];
				
				if(gridPlane.isAvailable(x, y, imgPos)) {
					// Adds the selected tower to towerList and sets the grid location to false
					gridPlane.setAvail(x, y, false);
					towerList.add(new TowerObject(imgPos[0], imgPos[1], towerAttr[selIndex]));
					money -= towerCost;
					selIndex = -1;
				}
			}
			
			menu.resetMenuImages();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		clickEvent(e.getX(), e.getY());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		clickEvent(e.getX(), e.getY());
	}
	
	@Override
	public void mousePressed(MouseEvent e) { }
	
	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }
}
