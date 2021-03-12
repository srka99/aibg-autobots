package actions;

public class FreeASpot extends Action{
	public FreeASpot(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x, y;


	@Override
	public String toString() {
		return "FreeASpot{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
