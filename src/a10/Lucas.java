package a10;

import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Lucas extends Plant {
	
	private static final int HEALTH = 10;
	private static final int COOLDOWN = 10;
	private static final int ATTACKDMG = 5;
	private static final BufferedImage IMAGE;
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

	public Lucas(Double startingPosition, Double initHitbox, BufferedImage img, int health, int coolDown,
			int attackDamage) {
		super(startingPosition, initHitbox, img, health, coolDown, attackDamage);
	}

	@Override
	public void attack(Actor other) {
		
	}

}
