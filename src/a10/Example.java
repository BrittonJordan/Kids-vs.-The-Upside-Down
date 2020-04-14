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

public class Example extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private ArrayList<Actor> actors; // Plants and zombies all go in here
	int numRows;
	int numCols;
	int cellSize;
	Random rand;
	int platinumPoints;
	JLabel pointsLabel;
	int counter;
	static JButton eleven;
	static JButton mike;
	static JButton will;
	static JButton lucas;
	static JButton dustin;
	private String nextPlant;

	/**
	 * Setup the basic info for the example
	 */
	public Example() {
		super();

		// Define some quantities of the scene
		numRows = 6;
		numCols = 7;
		cellSize = 45;
		setPreferredSize(new Dimension(200 + numCols * cellSize, 50 + numRows * cellSize));

		// Store all the plants and zombies in here.
		actors = new ArrayList<>();
		
		rand = new Random();

		// The timer updates the game each time it goes.
		// Get the javax.swing Timer, not from util.
		timer = new Timer(30, this);
		timer.start();

		pointsLabel = new JLabel("Platinum : 0");
		this.add(pointsLabel);
		platinumPoints = 0;
		counter = 0;

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

		addMouseListener(this);

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

		if (rand.nextInt(100) > 98) {
			int row = rand.nextInt(5) + 1;
			int y = row * 45;

			Zombie zombie = new Demogorgon(new Point2D.Double(500, y));
			actors.add(zombie);
		}
//		
		counter++;
		if (counter > 20) {
			platinumPoints += 10;
			pointsLabel.setText("Platinum : " + platinumPoints);
			counter = 0;
		}

		if (e.getSource() == eleven && platinumPoints >= 50) { // one for each kid
			nextPlant = "eleven";
		}
		if (e.getSource() == mike && platinumPoints >= 25) { // one for each kid
			nextPlant = "mike";
		}
		if (e.getSource() == will && platinumPoints >= 15) { // one for each kid
			nextPlant = "will";
		}
		if (e.getSource() == lucas && platinumPoints >= 10) { // one for each kid
			nextPlant = "lucas";
		}
		if (e.getSource() == dustin && platinumPoints >= 5) { // one for each kid
			nextPlant = "dustin";
		}
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

	@Override
	public void mouseClicked(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		int row = (y / 45) * 45;
		int col = (x / 45) * 45;
		Point2D.Double position = new Point2D.Double(col, row);
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
		nextPlant = "";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}