
public class Player extends Line {
	//player paddles
	private double degree = 0;
	private double dist = xEnd - xStart;
	private boolean left;
	private boolean rise;	
	public Player(int x, int y, int x1, int y1, boolean left) {
		super(x, y, x1, y1);
		this.left = left;
		multiplier = 2;
	}
	public void update() {
		if(rise)
		rise();
		else 
		fall();
	}
	public void userAction() {
		rise = true;
	}
	//rising animation
	private void rise() {		
		if(degree >= Math.PI / 4) {
			rise = false;
		} else {
		degree += 0.1;
		shift();
		}
	}
	//falling animation
	private void fall() {		
		if(degree <= 0) {
			if(left) {
				xEnd = (int) (xStart + dist);
				yEnd = (int) (yStart);
			} else {
				xStart = (int) (xEnd - dist);
				yStart = (int) (yEnd);
			}
		} else {
		degree -= 0.1;
		shift();
		}
	}
	//shifts paddles
	private void shift() {
		if(left) {
			xEnd = (int) (xStart + (Math.cos(degree) * dist));
			yEnd = (int) (yStart - (Math.sin(degree) * dist));
		} else {
			xStart = (int) (xEnd - (Math.cos(degree) * dist));
			yStart = (int) (yEnd - (Math.sin(degree) * dist));
		}
	}

}
