package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import a10.Zombie;

public class Demogorgon extends Zombie {

	// Constant Variables for Demogorgon Zombie
	private static final int HEALTH = 50;
	private static final int COOLDOWN = 30;
	private static final int SPEED = -1;
	private static final int ATTACKDMG = 10;
	private static final BufferedImage IMAGE;
	// Loads image for Demogorgon Zombie
	static {
		BufferedImage localImage = null;
		try {
			localImage = ImageIO.read(new File("src/a10/Icons/demogorgon.png"));

		} catch (IOException e) {
			System.out.println("Demogoron image was not found");
			System.exit(0);
		}
		IMAGE = localImage;
	}

	// Demogorgon Constructor
	public Demogorgon(Double startingPosition) {
		super(startingPosition, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN, SPEED,
				ATTACKDMG);
	}

	// Demogorgon only attacks plants upon collision
	@Override
	public void attack(Actor other) {
		if (other instanceof Plant && this != other && this.isCollidingOther(other)) {
			if (this.readyForAction()) {
				other.changeHealth(-ATTACKDMG);
				this.resetCoolDown();
			}
		}
	}
}
