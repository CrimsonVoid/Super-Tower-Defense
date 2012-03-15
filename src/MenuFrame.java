import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class MenuFrame {
	private BufferedImage compositeMenu;
	
	public MenuFrame() {
		
	}
	
	public void draw(Graphics window) {
		window.drawImage(compositeMenu, 0, 0, null);
	}
	
	public String toString() {
		return "";
	}
}
