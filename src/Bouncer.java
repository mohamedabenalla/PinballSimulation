
public class Bouncer extends Obstacle{
	private int xStart;
	private int yStart;
	private int radius;
	public Bouncer(int x, int y, int r) {
		xStart = x;
		yStart = y;
		radius = r;
		type = "bouncer";
		multiplier = 1.01;
	}
	@Override
	public boolean interact(int x, int y) {
		boolean inbounds = false;
		float center_x;
		float center_y;
		center_x = (float) (this.xStart + radius);
		center_y = (float) (this.yStart + radius);
		float dist = (float) Math.sqrt(Math.pow(x - center_x, 2) + Math.pow(y - center_y, 2));
		if (dist < radius + 10)
			inbounds = true;
		return inbounds;
	}
	@Override
	public int[] definitions() {
		int[] def = {xStart, yStart, radius};
		return def;
	}
	@Override
	public void update() {
	}
	@Override
	public void userAction() {
	}

}
