package dto;

import java.io.Serializable;

public class Tile implements Serializable{
	public int row, column;
	public String ownedByTeam;
	public TileContent tileContent;

	public Tile(int row, int column, String ownedByTeam, TileContent tileContent) {
		this.row = row;
		this.column = column;
		this.ownedByTeam = ownedByTeam;
		this.tileContent = tileContent;
	}

	public Tile() {
	}

	@Override
	protected Tile clone() {
		return new Tile(row, column, ownedByTeam, tileContent.clone());
	}
}
