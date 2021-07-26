
public class Tester {
	public static void main(String[]args) {
		int[] x = {0,500,220,500};
		angle(x, -2, -1);
	}
	private static void angle(int[] definitions, float velocityX, float velocityY) {
		float angleLine = (float) Math.atan((-(double)(definitions[1] - definitions[3]))/ ((double)(0.001 + definitions[0] - definitions[2])));
		if(definitions[0] > definitions[2]) {
			angleLine += Math.PI;
		}
		float angleVelocity = (float) (Math.atan(-velocityY / (velocityX + 0.001)));
		
		if (velocityX>= 0) {
			angleVelocity += Math.PI;
		}
		
		float angle = angleVelocity;
		System.out.print(angleLine / Math.PI);
		System.out.println("    " + angleVelocity / Math.PI);
		float diff = Math.abs(angleLine - angleVelocity);
		if(angleLine > angleVelocity) {
			angle = (float) (angleLine - Math.PI + diff);
		} else if (angleLine < angleVelocity ) {
			angle = (float) (angleLine + Math.PI - diff);
		}
		System.out.println(angle);
	}
}
