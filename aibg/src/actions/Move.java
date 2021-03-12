package actions;

public class Move extends Action{
	public char direction;
	public int distance;

	public Move(char direction, int distance) {
		this.direction = direction;
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Move " + direction + " " + distance;
	}
}
