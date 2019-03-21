package gameoflife.views;

import gameoflife.model.Coordinates;
import gameoflife.model.GameOfLife;
import gameoflife.model.ThreadManager;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class WindowController {
    @FXML
    private GridView gridView;

    @FXML
    private Slider speedSlider;

    private boolean running=false;

    public void moveNorth(){
        gridView.moveView(0,-5);
    }

    public void moveEast(){
        gridView.moveView(5,0);
    }

    public void moveSouth(){
        gridView.moveView(0,5);
    }

    public void moveWest(){
        gridView.moveView(-5,0);
    }

    public void buildHandlers(){
        Scene scene = speedSlider.getScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case UP:
                        moveNorth();
                        event.consume();
                        break;
                    case DOWN:
                        moveSouth();
                        event.consume();
                        break;
                    case LEFT:
                        moveWest();
                        event.consume();
                        break;
                    case RIGHT:
                        moveEast();
                        event.consume();
                        break;
                }
            }
        });
    }

    public void gridClick(MouseEvent e){
        GameOfLife.getInstance().modifyCell(new Coordinates((((int)e.getX())/50)+gridView.getViewport().getX(),(((int)e.getY())/50)+gridView.getViewport().getY()));
    }

    public void startStop(){
        if(running){
            ThreadManager.getInstance().stopAll();
            running=false;
        }
        else {
            Task<Void> task=new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while(true) {
                        GameOfLife.getInstance().step();
                        try {
                            Thread.sleep(600 / (long) speedSlider.getValue());
                        } catch (InterruptedException e) {
                            return null;
                        }
                    }
                }
            };
            ThreadManager.getInstance().start(task);
            running=true;
        }
    }
}
