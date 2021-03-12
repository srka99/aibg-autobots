package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import actions.*;
import org.apache.commons.lang3.SerializationUtils;

public class GameState implements Serializable, Cloneable{
	public int gameId;
	public Player player1, player2;
	public int currentPlayer;
	public Map map;
	public int numberOfFreeSpots;
	public Object[] player1ChangedTiles;
	public Object[] player2ChangedTiles;
	public int numOfMove;
	public String winnerTeamName;
	public boolean finished;
	
	@Override
	public GameState clone() {
		return SerializationUtils.clone(this);
	}

	private int moveX(char c, int x) {
		int newX = x;
		if (c == 'q') {
			newX -= 1;
		}
		if (c == 'w') {
			newX -= 2;
		}
		if (c == 'e') {
			newX -= 1;
		}
		if (c == 'd') {
			newX += 1;
		}
		if (c == 's') {
			newX += 2;
		}
		if (c == 'a') {
			newX += 1;
		}
		return newX;
	}

	private int moveY(char c, int x, int y) {
		int newY = y;
		if (c == 'q') {
			newY -= x % 2 == 0 ? 1 : 0;
		}
		if (c == 'w') {

		}
		if (c == 'e') {
			newY += x % 2 == 0 ? 0 : 1;
		}
		if (c == 'd') {
			newY += x % 2 == 0 ? 0 : 1;
		}
		if (c == 's') {

		}
		if (c == 'a') {
			newY -= x % 2 == 0 ? 1 : 0;
		}
		return newY;
	}

	private boolean isStuck(Player currentPlayer, Player otherPlayer) {
		for (char c : "qweasd".toCharArray()) {
			int newX = moveX(c, currentPlayer.x), newY = moveY(c, currentPlayer.x, currentPlayer.y);

			if (newX >= 0 && newX <= 26 && newY >= 0 && newY <= 8 && map.tiles[newX][newY].ownedByTeam.equals("") && !map.tiles[newX][newY].tileContent.itemType.equals("HOLE") && !(otherPlayer.x == newX && otherPlayer.y == newY)) {
				return false;
			}
		}

		return true;
	}

	private boolean adjacent(int x1, int y1, int x2, int y2) {
		for (char c : "qweasd".toCharArray()) {
			int newY = moveY(c, x1, y1);
			int newX = moveX(c, x1);
			if (newY == y2 && newX == x2) {
				return true;
			}
		}
		return false;
	}

	public boolean tryMove(Action action, boolean isMaxPlayer) {
		if (numOfMove > 250) {
			return false;
		}
		Player currentPlayer = (isMaxPlayer ^ player1.teamName.equals("autobots")) ? player2 : player1;
		Player otherPlayer = (isMaxPlayer ^ player1.teamName.equals("autobots")) ? player1 : player2;
		/*if (action instanceof FreeASpot) {
			FreeASpot a = (FreeASpot) action;
			if (!currentPlayer.hasFreeASpot || isStuck(currentPlayer, otherPlayer) || map.tiles[a.x][a.y].ownedByTeam.equals("") || currentPlayer.energy < (3*(1+ currentPlayer.numberOfUsedFreeASpot))) {
				return false;
			}
			numberOfFreeSpots++;
			numOfMove++;
			currentPlayer.energy -= (3*(1+ currentPlayer.numberOfUsedFreeASpot)) - 1;
			currentPlayer.numberOfUsedFreeASpot++;
			currentPlayer.score += map.tiles[a.x][a.y].ownedByTeam.equals(currentPlayer.teamName) ? -100 : 100;
			otherPlayer.score -= map.tiles[a.x][a.y].ownedByTeam.equals(currentPlayer.teamName) ? 0 : 100;
			map.tiles[a.x][a.y].ownedByTeam = "";
			return true;
		}*/
		if (action instanceof Move) {
			Move a = (Move) action;
			if (currentPlayer.energy < a.distance || isStuck(currentPlayer, otherPlayer)) {
				return false;
			}
			int currentDistance = 0;
			List<Tile> tilesToPass = new ArrayList<>();

			int newX = currentPlayer.x, newY = currentPlayer.y;
			while (currentDistance < a.distance) {
				newY = moveY(a.direction, newX, newY);
				newX = moveX(a.direction, newX);

				if (newX < 0 || newX > 26 || newY < 0 || newY > 8 || !map.tiles[newX][newY].ownedByTeam.equals("") || map.tiles[newX][newY].tileContent.itemType.equals("HOLE") || (otherPlayer.x == newX && otherPlayer.y == newY)) {
					return false;
				}

				tilesToPass.add(map.tiles[newX][newY]);

				currentDistance++;
			}
			for (Tile tile : tilesToPass) {
				if (tile.tileContent.itemType.equals("KOALA")) {
					currentPlayer.gatheredKoalas++;
					currentPlayer.score += 150;
				}
				else if (tile.tileContent.itemType.equals("KOALA_CREW")) {
					currentPlayer.gatheredKoalas += 10;
					currentPlayer.score += 1500;
				}
				else if (tile.tileContent.itemType.equals("ENERGY")) {
					currentPlayer.energy++;
				}
				tile.tileContent.itemType = "EMPTY";
			}

			currentPlayer.x = newX;
			currentPlayer.y = newY;
			currentPlayer.energy -= a.distance - 1;
			map.tiles[newX][newY].ownedByTeam = currentPlayer.teamName;
			numberOfFreeSpots--;
			numOfMove++;
			return true;
		}
		if (action instanceof SkipATurn) {
			if (isStuck(currentPlayer, otherPlayer) || currentPlayer.numOfSkipATurnUsed >= 5) {
				return false;
			}
			currentPlayer.energy += 3;
			currentPlayer.numOfSkipATurnUsed++;
			numOfMove++;
			return true;
		}
		if (action instanceof Teleport) {
			if (!isStuck(currentPlayer, otherPlayer)) {
				return false;
			}
			char[] moves = "dewqas".toCharArray();
			int radius = 2;
			int newX = currentPlayer.x, newY = currentPlayer.y;
			newY = moveY('a', newX, newY);
			newX = moveX('a', newX);
			newY = moveY('a', newX, newY);
			newX = moveX('a', newX);
			if (!(newX < 0 || newX > 26 || newY < 0 || newY > 8 || !map.tiles[newX][newY].ownedByTeam.equals("") || map.tiles[newX][newY].tileContent.itemType.equals("HOLE") || (otherPlayer.x == newX && otherPlayer.y == newY))) {
				currentPlayer.x= newX;
				currentPlayer.y= newY;
				currentPlayer.score -= 500;
				map.tiles[newX][newY].ownedByTeam = currentPlayer.teamName;
			}
			else {
				outerloop:
				while (radius < 50) {
					for (char c : moves) {
						for (int i = 1; i <= radius; i++) {
							newY = moveY(c, newX, newY);
							newX = moveX(c, newX);
							if (newX < 0 || newX > 26 || newY < 0 || newY > 8 || !map.tiles[newX][newY].ownedByTeam.equals("") || map.tiles[newX][newY].tileContent.itemType.equals("HOLE") || (otherPlayer.x == newX && otherPlayer.y == newY)) {
								continue;
							}
							currentPlayer.x = newX;
							currentPlayer.y = newY;
							currentPlayer.score -= 500;
							break outerloop;
						}
					}
					newY = moveY('a', newX, newY);
					newX = moveX('a', newX);
					radius++;
				}
			}
			numOfMove++;
			return true;
		}
		if (action instanceof StealKoalas) {
			if (currentPlayer.energy < 5 || !adjacent(currentPlayer.x, currentPlayer.y, otherPlayer.x, otherPlayer.y) || isStuck(currentPlayer, otherPlayer)) {
				return false;
			}
			int koalas = Math.min(10, otherPlayer.gatheredKoalas);
			currentPlayer.gatheredKoalas+= koalas;
			otherPlayer.gatheredKoalas -= koalas;
			currentPlayer.score += koalas * 150;
			otherPlayer.score -= koalas * 150;
			currentPlayer.energy -= 4;
			numOfMove++;
			return true;
		}
		return false;
	}
}
