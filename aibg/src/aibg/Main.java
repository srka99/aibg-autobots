package aibg;

import actions.Action;
import actions.JoinGame;
import actions.MakeGame;
import actions.Move;
import dto.GameState;
import dto.Player;
import game.MiniMax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		int playerId = 491355, playerId2 = 491356;
		int gameId = 0;
		try {
			gameId = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		//GameState game = Api.playAction(new MakeGame(), playerId, 0);
		GameState game = Api.playAction(new JoinGame(), playerId, gameId);
		System.out.println(game.gameId);
		System.out.println(game.player1.teamName);
		int expectedLastX = 0, expectedLastY = 0;
		while (!game.finished) {
			Worker w = new Worker(game);
			w.start();
			Thread.sleep(1000);
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
