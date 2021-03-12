package dto;

import java.io.Serializable;

public class TileContent implements Serializable{
	public String itemType;
	public int numOfItems;

	public TileContent(String itemType) {
		this.itemType = itemType;
	}

	public TileContent() {
	}

	@Override
	public TileContent clone() {
		return new TileContent(itemType);
	}
}
