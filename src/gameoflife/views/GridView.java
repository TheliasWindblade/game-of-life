package gameoflife.views;

import gameoflife.model.Coordinates;
import gameoflife.model.GameOfLife;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.*;

public class GridView extends StackPane implements Observer {

    private Canvas canvas;
    private Coordinates currView;

    public GridView(){
        currView=new Coordinates(0,0);
        canvas=new Canvas(900,900);
        redraw();
        this.getChildren().add(canvas);
        GameOfLife.getInstance().addObserver(this);

    }

    public void moveView(int x, int y){
        currView.setX(currView.getX()+x);
        currView.setY(currView.getY()+y);
        redraw();
    }

    public Coordinates getViewport(){
        return currView;
    }

    @Override
    public void update(Observable o, Object arg) {
        redraw();
    }

    private void redraw(){
        //draw
        GraphicsContext gc=canvas.getGraphicsContext2D();
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setFill(Color.RED);
        //draw cells
        for(Coordinates c : GameOfLife.getInstance().getLiveCells()){
            if(c.getX()<currView.getX() || c.getY()<currView.getY() || c.getX()>currView.getX()+canvas.getWidth()/50 || c.getY()>currView.getY()+canvas.getHeight()/50) continue;
            gc.fillRect((c.getX()-currView.getX())*50,(c.getY()-currView.getY())*50,50,50);
        }
        //draw grid
        gc.setStroke(Color.BLACK);
        for(int i=0;i<=canvas.getWidth();i+=50){
            gc.strokeLine(i,0,i,canvas.getHeight());
        }
        for(int i=0;i<=canvas.getHeight();i+=50){
            gc.strokeLine(0,i,canvas.getWidth(),i);
        }
    }
}
