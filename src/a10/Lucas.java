package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import a10.Zombie;

public class Lucas extends Plant {

	// Constant Variables for Lucas Plant
	private static final int HEALTH = 30;
	private static final int COOLDOWN = 30;
	private static final int ATTACKDMG = 5;
	private static final BufferedImage IMAGE;
	// Loads image for Lucas Plant
	static {
		BufferedImage localImage = null;
		try {
			localImage = ImageIO.read(new File("src/a10/Icons/lucas.png"));

		} catch (IOException e) {
			System.out.println("Lucas' image was not found");
			System.exit(0);
		}
		IMAGE = localImage;
	}

	// Lucas Constructor
	public Lucas(Double startingPosition) {
		super(startingPosition, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN,
				ATTACKDMG);
	}

	// Lucas only attacks zombies upon collision
	@Override
	public void attack(Actor other) {
		if (other instanceof Zombie && this != other && this.isCollidingOther(other) && this.readyForAction()) {
			other.changeHealth(-ATTACKDMG);
			this.resetCoolDown();
		}
	}
}
