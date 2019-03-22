package gameoflife.views;

import gameoflife.model.Coordinates;
import gameoflife.model.GameOfLife;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.Observer;

public class GridView extends Canvas implements Observer {

    private Coordinates currView;
    private int viewportSize=16;
    private Color cellColor=Color.BLACK;
    private Color backgroundColor=Color.WHITE;
    private Color gridColor=Color.BLACK;

    public GridView(){
        super(800,800);
        currView=new Coordinates(0,0);
        redraw();
        GameOfLife.getInstance().addObserver(this);
    }

    public void setCellColor(Color cellColor) {
        this.cellColor = cellColor;
        redraw();
    }

    public Color getCellColor() {
        return cellColor;
    }

    public void setBackgroundColor(Color cellColor) {
        this.backgroundColor = cellColor;
        redraw();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setGridColor(Color cellColor) {
        this.gridColor = cellColor;
        redraw();
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void moveView(int x, int y){
        currView.setX(currView.getX()+x);
        currView.setY(currView.getY()+y);
        redraw();
    }

    public void setView(Coordinates c){
        currView=c;
        redraw();
    }

    public void resizeViewport(int c){
        viewportSize=c;
        redraw();
    }

    public Coordinates getViewport(){
        return currView;
    }

    public int getViewportSize() {
        return viewportSize;
    }

    @Override
    public void update(Observable o, Object arg) {
        redraw();
    }

    private void redraw(){
        Canvas canvas=this;
        //resize
        int cellSize=(int)canvas.getWidth()/viewportSize;
        //draw
        GraphicsContext gc=canvas.getGraphicsContext2D();
        gc.setFill(backgroundColor);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setFill(cellColor);
        //draw cells
        for(Coordinates c : GameOfLife.getInstance().getLiveCells()){
            if(c.getX()<currView.getX() || c.getY()<currView.getY() || c.getX()>currView.getX()+canvas.getWidth()/cellSize || c.getY()>currView.getY()+canvas.getHeight()/cellSize) continue;
            gc.fillRect((c.getX()-currView.getX())*cellSize,(c.getY()-currView.getY())*cellSize,cellSize,cellSize);
        }
        //draw grid
        gc.setStroke(gridColor);
        for(int i=0;i<=canvas.getWidth();i+=cellSize){
            gc.strokeLine(i,0,i,canvas.getHeight());
        }
        for(int i=0;i<=canvas.getHeight();i+=cellSize){
            gc.strokeLine(0,i,canvas.getWidth(),i);
        }
    }
}
