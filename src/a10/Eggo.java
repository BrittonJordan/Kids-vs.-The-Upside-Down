package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Eggo extends Zombie {

	// Constant Variables for Eggo Zombie
	private static final int HEALTH = 50;
	private static final int COOLDOWN = 999;
	private static final int SPEED = -2;
	private static final int ATTACKDMG = 35;
	private static final BufferedImage IMAGE;
	// Loads image for Eggo Zombie
	static {
		BufferedImage localImage = null;
		try {
			localImage = ImageIO.read(new File("src/a10/Icons/eggo.png"));

		} catch (IOException e) {
			System.out.println("Eggo image was not found");
			System.exit(0);
		}
		IMAGE = localImage;
	}

	// Eggo Constructor
	public Eggo(Double startingPosition) {
		super(startingPosition, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN, SPEED,
				ATTACKDMG);
	}

	// Eggo only attacks plants upon collision
	@Override
	public void attack(Actor other) {
		if (other instanceof Plant && this != other && this.isCollidingOther(other)) {
			other.changeHealth(-(other.getHealth())); // Completely wipes out all health of the plant it hits upon collision
			this.changeHealth(-(this.getHealth()));  // Wipes out all health of eggo itself
		}
	}
}
