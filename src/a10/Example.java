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

public class Example extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private ArrayList<Actor> actors; // Plants and zombies all go in here
	BufferedImage plantImage; // Maybe these images should be in those classes, but easy to change here.
	BufferedImage zombieImage;
	int numRows;
	int numCols;
	int cellSize;
	Random rand;
	
	// WE ARE IN BUSINESS //
	//Checking, again//

	/**
	 * Setup the basic info for the example
	 */
	public Example() {
		super();

		// Define some quantities of the scene
		numRows = 5;
		numCols = 7;
		cellSize = 75;
		setPreferredSize(new Dimension(50 + numCols * cellSize, 50 + numRows * cellSize));

		// Store all the plants and zombies in here.
		actors = new ArrayList<>();

		// Load images
		try {
			plantImage = ImageIO.read(new File("src/a10/Icons/will.png"));
			zombieImage = ImageIO.read(new File("src/a10/Icons/eleven.png"));
		} catch (IOException e) {
			System.out.println("A file was not found");
			System.exit(0);
		}

		// Make a plant
		Plant plant = new Plant(new Point2D.Double(200, 200),
				new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), plantImage, 100, 5, 1);
		// Make a zombie
		Zombie zombie = new Zombie(new Point2D.Double(500, 200),
				new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), zombieImage, 100, 50, -2, 10);

		// Add them to the list of actors
		actors.add(plant);
		actors.add(zombie);
		rand = new Random();

		// The timer updates the game each time it goes.
		// Get the javax.swing Timer, not from util.
		timer = new Timer(30, this);
		timer.start();

	}

	/***
	 * Implement the paint method to draw the plants
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Actor actor : actors) {
			actor.draw(g, 0);
			actor.drawHealthBar(g);
		}
	}

	/**
	 * 
	 * This is triggered by the timer. It is the game loop of this test.
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// This method is getting a little long, but it is mostly loop code
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
				actor.removeAction(actors); // any special effect or whatever on removal
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
		
		// Create zombies at random rows
		
		if (rand.nextInt(100) > 97) {
			int row = rand.nextInt(6);
			int y = row * 50;
			
			Zombie zombie = new Zombie(new Point2D.Double(500, y), new Point2D.Double(zombieImage.getWidth(), zombieImage.getHeight()), zombieImage, 100, 50, -2, 10);
			actors.add(zombie);
		}
//		Random hi = new Random();
//		if (hi.nextInt(100) > 97)
//			System.out.println("Yep");
//		Zombie zombie = new Zombie(new Point2D.Double(500, 200), new Point2D.Double(zombieImage.getWidth(), zombieImage.getHeight()), zombieImage, 100, 50, -2, 10);
//		actors.add(zombie);
		

		// Redraw the new scene
		repaint();
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
				app.pack();
				app.setVisible(true);
			}
		});
	}

}