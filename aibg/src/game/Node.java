package game;

import java.util.LinkedList;

import actions.Action;
import dto.GameState;

public class Node {
	public LinkedList<Node> children = new LinkedList<>();
    public Node parent;
    public boolean isLastChild;
    
	public boolean isMaxPlayer;
    public int score;
    public Action actionDone;
    public GameState gameState;
}