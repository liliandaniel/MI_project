///asd.kacsa,Daniel.Lilian.Anita@stud.u-szeged.hu

import game.quoridor.QuoridorPlayer;
import game.quoridor.utils.QuoridorAction;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import game.quoridor.MoveAction;
import game.quoridor.QuoridorGame;
import game.quoridor.QuoridorPlayer;
import game.quoridor.WallAction;
import game.quoridor.players.DummyPlayer;
import game.quoridor.utils.PlaceObject;
import game.quoridor.utils.QuoridorAction;
import game.quoridor.utils.WallObject;

public class Agent extends QuoridorPlayer {

    private final List<WallObject> walls = new LinkedList<WallObject>();
    private final QuoridorPlayer[] players = new QuoridorPlayer[2];
    private int numWalls;
    /**
     * elozo lepeseink helye
     */
    private int[][] prevSteps = new int[9][9];//a bejaras soran erintettuk-e mar az adott mezot
    /** cel index*/
    private int final_i;
    /** cel index*/
    private int final_j;
    /** merre lepjunk hogy a celbe erjunk*/
    private int add_i=0;
    /** merre lepjunk hogy a celbe erjunk*/
    private int add_j=0;

    /**
     *elozo lepeseink zombot feltoltom 0-val
     *majd meghatarozom a jo iranyt
     *
     * @param i
     * @param j
     * @param color
     * @param random
     */
    public Agent(int i, int j, int color, Random random) {
        super(i, j, color, random);
        players[color] = this;
        players[1-color] = new DummyPlayer((1-color) * (QuoridorGame.HEIGHT - 1), j, 1-color, null);
        numWalls = 0;
        for(int k=0;k<9;k++){
            for(int l=0;l<9;l++){
                prevSteps[k][l]=0;
            }
        }
        //cél meghatáozása
        if(i==0 && j==4){ //fent kezdunk
            final_i=8;
            final_j=4;
            add_i=1;//jelen esetben ahhoz hogy 0 8 legyen mindig 1 el novelni kell
        }
        if(j==0 && i==4) {//bal oldalt kezdunk
            final_j=8;
            final_i=4;
            add_j=1;
        }
        if(i==8 && j==4){//lent kezdunk
            final_i=0;
            final_j=4;
            add_i=-1;
        }
        if(j==8 && i==4) {//jobb oldalt kezdunk
            final_j=0;
            final_i=4;
            add_i=-1;
        }

    }

    /**
     *ha lehet ugy elore lepni, hogy meg nem voltunk ott akkor meglepjuk
     *ha mar voltunk ott vagy nem lehet elore lepni, akkor jobbra menjunk el
     *ha jobbra sem lehet lepni, vagy voltunk mar ott akkor pedig balra
     *kulonben zsakutcaban vagyunk, ezert menjunk lefele
     *
     * @param prevAction
     * @param remainingTimes
     * @return vissza adja a megfelelo lepest
     */
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

        PlaceObject me = new PlaceObject(i,j);//itt allok most

        //vegig iteraljuk a lehetseges lepeseket
        for (PlaceObject lepes : me.getNeighbors(walls, players)){//elore
            //prioritas --> cel megkozelitese
            if(lepes.i == i+add_i && lepes.j==j+add_j && prevSteps[i+add_i][j+add_j]==0){
                prevSteps[lepes.i][lepes.j]=1;//ha meglepjuk a lepest akkor felírjuk
                return new MoveAction(i,j, lepes.i, lepes.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){// egyik oldal
            if(n.i == i+add_j && n.j==j+add_i && prevSteps[i+add_j][j+add_i]==0){
                prevSteps[n.i][n.j]=1;
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){//masik oldal
            if(n.i == i+(add_j*-1) && n.j==j+(add_i*-1) && prevSteps[i+add_j*-1][j+add_i*-1]==0){
                prevSteps[n.i][n.j]=1;
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){//hattra
            if(n.i == (i+add_i*-1) && n.j==(j+add_j*-1) ){
                prevSteps[n.i][n.j]=1;
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){// egyik oldal
            if(n.i == i+add_j && n.j==j+add_i && prevSteps[i+add_j][j+add_i]==1){
                prevSteps[n.i][n.j]+=1;
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        for (PlaceObject n : me.getNeighbors(walls, players)){//masik oldal
            if(n.i == i+(add_j*-1) && n.j==j+(add_i*-1) && prevSteps[i+add_j*-1][j+add_i*-1]>=1){
                prevSteps[n.i][n.j]+=1;
                return new MoveAction(i,j, n.i, n.j);
            }
        }
        return null;
    }
}