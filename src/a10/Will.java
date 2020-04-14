package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Will extends Plant{

	private static final int HEALTH = 40;
	private static final int COOLDOWN = 15;
	private static final int ATTACKDMG = 5;
	private static final BufferedImage IMAGE;
	static {
		BufferedImage localImage = null;
		try {
			localImage = ImageIO.read(new File("src/a10/Icons/will.png"));

		} catch (IOException e) {
			System.out.println("Will image was not found");
			System.exit(0);
		}
		IMAGE = localImage;
	}
	
	public Will(Double startingPosition) {
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
