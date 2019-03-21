package gameoflife.views;

import gameoflife.model.Coordinates;
import gameoflife.model.GameOfLife;
import gameoflife.model.ThreadManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.NumberStringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


public class WindowController {
    @FXML
    private GridView gridView;

    @FXML
    private Slider speedSlider;

    @FXML
    private Button playButton;

    @FXML
    private TextField xCoord;

    @FXML
    private TextField yCoord;

    @FXML
    private TextField sizePrompt;

    @FXML
    private ColorPicker cellColor;


    private boolean running=false;

    public void moveNorth(){
        moveView(0,-5);
    }

    public void moveEast(){
        moveView(5,0);
    }

    public void moveSouth(){
        moveView(0,5);
    }

    public void moveWest(){
        moveView(-5,0);
    }

    public void moveView(int x, int y) {
        gridView.moveView(x,y);
        xCoord.setText(Integer.toString(gridView.getViewport().getX()));
        yCoord.setText(Integer.toString(gridView.getViewport().getY()));
    }

    public void setView(){
        Coordinates c = new Coordinates(Integer.valueOf(xCoord.getText()),Integer.valueOf(yCoord.getText()));
        gridView.setView(c);
        xCoord.setText(Integer.toString(gridView.getViewport().getX()));
        yCoord.setText(Integer.toString(gridView.getViewport().getY()));
    }

    public void setViewportSize(){
        int c = Integer.valueOf(sizePrompt.getText());
        gridView.resizeViewport(c);
    }

    public void refocusGrid(){ playButton.getParent().requestFocus();}

    public void changeCellColor(){
        gridView.setCellColor(cellColor.getValue());
    }

    public void buildHandlers(){
        Pattern pattern=Pattern.compile("-?\\d+");
        xCoord.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null));
        yCoord.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null));
        sizePrompt.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null));

        xCoord.setText("0");
        yCoord.setText("0");
        sizePrompt.setText("16");

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
        GameOfLife.getInstance().modifyCell(new Coordinates((((int)e.getX())/((int)gridView.getWidth()/gridView.getViewportSize()))+gridView.getViewport().getX(),(((int)e.getY())/((int)gridView.getHeight()/gridView.getViewportSize()))+gridView.getViewport().getY()));
        refocusGrid();
    }

    public void exit(){
        System.exit(0);
    }

    public void clearBoard(){
        GameOfLife.getInstance().clear();
    }

    public void openAbout(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Game of Life");
        alert.setContentText("By Thelias\nVersion 1.1.2");
        alert.showAndWait();
    }

    public void startStop(){
        if(running){
            ThreadManager.getInstance().stopAll();
            playButton.setText("START");
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
            playButton.setText("STOP");
            running=true;
        }
    }
}
