package dto;

import java.io.Serializable;

public class Player implements Serializable{
	public int x, y, score, gatheredKoalas, energy;
	public boolean hasFreeASpot;
	public int numberOfUsedFreeASpot;
	public int numOfSkipATurnUsed;
	public String executedAction;
	public String teamName;
	public int skin;


	public Player(int x, int y, int score, int gatheredKoalas, int energy, boolean hasFreeASpot, int numberOfUsedFreeASpot, int numOfSkipATurnUsed, String executedAction, String teamName, int skin) {
		this.x = x;
		this.y = y;
		this.score = score;
		this.gatheredKoalas = gatheredKoalas;
		this.energy = energy;
		this.hasFreeASpot = hasFreeASpot;
		this.numberOfUsedFreeASpot = numberOfUsedFreeASpot;
		this.numOfSkipATurnUsed = numOfSkipATurnUsed;
		this.executedAction = executedAction;
		this.teamName = teamName;
		this.skin = skin;
	}

	public Player() {
	}

	@Override
	protected Player clone() {
		return new Player(x, y, score, gatheredKoalas, energy, hasFreeASpot, numberOfUsedFreeASpot, numOfSkipATurnUsed, executedAction, teamName, skin);
	}
}
