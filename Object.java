import java.awt.Color;
import java.util.ArrayList;

public class Object {
	//game ball 
	private double width = 16;
	private double length = 16;
	private double x;
	private double y;
	private Color color;
	private static double accelerationY = -0.00098 * 5;
	private double accelerationX = 0;
	private double velocityX;
	private double velocityY;
	private double oldVelocityX;
	private double oldVelocityY;
	private double mass = 2;
	private boolean gravityOn = true;
	private int gravityCounter = 0;
	private final int windowLength = 800;
	private final int windowWidth = 600;
	ArrayList<Integer> xCoords = new ArrayList<Integer>();
	ArrayList<Integer> yCoords = new ArrayList<Integer>();;
	ArrayList<Integer[]> coords = new ArrayList<Integer[]>();;
	// when the ball is out of play
	private boolean out;
	private ArrayList<Boolean> alreadyInteracted = new ArrayList<Boolean>();
	private ArrayList<Integer> interactTimer = new ArrayList<Integer>();
	// future variables to add wind and other forces to make game more difficult
	ArrayList<Force> forcesY = new ArrayList<Force>();
	ArrayList<Force> forcesXAir = new ArrayList<Force>();
	ArrayList<Force> forcesXGround = new ArrayList<Force>();

	public Object(double x, double y, double velocityX, double velocityY, Color color) {
		this.x = x - (width / 2); // calculates the origin of the point
		this.y = y - (length / 2); // calculates the origin of the point
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.color = color;
		Gravity g = new Gravity();
		forcesY.add(g);
		out = false;
		for (int i = 0; i < 10; i++) {
			xCoords.add(new Integer(0));
			yCoords.add(new Integer(0));
		}

	}

	public void updateCoord() {
		//adds points at the surface of the ball to help other objects detect collisions with this one
		//in the future, might incorporate using only the origin of the ball with the radius to run less computations 
		for (int i = 0; i < 10; i += 1) {
			xCoords.set(i, new Integer((int) (centerX() + (Math.cos((Math.PI / 5) * i) * width / 2))));
			yCoords.set(i, new Integer((int) (centerY() - (Math.sin((Math.PI / 5) * i) * length / 2))));
		}
	}

	public ArrayList<Integer> xCoords() {
		return xCoords;
	}

	public ArrayList<Integer> yCoords() {
		return yCoords;
	}

	// ran into an issue with gravity and its effects on the ball animating when hitting a line
	// therefore made method to temp disable gravity which lets the ball move
	// without gravity's effect
	public void tempDisableGravity() {
		gravityOn = false;
		gravityCounter = 20;

	}

	public void interact(int index) {
		alreadyInteracted.set(index, true);
		interactTimer.set(index, 40);
	}

	public void out() {
		out = true;
	}

	public void clearInteract() {
		for (int i = 0; i < interactTimer.size(); i++) {
			interactTimer.set(i, interactTimer.get(i) - 1);
			if (interactTimer.get(i) == 0 && alreadyInteracted.get(i)) {
				alreadyInteracted.set(i, false);
			}
		}
	}

	public boolean canInteract(int index) {
		return !(alreadyInteracted.get(index));
	}

	// public int interactTimer() {
	// return interactTimer;
	// }
	public void addInteract(int index) {
		alreadyInteracted.add(false);
		interactTimer.add(-1);
	}

	public void gravityUp() {
		accelerationY += 0.05;
	}

	public void gravityDown() {
		accelerationY -= 0.05;
	}

	public double centerX() {
		return x + (width / 2);
	}

	public double centerY() {
		return y + (length / 2);
	}

	public Color getColor() {
		return color;
	}

	public int width() {
		return (int) width;
	}

	public int length() {
		return (int) length;
	}

	public int x() {
		return (int) x;
	}

	public int y() {
		return (int) y;
	}

	public double velocityX() {
		return oldVelocityX;
	}

	public double velocityY() {
		return oldVelocityY;
	}

	public void horizontalLine() {
		velocityY = -velocityY;
	}

	public void verticalLine() {
		velocityX = -velocityX;
	}
	
	//changes velocity when collides with line
	public boolean changeVelocity(float angle, boolean minimiumVelocity) {
		double velocity;
		if (minimiumVelocity) {
			velocity = 0.5;
		} else {
			velocity = (Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
			if (velocity < 0.2) {
				return false;
			}
		}
		velocityY = -Math.sin((double) angle) * velocity;
		velocityX = -Math.cos((double) angle) * velocity;
		return true;

	}
	//changes velocity when collides with other ball
	public void changeVelocityBall(float angle, double collisonVelocityX, double collisonVelocityY) {
		double velocity = (Math.sqrt(Math.pow(collisonVelocityX, 2) + Math.pow(collisonVelocityY, 2)));
		velocityY = Math.sin(angle) * velocity;
		velocityX = -Math.cos(angle) * velocity;

	}

	private void acceleration() {
		double xSum = 0;
		accelerationX = 0;
		if ((y + velocityY > windowLength - length || y + velocityY < 0) && velocityX * 1000 != (int) 0) {
			for (int i = 0; i < forcesXGround.size(); i++) {
				if (velocityX < 0) {
					xSum += forcesXGround.get(i).returnForce();
				} else if (velocityX > 0)
					xSum -= forcesXGround.get(i).returnForce();
			}
			accelerationX = xSum / mass;
		}

	}

	private void moveX() {
		// If ball hits the outer bounds, sets new X position and reverse trajectory with less speed
		//else moves the ball
		if (x + velocityX > windowWidth - width) {
			x = windowWidth - width;
			velocityX /= -(1.5);
		} else if (x + velocityX < 0) {
			x = 0;
			velocityX /= -(1.5);
		} else {
			x += velocityX;
		}
	}


	public boolean inbounds(double x, double y) {
		//detects if another ball is inbounds by using distance from origin compared to radius
		boolean inbounds = false;
		float center_x;
		float center_y;
		double r = width / 2;
		center_x = (float) (this.x + r);
		center_y = (float) (this.y + r);
		float dist = (float) Math.sqrt(Math.pow(x - center_x, 2) + Math.pow(y - center_y, 2));
		if (dist < r)
			inbounds = true;
		return inbounds;
	}

	public boolean inboundsLine(double x, double y, double x1, double y2) {
		//detects if line is inbounds by using distance from origin compared to radius
		//Separate from inbounds method to allow for potential change (buffer, using different points of line)
		boolean inbounds = false;
		double r = width / 2;
		float center_x = (float) (this.x + r);
		float center_y = (float) (this.y + r);
		float dist = (float) Math.sqrt(Math.pow(x - center_x, 2) + Math.pow(y - center_y, 2));
		if (dist < r)
			inbounds = true;
		return inbounds;
	}

	private void moveY() {
		// If ball hits the outer bounds, sets new Y position and reverse trajectory with less speed
		//else moves the ball
		if (y - velocityY > windowLength - length) {
			y = windowLength - length;
			velocityY /= -(1.5);
		} else if (y - velocityY < 0) {
			y = 0;
			velocityY /= -(1.5);
		} else {
			y -= velocityY;
		}
	}

	public void update() {
		velocityX += accelerationX;
		gravityCounter -= 1;
		if (gravityCounter < 0) {
			gravityOn = true;
		}
		if (gravityOn) {
			velocityY += accelerationY;
		}
		//controls the ball from moving to fast
		if (velocityX > 2) {
			velocityX = 2;
		}
		if (velocityX < -2) {
			velocityX = -2;
		}
		if (velocityY > 2) {
			velocityY = 2;
		}
		if (velocityY < -2) {
			velocityY = -2;
		}
		acceleration();
		//checks to make sure ball isnt moving out of bounds
		if (!out) {
			moveX();
			moveY();
			updateCoord();
		}
		oldVelocityX = velocityX;
		oldVelocityY = velocityY;
	}
}
