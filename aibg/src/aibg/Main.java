package aibg;

import actions.Action;
import actions.MakeGame;
import actions.Move;
import dto.GameState;
import dto.Player;
import game.MiniMax;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		int playerId = 491355, playerId2 = 491356;

		GameState game = Api.playAction(new MakeGame(), playerId, 0);
		System.out.println(game.gameId);
		System.out.println(game.player1.teamName);
		int expectedLastX = 0, expectedLastY = 0;
		while (!game.finished) {
			Worker w = new Worker(game);
			w.start();
			Thread.sleep(4500);
			w.interrupt();
			w.join();
			Player currentPlayer = (game.player1.teamName.equals("autobots")) ? game.player1 : game.player2;
			System.out.println(currentPlayer.executedAction);
			System.out.println(game.numOfMove + ": " + w.bestMove);
			if (expectedLastX != currentPlayer.x || expectedLastY != currentPlayer.y) {
				System.out.println("!!!Expected " + expectedLastX + " " + expectedLastY + " but got " + currentPlayer.x + " " + currentPlayer.y);
			}
			boolean ok = game.tryMove(w.bestMove, true);
			currentPlayer = (game.player1.teamName.equals("autobots")) ? game.player1 : game.player2;
			expectedLastX = currentPlayer.x;
			expectedLastY = currentPlayer.y;
			game = Api.playAction(w.bestMove, playerId, game.gameId);

		}

		System.out.println();
		System.out.println(game.player1.teamName + " " + game.player1.score);
		System.out.println(game.player2.teamName + " " + game.player2.score);
		System.out.println("Winner: " + game.winnerTeamName);
	}

}
