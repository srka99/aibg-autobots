package dto;

import java.io.Serializable;

public class Tile implements Serializable{
	public int row, column;
	public String ownedByTeam;
	public TileContent tileContent;
}
