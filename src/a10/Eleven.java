package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Eleven extends Plant{

	private static final int HEALTH = 100;
	private static final int COOLDOWN = 60;
	private static final int ATTACKDMG = 100;
	private static final BufferedImage IMAGE;
	static {
		BufferedImage localImage = null;
		try {
			localImage = ImageIO.read(new File("src/a10/Icons/eleven.png"));

		} catch (IOException e) {
			System.out.println("Eleven image was not found");
			System.exit(0);
		}
		IMAGE = localImage;
	}
	
	public Eleven(Double startingPosition) {
		super(startingPosition, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN, ATTACKDMG);
		
	}
	
	@Override
	public void attack(Actor other) {
		if (other instanceof Zombie && this != other && this.isCollidingOther(other)) {
			if(this.readyForAction()) {
				other.changeHealth(-ATTACKDMG);
				this.resetCoolDown();
			}
			
		}
	}

}
