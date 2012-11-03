import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MenuFrame {
	// MenuImageAttrib used to set xPos, yPos, and image of the tower menu image
	private class MenuImageAttrib {
		private int xPos, yPos;
		private String imageString;
		private BufferedImage menuImage;
		
		public MenuImageAttrib(int xP, int yP, String img) {
			setX(xP);
			setY(yP);
			setImageString(img);
			
			try {
				menuImage = ImageIO.read(new File("media//images//menu//" + imageString + ".png"));
			} catch (IOException e) {
				System.out.printf("Unable to find %s in MenuFrame\n", imageString);
			}
		}
		
		public String toString() {
			return String.format("Image: %s [%d, %d]\n", imageString, xPos, yPos);
		}
		
		//* Setters and Getters*//
		
		public void setX(int xP) {
			xPos = xP;
		}
		
		public void setY(int yP) {
			yPos = yP;
		}
		
		public void setImageString(String img) {
			imageString = img;
		}
		
		public void setImage(String newImage) {
			// Sets the image as the selected version image, doesn't modify imageString for reset and to get the selection
			
			try {
				menuImage = ImageIO.read(new File("media//images//menu//" + newImage + ".png"));
			} catch (IOException e) {
				System.out.printf("Unable to find %s in MenuFrame\n", newImage);
			}
		}
		
		public int getX() {
			return xPos;
		}
		
		public int getY() {
			return yPos;
		}
		
		public String getImageString() {
			return imageString;
		}
		
		public void resetImage() {
			setImage(imageString);
		}
		
		public BufferedImage getImage() {
			return menuImage;
		}
	}
	
	private int width, height;
	private BufferedImage menuStart, menuEnd, menuSeperator;
	private MenuImageAttrib menuA[];
	
	public MenuFrame(String imgArray[]) {
		menuA = new MenuImageAttrib[imgArray.length];
		
		try {
			menuStart = ImageIO.read(new File("media//images//menu//menu-start.png"));
			menuEnd = ImageIO.read(new File("media//images//menu//menu-end.png"));
			menuSeperator = ImageIO.read(new File("media//images//menu//menu-seperator.png"));
		} catch (IOException e) {
			System.out.printf("Unable to find menu image in MenuFrame\n");
		}
		
		int i = 0;
		for(String s : imgArray)
			// Adds a MenuImageAttrib(width of 'menuStart' + width of 'menuSeperator' * number of separators placed + x padding to offset 'menuSeperator', y padding to offset 'menuSeperator', image name) object to menuA
			menuA[i] = new MenuImageAttrib(85 + menuStart.getWidth() + (i++) * menuSeperator.getWidth() + 10, 6, s);
		
		width = menuStart.getWidth() + (menuSeperator.getWidth() * menuA.length) + menuEnd.getWidth();
		height = menuStart.getHeight();
	}
	
	public String getSelection(int xPos, int yPos) {
		for(MenuImageAttrib mA : menuA)
			// if the xPos and yPos are within the bounds of mA
			if(xPos >= mA.getX() && xPos <= mA.getX()+mA.getImage().getWidth() && yPos >= mA.getY() && yPos <= mA.getY()+mA.getImage().getHeight()) {
				resetMenuImages();
				mA.setImage(mA.getImageString() + "-selected");
				
				return mA.getImageString();
			}
		
		return "";
	}
	
	public void resetMenuImages() {
		for(MenuImageAttrib mA : menuA)
			mA.resetImage();
	}
	
	public void draw(Graphics window) {
		// Draws a composite image of 'menuStart', images in 'menuA' contained within 'menuSeperator', and 'menuEnd' 
		
		int xDraw = 85;
		
		window.drawImage(menuStart, xDraw, 0, null);
		xDraw += menuStart.getWidth();
		
		for(MenuImageAttrib mA : menuA) {
			window.drawImage(menuSeperator, xDraw, 0, null);
			window.drawImage(mA.getImage(), mA.getX(), mA.getY(), null);
			xDraw += menuSeperator.getWidth();
		}
		
		window.drawImage(menuEnd, xDraw, 0, null);
	}
	
	public String toString() {
		return String.format("Menu frame with %d images\n", menuA.length);
	}
	
	//* Getter *//
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
