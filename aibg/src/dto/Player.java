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
}
