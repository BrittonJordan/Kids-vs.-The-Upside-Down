package a10;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Kids Vs. The Upside Down
 * 
 * A Plants Vs. Zombies type game based off the popular Netflix Series, Stranger
 * Things.
 * 
 * CS1410, Assignment 10, Spring 2020
 * 
 * @author David Johnson (Starter Code)
 * @author Taylor Dunn
 * @author Britton Jordan
 * 
 *         Stranger Things is property of Netflix. Background Image Source:
 *         https://vignette.wikia.nocookie.net/strangerthings8338/images/f/f1
 *         /The_Flea_and_the_Acrobat_-_the_Monster_feeds.png/revision/latest?cb=20170409124503
 *         Pixel Art and Background Overlay Created By: Taylor Dunn
 * 
 */
public class Example extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private ArrayList<Actor> actors; // Plants and zombies all go in here
	private int cellSize;
	private int counter;
	private int numCols;
	private int numRows;
	private static int platinumPoints; // Game currency
	private JLabel pointsLabel;
	private Random rand;
	private static JButton dustin;
	private static JButton eleven;
	private static JButton lucas;
	private static JButton mike;
	private static JButton will;
	private String nextPlant; // Used to store next plant to be placed on playing field
	private BufferedImage background;

	/**
	 * Setup the basic info for the application
	 */
	public Example() {
		super();

		// Sets Dimensions of the application
		numRows = 6;
		numCols = 7;
		cellSize = 50;
		setPreferredSize(new Dimension(250 + numCols * cellSize, numRows * cellSize));

		// Store all the plants and zombies in here.
		actors = new ArrayList<>();

		// Used to randomize the placement of zombies
		rand = new Random();

		// The timer updates the game each time it goes.
		timer = new Timer(30, this);
		timer.start();

		// Makes a label for the game's resource, platinum
		pointsLabel = new JLabel("Platinum : 0");
		this.add(pointsLabel);
		platinumPoints = 0;
		counter = 0;

		// Makes a button for each plant
		eleven = new JButton("Eleven: 50");
		eleven.addActionListener(this);

		mike = new JButton("Mike: 25");
		mike.addActionListener(this);

		will = new JButton("Will: 15");
		will.addActionListener(this);

		lucas = new JButton("Lucas: 10");
		lucas.addActionListener(this);

		dustin = new JButton("Dustin: 5");
		dustin.addActionListener(this);

		// Load the background image from file
		try {
			background = ImageIO.read(new File("src/a10/Icons/background.png"));
		} catch (IOException e) {
			System.out.println("Background image not found");
			e.printStackTrace();
		}
		// Adds a Mouse Listener to the application
		addMouseListener(this);
	}

	/**
	 * Allows outside access to the platinum points resource
	 * 
	 * @param change The amount by which platinumPoints will change
	 */
	public static void changePlatinumPoints(int change) {
		platinumPoints += change;
	}

	/***
	 * Implement the paint method to draw the plants
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, 600, 300, this);
		for (Actor actor : actors) {
			actor.draw(g, 0);
			actor.drawHealthBar(g);
		}
	}

	/**
	 * 
	 * This is triggered by the timer. It is the game loop of this application
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Increment their cooldowns and reset collision status
		for (Actor actor : actors) {
			actor.update();
		}

		// Try to attack
		for (Actor actor : actors) {
			for (Actor other : actors) {
				actor.attack(other);
			}
		}

		// Remove plants and zombies with low health
		ArrayList<Actor> nextTurnActors = new ArrayList<>();
		for (Actor actor : actors) {
			if (actor.isAlive())
				nextTurnActors.add(actor);
			else
				actor.removeAction(actors); // any special effect on removal
		}
		actors = nextTurnActors;

		// Check for collisions between zombies and plants and set collision status
		for (Actor actor : actors) {
			for (Actor other : actors) {
				actor.setCollisionStatus(other);
			}
		}

		// Move the actors.
		for (Actor actor : actors) {
			actor.move(); // for Zombie, only moves if not colliding.
		}

		// Create demogorgons at random rows
		if (rand.nextInt(1000) > 995) {
			int row = rand.nextInt(5) + 1;
			int y = row * 50;
			Zombie zombie = new Demogorgon(new Point2D.Double(500, y));
			actors.add(zombie);
		}

		// More demogorgons generated after about 90 seconds
		if (counter > 3000) {
			if (rand.nextInt(1000) > 997) {
				int row = rand.nextInt(5) + 1;
				int y = row * 50;
				Zombie zombie = new Demogorgon(new Point2D.Double(500, y));
				actors.add(zombie);
			}
		}

		// Create eggos at random rows
		counter++;
		if (counter > 1000) { // wait for about 30 seconds to start creating eggos
			if (rand.nextInt(1000) > 995) {
				int row = rand.nextInt(5) + 1;
				int y = row * 50;
				Zombie zombie = new Eggo(new Point2D.Double(500, y));
				actors.add(zombie);
			}
		}

		// Increment resource
		if (counter % 20 == 0) {
			platinumPoints += 2;
			pointsLabel.setText("Platinum : " + platinumPoints);
		}

		// Check for button clicks & store plant to place on playing field
		if (e.getSource() == eleven && platinumPoints >= 50) {
			nextPlant = "eleven";
		} else if (e.getSource() == eleven) {
			System.out.println("CODE RED: Not enough platinum!");// Alerts player of insufficient funds
		}
		if (e.getSource() == mike && platinumPoints >= 25) {
			nextPlant = "mike";
		} else if (e.getSource() == mike) {
			System.out.println("CODE RED: Not enough platinum!");
		}
		if (e.getSource() == will && platinumPoints >= 15) {
			nextPlant = "will";
		} else if (e.getSource() == will) {
			System.out.println("CODE RED: Not enough platinum!");
		}
		if (e.getSource() == lucas && platinumPoints >= 10) {
			nextPlant = "lucas";
		} else if (e.getSource() == lucas) {
			System.out.println("CODE RED: Not enough platinum!");
		}
		if (e.getSource() == dustin && platinumPoints >= 5) {
			nextPlant = "dustin";
		} else if (e.getSource() == dustin) {
			System.out.println("CODE RED: Not enough platinum!");
		}

		// Alerts player of score achieved at the end of the game
		for (Actor zombie : actors) {
			if (zombie.getPosition().getX() < 0) {
				System.out.println("You survived the Upside Down for " + counter / 33 + " seconds!");
				System.exit(0);
			}
		}
		// Redraw the new scene
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Gets the x and y coordinate of the mouse click
		int x = e.getX();
		int y = e.getY();
		// Changes coordinate to a specific integer % 50
		int row = (y / 50) * 50;
		int col = (x / 50) * 50;
		// Finds the specific grid location for plant placement
		Point2D.Double position = new Point2D.Double(col, row);
		// Checks that no plant is already located in the cell
		for (Actor actor : actors) {
			if (position.equals(actor.getPosition())) {
				return;
			}
		}
		// If nothing is stored in nextPlant, no plant will try to be placed
		if (nextPlant == null) {
			return;
		}

		if ((col <= 300) && (row > 0 && row <= 250)) { // Restrict the playing field
			// Checks which plant is stored and places in set grid position
			if (nextPlant.equals("eleven")) {
				Plant elevenPlant = new Eleven(position);
				actors.add(elevenPlant);
				platinumPoints -= 50;
				pointsLabel.setText("Platinum : " + platinumPoints);
			}
			if (nextPlant.equals("mike")) {
				Plant mikePlant = new Mike(position);
				actors.add(mikePlant);
				platinumPoints -= 25;
				pointsLabel.setText("Platinum : " + platinumPoints);
			}
			if (nextPlant.equals("will")) {
				Plant willPlant = new Will(position);
				actors.add(willPlant);
				platinumPoints -= 15;
				pointsLabel.setText("Platinum : " + platinumPoints);
			}
			if (nextPlant.equals("lucas")) {
				Plant lucasPlant = new Lucas(position);
				actors.add(lucasPlant);
				platinumPoints -= 10;
				pointsLabel.setText("Platinum : " + platinumPoints);
			}
			if (nextPlant.equals("dustin")) {
				Plant dustinPlant = new Dustin(position);
				actors.add(dustinPlant);
				platinumPoints -= 5;
				pointsLabel.setText("Platinum : " + platinumPoints);
			}
			// Removes stored plant after placement
			nextPlant = null;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Make the game and run it
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame app = new JFrame("Plant and Zombie Test");
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Example panel = new Example();
				app.setContentPane(panel);
				// Add Buttons to Panel
				app.add(eleven);
				app.add(mike);
				app.add(will);
				app.add(lucas);
				app.add(dustin);
				app.pack();
				app.setVisible(true);
			}
		});
	}
}