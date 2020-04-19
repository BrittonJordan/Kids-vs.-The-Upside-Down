package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Mike extends Plant {

	// Constant variables for Mike Plant
	private static final int HEALTH = 50;
	private static final int COOLDOWN = 10;
	private static final int ATTACKDMG = 5;
	private static final BufferedImage IMAGE;
	// Loads image for Mike Plant
	static {
		BufferedImage localImage = null;
		try {
			localImage = ImageIO.read(new File("src/a10/Icons/mike.png"));

		} catch (IOException e) {
			System.out.println("Mike image was not found");
			System.exit(0);
		}
		IMAGE = localImage;
	}

	// Mike Constructor
	public Mike(Double startingPosition) {
		super(startingPosition, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN,
				ATTACKDMG);
	}

	// Mike only attacks zombies upon collision
	@Override
	public void attack(Actor other) {
		if (other instanceof Zombie && this != other && this.isCollidingOther(other)) {
			if (this.readyForAction()) {
				other.changeHealth(-ATTACKDMG);
				this.resetCoolDown();
			}
		}
	}

	// Upon Mike dying, the player will receive 10 Platinum points
	@Override
	public void removeAction(ArrayList<Actor> others) {
		Example.changePlatinumPoints(10);

	}
}
