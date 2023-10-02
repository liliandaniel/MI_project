import game.quoridor.MoveAction;
import game.quoridor.QuoridorGame;
import game.quoridor.QuoridorPlayer;
import game.quoridor.WallAction;
import game.quoridor.players.DummyPlayer;
import game.quoridor.utils.PlaceObject;
import game.quoridor.utils.QuoridorAction;
import game.quoridor.utils.WallObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SamplePlayer extends QuoridorPlayer {

    private final List<WallObject> walls = new LinkedList<WallObject>();
    private final QuoridorPlayer[] players = new QuoridorPlayer[2];
    private int numWalls;
    private int[][] prevSteps = new int[9][9];
    private int final_i;
    private int final_j;
    private int add_i=0;
    private int add_j=0;
    public SamplePlayer(int i, int j, int color, Random random) {
        super(i, j, color, random);
        players[color] = this;
        players[1-color] = new DummyPlayer((1-color) * (QuoridorGame.HEIGHT - 1), j, 1-color, null);
        numWalls = 0;
        for(int k=0;k<9;k++){
            for(int l=0;l<9;l++){
                prevSteps[k][l]=0;
            }
        }
        if(i==0 && j==4){
            final_i=8;
            final_j=4;
            add_i=1;
        }
        if(j==0 && i==4) {
            final_j=8;
            final_i=4;
            add_j=1;
        }
        if(i==8 && j==4){
            final_i=0;
            final_j=4;
            add_i=-1;
        }
        if(j==8 && i==4) {
            final_j=0;
            final_i=4;
            add_i=-1;
        }

    }


    @Override
    public QuoridorAction getAction(QuoridorAction prevAction, long[] remainingTimes) {


        if (prevAction instanceof WallAction) {
            WallAction a = (WallAction) prevAction;
            walls.add(new WallObject(a.i, a.j, a.horizontal));
        } else if (prevAction instanceof MoveAction) {
            MoveAction a = (MoveAction) prevAction;
            players[1 - color].i = a.to_i;
            players[1 - color].j = a.to_j;
        }
        //System.out.println(21*2+i*21*2+j*2+2+i); helyünk a tömbben
        //System.out.println(QuoridorGame.print(QuoridorGame.getBoard(walls, players)).charAt(21*2+i*21*2+j*2+2+i));//44elso mezo-->44+
        PlaceObject me = new PlaceObject(i,j);
        //me= new PlaceObject(i,j);

        for (PlaceObject lepes : me.getNeighbors(walls, players)){//elore
            System.out.println(lepes.i + " " + lepes.j);
            if(lepes.i == i+add_i && lepes.j==j+add_j && prevSteps[i+add_i][j+add_j]==0){
                prevSteps[lepes.i][lepes.j]=1;
                System.out.println("itt van");
                return new MoveAction(i,j, lepes.i, lepes.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){// egyik oldal
            if(n.i == i+add_j && n.j==j+add_i && prevSteps[i+add_j][j+add_i]==0){
                prevSteps[n.i][n.j]=1;System.out.println("itt van");
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){//masik oldal
            if(n.i == i+(add_j*-1) && n.j==j+(add_i*-1) && prevSteps[i+add_j*-1][j+add_i*-1]==0){
                prevSteps[n.i][n.j]=1;System.out.println("itt van");
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){//hattra
            if(n.i == (i+add_i*-1) && n.j==(j+add_j*-1) ){
                prevSteps[n.i][n.j]=1;System.out.println("itt van");
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){// egyik oldal
            if(n.i == i+add_j && n.j==j+add_i && prevSteps[i+add_j][j+add_i]==1){
                prevSteps[n.i][n.j]+=1;System.out.println("itt van");
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){//masik oldal
            if(n.i == i+(add_j*-1) && n.j==j+(add_i*-1) && prevSteps[i+add_j*-1][j+add_i*-1]>=1){
                prevSteps[n.i][n.j]+=1;System.out.println("itt van");
                return new MoveAction(i,j, n.i, n.j);
            }
        }

        /*
        if(this.getStep(add_i,add_j)!=null) return this.getStep(add_i,add_j);
        //if(this.getStep(add_i*2,add_j*2)!=null) return this.getStep(add_i*2,add_j*2);
        if(this.getStep(0,1)!=null && prevSteps[i][j+1]!=1) return this.getStep(0,1);
        if(this.getStep(1,0)!=null && prevSteps[i+1][j]!=1) return this.getStep(1,0);

        /*MoveAction next_step = new MoveAction(i,j, i+add_i, j+add_j);
        if(QuoridorGame.isValid(next_step)){
            i+=add_i;
            j+=add_j;
            return next_step;
        }
        next_step = new MoveAction(i,j, i+add_i*2, j+add_j*2);
        if(QuoridorGame.isValid(next_step)){
            i+=add_i*2;
            j+=add_j*2;
            prevSteps[i][j]=1;
            return next_step;
        }
        next_step = new MoveAction(i,j, i+1, j);*/
        return null;
    }
}