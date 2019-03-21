package gameoflife.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;

public class GameOfLife extends Observable {
    private static GameOfLife instance=new GameOfLife();
    private HashSet<Coordinates> live;
    private int maxSize;

    private GameOfLife(){
        live=new HashSet<>();
        maxSize=100;
    }

    public static GameOfLife getInstance() {
        return instance;
    }

    public HashSet<Coordinates> getLiveCells(){
        return live;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    private void switchCell(Coordinates c){
        if(live.contains(c)){
            live.remove(c);
        }
        else{
            live.add(c);
        }
    }

    private HashMap<Coordinates,Integer> getNeighbours(){
        HashMap<Coordinates,Integer> n=new HashMap<>();
        for(Coordinates c : live){
            n.put(c,0);
        }
        for(Coordinates c : live){
            for(int i=-1;i<=1;i++){
                int k=1;
                if(i==0) k=2;
                for(int j=-1;j<=1;j+=k){
                    Integer r=n.get(new Coordinates(c.getX()+i,c.getY()+j));
                    if(r==null){
                        n.put(new Coordinates(c.getX()+i,c.getY()+j),1);
                    }
                    else{
                        n.replace(new Coordinates(c.getX()+i,c.getY()+j),r+1);
                    }
                }
            }
        }
        return n;
    }

    private void processCells(HashMap<Coordinates,Integer> n){
        for(Coordinates c : n.keySet()){
            int x=n.get(c);
            if(!live.contains(c)){
                if(x==3){
                    switchCell(c);
                }
            }
            else {
                if(Math.abs(c.getX())>maxSize || Math.abs(c.getY())>maxSize){
                    switchCell(c);
                }
                else if(x<2 || x>3)
                {
                    switchCell(c);
                }
            }
        }
    }

    public void step(){
        processCells(getNeighbours());
        setChanged();
        notifyObservers();
    }

    public void modifyCell(Coordinates c){
        switchCell(c);
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return live.toString();
    }
}
