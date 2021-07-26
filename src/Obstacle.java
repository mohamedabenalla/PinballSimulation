
public abstract class Obstacle {
	//game obstacles superclass
	String type;
	double multiplier;
	public Obstacle() {

	}
	public String type() {
		return type;
	}
	public double multiplier() {
		return multiplier;
	}
	public abstract int[] definitions();
	public abstract boolean interact(int x, int y);
	public abstract void update();
	public abstract void userAction();
	
}
