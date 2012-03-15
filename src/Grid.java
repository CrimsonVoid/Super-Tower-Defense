import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Grid{
	private class GridAttrib {
		private int xPos, yPos;
		private boolean isAvailable;
		
		public GridAttrib(int xP, int yP) {
			xPos = xP;
			yPos = yP;
			isAvailable = true;
		}
		
		//* Setter and Getter *//
		
		public void setX(int xP) {
			xPos = xP;
		}
		
		public int getX() {
			return xPos;
		}
		
		public void setY(int yP) {
			yPos = yP;
		}
		
		public int getY() {
			return yPos;
		}
		
		public void setAvail(boolean change) {
			isAvailable = change;
		}
		
		public boolean getAvail() {
			return isAvailable;
		}
	}
	
	private GridAttrib gridA[];
	private BufferedImage gridImg;
	
	public Grid() {
		gridA = new GridAttrib[54];
		createGrid();
		
		try {
			gridImg = ImageIO.read(new File("media\\images\\lawn.jpg"));
		} catch (IOException e) {
			System.out.printf("%s\n", "Curse your sudden but inevitable betrayal(Can't find grid image)");
			e.printStackTrace();
		}
	}
	
	public void createGrid() {
		int i = 0;
		
		for(int yP = 110; yP <= 700; yP += 110)
			for(int xP = 110; xP < 1000; xP += 110)
				gridA[i++] = new GridAttrib(xP, yP);
	}

	public void draw(Graphics window) {
		for(GridAttrib gA : gridA)
			window.drawImage(gridImg, gA.getX(), gA.getY(), null);
	}
	
	public String toString() {
		String s = "";
		int oldY = gridA[0].getY();
		
		for(GridAttrib gA : gridA) {
			if(oldY != gA.getY()) {
				oldY = gA.getY();
				s += '\n';
			}
			s += String.format("[%d, %d]", gA.getY(), gA.getX());
		}
		s += '\n';
		
		return s;
	}

	public boolean getAvail(int xP, int yP) {
		int imgW = gridImg.getWidth(), imgH = gridImg.getHeight();
		
		for(GridAttrib gA : gridA)
			if( xP >= gA.getX() && xP <= gA.getX()+imgW && yP >= gA.getY() && yP <= gA.getY()+imgH )
				return gA.getAvail();
		
		return false;
	}
	
	//* Setter and Getter *//
	
	/*public void setX(int x) {
		xPos = x;
	}
	
	public void setY(int y) {
		yPos = y;
	}
	
	public void setAvail(boolean change) {
		isAvailable = change;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}*/
}
