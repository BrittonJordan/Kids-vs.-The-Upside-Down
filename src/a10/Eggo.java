package a10;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Eggo extends Zombie {

	private static final int HEALTH = 50;
	private static final int COOLDOWN = 999;
	private static final int SPEED = -1;
	private static final int ATTACKDMG = 35;
	private static final BufferedImage IMAGE;
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
	

	public Eggo(Double startingPosition) {
		super(startingPosition, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN, SPEED, ATTACKDMG);
		
	}
	
	@Override
	public void attack(Actor other) {
		if (other instanceof Plant && this != other && this.isCollidingOther(other)) {
				other.changeHealth(-(other.getHealth()));
				this.changeHealth(-(this.getHealth()));
		}
	}

}