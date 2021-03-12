package aibg;

import actions.Action;
import dto.GameState;
import game.MiniMax;

public class Worker extends Thread{
    GameState gs;
    volatile Action bestMove;

    public Worker(GameState gs){
        super();
        this.gs = gs;
    }

    @Override
    public void run(){
        bestMove = new MiniMax(gs).getBestMove();
    }


}
