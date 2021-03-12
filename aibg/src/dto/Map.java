package dto;

import java.io.Serializable;

public class Map implements Serializable{
	public Tile[][] tiles;
	public int numberOfFreeSpots;

	public Map(Tile[][] tiles, int numberOfFreeSpots) {
		this.tiles = tiles;
		this.numberOfFreeSpots = numberOfFreeSpots;
	}

	public Map() {
	}

	@Override
	protected Map clone() {
		Tile[][] tiles = new Tile[this.tiles.length][this.tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = (Tile) this.tiles[i][j].clone();
			}
		}
		return new Map(tiles, numberOfFreeSpots);
	}
}
