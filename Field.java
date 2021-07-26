import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Field implements KeyListener {
	final int screenLength = 800;
	final int screenWidth = 600;
	// animationTime used for slower animations to look more realistic
	int animationTime = 0;
	int ballTimer = 0;
	int score = 0;
	int ballsInPlay = 0; // gradually increases
	boolean gameOver = false;;
	ArrayList<Integer> eliminatedBalls = new ArrayList<Integer>();
	javax.swing.Timer t;
	ArrayList<Object> object = new ArrayList<Object>();
	ArrayList<Obstacle> obstacle = new ArrayList<Obstacle>();
	private draw contentPane;
	private JFrame graph;
	private Color[] colors = { new Color(255, 102, 102), new Color(255, 51, 51), new Color(255, 0, 0),
			new Color(204, 0, 0), new Color(153, 0, 0), new Color(51, 204, 255), new Color(51, 153, 255),
			new Color(0, 0, 255), new Color(0, 0, 204), new Color(0, 0, 153) };

	public static void main(String[] args) {
		Field pinballGame = new Field();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					pinballGame.makeWindow();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void createField() {
		// Creates Game Field Using Obstacle Superclass
		Obstacle line1 = new Line(0, 150, 200, 0);
		obstacle.add(line1);
		Obstacle line2 = new Line(400, 0, 600, 150);
		obstacle.add(line2);
		Obstacle line3 = new Line(50, 150, 150, 250);
		obstacle.add(line3);
		Obstacle line4 = new Line(450, 250, 550, 150);
		obstacle.add(line4);
		Obstacle line5 = new Line(150, 325, 280, 275);
		obstacle.add(line5);
		Obstacle line6 = new Line(320, 275, 450, 325);
		obstacle.add(line6);
		Obstacle line7 = new Line(16, 350, 150, 475);
		obstacle.add(line7);
		Obstacle line8 = new Line(450, 475, 584, 350);
		obstacle.add(line8);
		Obstacle line9 = new Line(200, 500, 200, 600);
		obstacle.add(line9);
		Obstacle line10 = new Line(400, 600, 400, 500);
		obstacle.add(line10);
		Obstacle line11 = new Line(0, 675, 178, 750);
		obstacle.add(line11);
		Obstacle line12 = new Line(422, 750, 600, 675);
		obstacle.add(line12);
		Obstacle bouncer1 = new Bouncer(275, 120, 25);
		obstacle.add(bouncer1);
		Obstacle bouncer2 = new Bouncer(240, 75, 10);
		obstacle.add(bouncer2);
		Obstacle bouncer3 = new Bouncer(340, 75, 10);
		obstacle.add(bouncer3);
		Obstacle bouncer4 = new Bouncer(290, 400, 10);
		obstacle.add(bouncer4);
		Obstacle bouncer5 = new Bouncer(60, 470, 20);
		obstacle.add(bouncer5);
		Obstacle bouncer6 = new Bouncer(540, 470, 20);
		obstacle.add(bouncer6);
		Obstacle bouncer7 = new Bouncer(280, 570, 20);
		obstacle.add(bouncer7);
		Obstacle playerLeft = new Player(180, 760, 290, 760, true);
		obstacle.add(playerLeft);
		Obstacle playerRight = new Player(310, 760, 420, 760, false);
		obstacle.add(playerRight);
		Obstacle eliminate = new EliminationLine();
		obstacle.add(eliminate);

	}

	public void makeWindow() throws InterruptedException {
		//design
		graph = new JFrame("Pinball");
		graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graph.setLocation(600, 0);
		contentPane = new draw();
		contentPane.setPreferredSize(new Dimension(screenWidth, screenLength));
		graph.addKeyListener(this);
		graph.add(contentPane);
		graph.setBackground(Color.black);
		graph.setVisible(true);
		graph.pack();
		//creates necessary objects
		createField();
		createBall();
		t = new javax.swing.Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.repaint();
				//graph.add(contentPane);
				graph.repaint();
				animationTime = animationTime + 1;
				ballTimer = ballTimer + 1;
				if (animationTime > 10) {
					score = (int) (score + (1 * Math.pow(3, ballsInPlay)));
					obstacle.get(obstacle.size() - 3).update();
					obstacle.get(obstacle.size() - 2).update();
					animationTime = 0;
				}
				if (ballTimer > 4000) {
					if (object.size() < 7) {
						createBall();
					}
					ballTimer = 0;
				}
				// iterates through each ball in play to check for potential collisions
				for (int i = 0; i < object.size(); i++) {
					object.get(i).clearInteract();
					//collisons with other balls
					for (int j = 0; j < object.get(i).xCoords().size(); j += 1) {
						for (int k = 0; k < object.get(i).yCoords().size(); k += 1) {
							for (int l = 0; l < object.size(); l++) {
								if (object.get(l) == object.get(i)) {
									//catch to not consider a ball as hitting itself
								} else if (object.get(l).inbounds(object.get(i).xCoords().get(j),
										object.get(i).yCoords().get(k)) && object.get(l).canInteract(i)) {
									//if two balls hit each other, calculates resulting trajectories of each ball and transfers momentum between the two
									float distanceX = ((float) object.get(l).centerX())
											- ((float) object.get(i).centerX());
									float distanceY = ((float) object.get(l).centerY())
											- ((float) object.get(i).centerY());
									float angle = (float) Math.atan(distanceY / distanceX);
									if (distanceX > 0) {
										angle += Math.PI;
									}
									object.get(l).interact(i);
									object.get(l).changeVelocityBall(angle, object.get(i).velocityX(),
											object.get(i).velocityY());
								}
							}

						}
					//obstacle collision
					}
					for (int j = 0; j < obstacle.size(); j++) {
						if (obstacle.get(j).type().equals("line")) {
							if (obstacle.get(j).interact((int) object.get(i).centerX(),
									(int) object.get(i).centerY())) {
								int[] definitions = obstacle.get(j).definitions();
								if (definitions[1] == definitions[3]) {
									// if horizontalLine then computation not necessary since yVelocity is just
									// being reversed
									object.get(i).horizontalLine();
								} else if (definitions[0] == definitions[2]) {
									// if verticalLine then computation not necessary since xVelocity is just
									// being reversed
									object.get(i).verticalLine();
								} else {
									// computes new trajectory
									float angleLine = (float) Math.atan((-(double) (definitions[1] - definitions[3]))
											/ ((double) (definitions[0] - definitions[2])));
									if (definitions[1] == definitions[3]) {
										angleLine += Math.PI;
									}
									float angleVelocity = (float) (Math
											.atan(-object.get(i).velocityY() / (object.get(i).velocityX() + 0.001)));

									if (object.get(i).velocityX() <= 0) {
										angleVelocity += Math.PI;
									}

									float angle = angleVelocity;
									float diff = Math.abs(angleLine - angleVelocity);
									if (angleLine > angleVelocity) {
										angle = (float) (angleLine - Math.PI + diff);
									} else if (angleLine < angleVelocity) {
										angle = (float) (angleLine + Math.PI - diff);
									}
									if (obstacle.get(j) instanceof Player) {
										if (Math.sin(angle) > 0) {
											angle = (float) (Math.PI - (angle - Math.PI));
										}
									}
									boolean succeed = object.get(i).changeVelocity(angle, false);
									if (!succeed) {
										if (angleLine > 0 && angleLine < Math.PI) {
											angleLine += Math.PI;
										}
										object.get(i).changeVelocity(angleLine, true);
									}
								}
								//Temporary disabling gravity to allow for cleaner animation
								object.get(i).tempDisableGravity();
								// a hidden line is last in arraylist at bottom of screen to calculate when the ball goes out if bounds
								if (j == obstacle.size() - 1) {
									object.get(i).out();
									if (!(eliminatedBalls.contains(i))) {
										ballsInPlay -= 1;
									}
									eliminatedBalls.add(i);
									if (ballsInPlay == 0)
										gameOver = true;
								}
								j = obstacle.size();
							}
						} else if (obstacle.get(j).type().equals("bouncer") && obstacle.get(j)
								.interact((int) object.get(i).centerX(), (int) object.get(i).centerY())) {
							int[] definitions = obstacle.get(j).definitions();
							// center of bouncer is xcoord + radius
							float centerX = definitions[0] + definitions[2];
							// center of bouncer is ycoord - radius
							float centerY = definitions[1] + definitions[2];
							float distanceX = ((float) object.get(i).centerX()) - ((float) centerX);
							float distanceY = ((float) object.get(i).centerY()) - ((float) centerY);
							float angle = (float) Math.atan(distanceY / distanceX);
							if (distanceX > 0) {
								angle += Math.PI;
							}
							object.get(i).changeVelocityBall(angle, object.get(i).velocityX() * 1.1,
									object.get(i).velocityY() * 1.1);
							j = obstacle.size();
							score += 500 * Math.pow(3, ballsInPlay);
						}
					}

				}

			}
		});
		t.start();
	}

	class draw extends JPanel {
		public void paintComponent(Graphics g) {
			if (!gameOver) {
				for (int i = 0; i < object.size(); i++) {
					g.setColor(object.get(i).getColor());
					if (!eliminatedBalls.contains(i))
						g.fillOval(object.get(i).x(), object.get(i).y(), object.get(i).width(), object.get(i).length());
					object.get(i).update();

				}
				g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
				g.drawString("Score is " + score, 20, 780);
				g.setColor(new Color(255, 255, 255));
				g.drawString("Ball Amount is " + ballsInPlay, 400, 780);
				for (int i = 0; i < obstacle.size(); i++) {
					if (i == obstacle.size() - 1) {
						g.setColor(Color.BLACK);
					}
					if (obstacle.get(i).type().equals("line")) {
						int[] coords = obstacle.get(i).definitions();
						for (int j = -1; j <= 1; j++) {
							g.drawLine(coords[0], coords[1] + j, coords[2], coords[3] + j);
						}
					} else if (obstacle.get(i).type().equals("bouncer")) {
						int[] coords = obstacle.get(i).definitions();
						g.fillOval(coords[0], coords[1], coords[2] * 2, coords[2] * 2);
					}

				}
			} else {
				//gameover screen
				g.setColor(Color.RED);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
				g.drawString("GameOver", 70, 350);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
				g.setColor(Color.white);
				g.drawString("Score is " + score, 140, 450);
				t.stop();
			}

		}
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == 39) {
			obstacle.get(obstacle.size() - 2).userAction();
			// Right arrow key code
		}

		else if (e.getKeyCode() == 37) {
			// Left arrow key code
			obstacle.get(obstacle.size() - 3).userAction();
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public void createBall() {
		ballsInPlay = ballsInPlay + 1;
		Random rand = new Random();
		object.add(new Object(300, 50, Math.random() - 0.5, 0, colors[rand.nextInt(9)]));
		for (int i = 0; i < object.size(); i++) {
			if (i != object.size() - 1) {
				object.get(object.size() - 1).addInteract(i);
			}
			object.get(i).addInteract(object.size() - 1);
		}
	}

}
