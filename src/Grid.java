import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Grid{
	// GridAttrib used to set xPos, yPos, and the availability of the grid
	private class GridAttrib {
		private int xPos, yPos;
		private boolean isAvailable;	//if a tower can be placed on the grid location
		
		public GridAttrib(int xP, int yP) {
			setX(xP);
			setY(yP);
			isAvailable = true;
		}
		
		public String toString() {
			return String.format("Available: %b [%d, %d]\n", isAvailable, xPos, yPos);
		}
		
		//* Setters and Getters *//
		
		public void setX(int xP) {
			xPos = xP;
		}
		
		public void setY(int yP) {
			yPos = yP;
		}
		
		public void setAvail(boolean change) {
			isAvailable = change;
		}

		public int getX() {
			return xPos;
		}
		
		public int getY() {
			return yPos;
		}
		
		public boolean getAvail() {
			return isAvailable;
		}
	}
	
	public static int gridRow = 9, gridColumn = 6;
	private GridAttrib gridA[][];
	private BufferedImage gridImg;	// All grid locations use the same image, no need to create multiple images. Just change the x and y position
	
	public Grid() {
		gridA = new GridAttrib[gridRow][gridColumn];
		createGrid();
		
		try {
			gridImg = ImageIO.read(new File("media//images//lawn.jpg"));
		} catch (IOException e) {
			System.out.printf("Unable to find lawn image in Grid\n");
		}
	}
	
	public void createGrid() {
		int xP = 60, yP = 90;
		
		for(GridAttrib gRow[] : gridA) {
			for(int y = 0; y < gRow.length; ++y) {
				gRow[y] = new GridAttrib(xP, yP);
				yP += 105;
			}
			xP += 105;
			yP = 90;
		}
	}
	
	public boolean isAvailable(int xP, int yP) {
		return isAvailable(xP, yP, new int[2]);
	}
	
	public boolean isAvailable(int xP, int yP, int imgPos[]) {
		
		int imgW = gridImg.getWidth(), imgH = gridImg.getHeight();
		
		int startInd = 0, endInd = gridColumn;
		int mid = (startInd + endInd) / 2;
		
		while ( !(yP >= gridA[1][mid].getY() && yP <= gridA[1][mid].getY()+imgH) ) {
			if (startInd >= endInd)
				return false;
			
			if (yP < gridA[1][mid].getY())
				endInd = mid - 1;
			else if (yP > gridA[1][mid].getY()+imgH)
				startInd = mid + 1;
			
			mid = (startInd + endInd) / 2;
		}
		
		int yCol = mid;
		startInd = 0;
		endInd = gridRow;
		mid = (startInd + endInd) / 2;
		
		while ( !(xP >= gridA[mid][yCol].getX() && xP <= gridA[mid][yCol].getX()+imgW) ) {
			if (startInd >= endInd)
				return false;
			
			if (xP < gridA[mid][yCol].getX())
				endInd = mid;
			else if (xP > gridA[mid][yCol].getX()+imgW)
				startInd = mid + 1;
			
			mid = (startInd + endInd) / 2;
		}
		
		imgPos[0] = gridA[mid][yCol].getX();
		imgPos[1] = gridA[mid][yCol].getY();
		
		return gridA[mid][yCol].getAvail();
	}
		
	public void setAvail(int xP, int yP, boolean change) {
		int imgW = gridImg.getWidth(), imgH = gridImg.getHeight();
		
		for(GridAttrib gRow[] : gridA)
			for(GridAttrib gA : gRow)
				if(xP >= gA.getX() && xP <= gA.getX()+imgW && yP >= gA.getY() && yP <= gA.getY()+imgH) {
					gA.setAvail(change);
					return;
				}
	}
	
	public void resetAll() {
		for(GridAttrib gRow[] : gridA)
			for(GridAttrib gA : gRow)
				gA.setAvail(true);
	}
	
	public void draw(Graphics window) {
		for(GridAttrib gRow[] : gridA)
			for(GridAttrib gA : gRow)
				window.drawImage(gridImg, gA.getX(), gA.getY(), null);
	}

	public String toString() {
		String s = "";
		
		for(GridAttrib gRow[] : gridA) {
			for(GridAttrib gA : gRow)
				s += String.format("[%d, %d] ", gA.getY(), gA.getX());
			
			s += '\n';
		}
		
		return s;
	}
}
