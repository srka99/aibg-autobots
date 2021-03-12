package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import actions.*;
import dto.GameState;

public class MiniMax {
	public Node root;
	public Action bestMove;
	public List<Action> actions = new ArrayList<>();
	
	public MiniMax(GameState gameState) {
		root = new Node();
		root.isMaxPlayer = true;
		root.gameState = gameState;
		addActions();
	}
	
	public Action getBestMove() {
		LinkedList<Node> queue = new LinkedList<>(); 
		bestMove = actions.get(0);
		queue.add(root);

		while(!Thread.currentThread().isInterrupted() && queue.size() > 0) {
			Node curr = queue.removeFirst();
			curr.score = calculateScore(curr.gameState);
			
			GameState newState = curr.gameState.clone();
			Iterator<Action> it = actions.iterator();
			
			Action currAction;
			while(it.hasNext()) {
				currAction = it.next();
				if (newState.tryMove(currAction, curr.isMaxPlayer)) {
					Node newNode = createNode(curr, newState, currAction);
					curr.children.add(newNode);
					queue.add(newNode);
					newState = curr.gameState.clone();
				}
			}
			if (curr.children.size() > 0)
				curr.children.getLast().isLastChild = true;
			
			if(curr.isLastChild) {
				adjustState(curr.parent);
			}
		}

		int max = Integer.MIN_VALUE;

		for(Node ch : root.children){
			if(ch.score > max){
				bestMove = ch.actionDone;
				max = ch.score;
			}
		}

		return bestMove;
	}
	
	private int calculateScore(GameState gameState) {
		int score1, score2;

		score1 = gameState.player1.score + gameState.player1.energy*100 + ((gameState.player1.hasFreeASpot && gameState.player1.numberOfUsedFreeASpot == 0) ? 700 : 0);
		score2 = gameState.player2.score + gameState.player2.energy*100 + ((gameState.player2.hasFreeASpot && gameState.player2.numberOfUsedFreeASpot == 0) ? 700 : 0);

		if(!gameState.player1.teamName.equals("autobots")) {
			int temp = score1;
			score1 = score2;
			score2 = temp;
		}

		return score1 - score2;
	}

	private void addActions() {
		/*for (int i = 0; i <= 26; i++) {
			for (int j = 0; j <= 8; j++) {
				actions.add(new FreeASpot(i, j));
			}
		}*/
		for (int i = 0; i < 6; i++) {
			for (int j = 1; j <= 5; j++) {
				actions.add(new Move("qweasd".charAt(i), j));
			}
		}
		actions.add(new SkipATurn());
		actions.add(new StealKoalas());
		actions.add(new Teleport());
	}
	
	private Node createNode(Node parent, GameState newState, Action move) {
		Node node = new Node();
		node.parent = parent;
		node.gameState = newState;
		node.isMaxPlayer = !parent.isMaxPlayer;
		node.actionDone = move;
		
		return node;
	}
	
	private void adjustState(Node node) {
		do {
			int newScore;
			if(node.isMaxPlayer) {
				newScore = getMax(node.children);
			}else {
				newScore = getMin(node.children);
			}
			node.score = newScore;
			
			node = node.parent;
		}while(node != null);
	}
	
	private int getMax(List<Node> children) {
		int max = Integer.MIN_VALUE;
		
		for(Node c : children) {
			if(c.score > max) {
				max = c.score;
			}
		}
		
		return max;
	}
	
	private int getMin(List<Node> children) {
		int min = Integer.MAX_VALUE;
		
		for(Node c : children) {
			if(c.score < min) {
				min = c.score;
			}
		}
		
		return min;
	}


	
}
