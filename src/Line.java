

public class Line extends Obstacle {
	int xStart;
	int yStart;
	int xEnd;
	int yEnd;
	float buffer = 5;

	public int[] definitions() {
		int[] coord = { xStart, yStart, xEnd, yEnd };
		return coord;
	}

	public Line(int x, int y, int x1, int y1) {
		xStart = x;
		yStart = y;
		xEnd = x1;
		yEnd = y1;
		type = "line";
		multiplier = 1;
	}

	@Override
	public boolean interact(int x, int y) {
		//determines if collision using the distance from each end point of the line
		//if the sum of the distance between two end points is equal to the length of the line then the algorithm knows the point is on the line
		//adds a buffer to account for the point being passed being the origin of the ball as well as a buffer for error (balls moving at high velocities)
		boolean collides = false;
		float d1 = dist(x, y, xStart, yStart);
		float d2 = dist(x, y, xEnd, yEnd);
		float sum = d1 + d2;
		float length = dist(xStart, yStart, xEnd, yEnd);
		if (sum > length - buffer && sum < length + buffer) {
			collides = true;
		}
		return collides;
	}

	private float dist(int x, int y, int x1, int y1) {
		float distance = (float) (Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2)));
		return distance;
	}

	@Override
	public void update() {

	}

	@Override
	public void userAction() {
	}



}
