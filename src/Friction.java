
public class Friction extends Force{

	public Friction(Force g, double coeF) {
		change(g.returnForce() * coeF);
	}

}
