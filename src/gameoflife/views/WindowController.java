package gameoflife.views;

import gameoflife.model.Coordinates;
import gameoflife.model.GameOfLife;
import gameoflife.model.ThreadManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.*;
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
    private Slider redCell;

    @FXML
    private Slider greenCell;
    
    @FXML
    private Slider blueCell;

    @FXML
    private Rectangle previewCellColor;

    @FXML
    private Slider redBackground;

    @FXML
    private Slider greenBackground;

    @FXML
    private Slider blueBackground;

    @FXML
    private Rectangle previewBackgroundColor;

    @FXML
    private Slider redGrid;

    @FXML
    private Slider greenGrid;

    @FXML
    private Slider blueGrid;

    @FXML
    private Rectangle previewGridColor;


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

    private void moveView(int x, int y) {
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
        saveConfig();
    }

    public void refocusGrid(){ playButton.getScene().getRoot().requestFocus();}

    private void changeCellColor(){
        double r=redCell.getValue();
        double g=greenCell.getValue();
        double b=blueCell.getValue();
        previewCellColor.setFill(Color.color(r,g,b));
        gridView.setCellColor(Color.color(r,g,b));
        saveConfig();
    }

    private void changeGridColor(){
        double r=redGrid.getValue();
        double g=greenGrid.getValue();
        double b=blueGrid.getValue();
        previewGridColor.setFill(Color.color(r,g,b));
        gridView.setGridColor(Color.color(r,g,b));
        saveConfig();
    }

    private void changeBackgroundColor(){
        double r=redBackground.getValue();
        double g=greenBackground.getValue();
        double b=blueBackground.getValue();
        previewBackgroundColor.setFill(Color.color(r,g,b));
        gridView.setBackgroundColor(Color.color(r,g,b));
        saveConfig();
    }

    public void buildHandlers(){
        Pattern pattern=Pattern.compile("-?\\d+");
        xCoord.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null));
        yCoord.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null));
        sizePrompt.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null));

        xCoord.setText("0");
        yCoord.setText("0");
        sizePrompt.setText("16");

        redCell.valueProperty().addListener((observable, oldValue, newValue) -> {changeCellColor();});
        greenCell.valueProperty().addListener((observable, oldValue, newValue) -> {changeCellColor();});
        blueCell.valueProperty().addListener((observable, oldValue, newValue) -> {changeCellColor();});
        
        redBackground.valueProperty().addListener((observable, oldValue, newValue) -> {changeBackgroundColor();});
        greenBackground.valueProperty().addListener((observable, oldValue, newValue) -> {changeBackgroundColor();});
        blueBackground.valueProperty().addListener((observable, oldValue, newValue) -> {changeBackgroundColor();});

        redGrid.valueProperty().addListener((observable, oldValue, newValue) -> {changeGridColor();});
        greenGrid.valueProperty().addListener((observable, oldValue, newValue) -> {changeGridColor();});
        blueGrid.valueProperty().addListener((observable, oldValue, newValue) -> {changeGridColor();});

        Scene scene = speedSlider.getScene();
        scene.setOnKeyPressed(event -> {
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
        });
        loadConfig();
    }

    private void saveConfig(){
        try{
            FileOutputStream fileOS=new FileOutputStream("./config");
            ObjectOutputStream out=new ObjectOutputStream(fileOS);
            out.writeObject(gridView.getViewportSize());
            out.writeObject(gridView.getCellColor().toString());
            out.writeObject(gridView.getBackgroundColor().toString());
            out.writeObject(gridView.getGridColor().toString());
            out.close();
            fileOS.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadConfig(){
        try{
            FileInputStream fileIS=new FileInputStream("./config");
            ObjectInputStream in=new ObjectInputStream(fileIS);
            gridView.resizeViewport((int)in.readObject());
            gridView.setCellColor(Color.valueOf((String)in.readObject()));
            gridView.setBackgroundColor(Color.valueOf((String)in.readObject()));
            gridView.setGridColor(Color.valueOf((String)in.readObject()));
            in.close();
            fileIS.close();
            recalibrate();
        }catch (FileNotFoundException e){
            saveConfig();
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void recalibrate(){
        Color cell=gridView.getCellColor(),back=gridView.getBackgroundColor(),grid=gridView.getGridColor();
        redCell.setValue(cell.getRed());
        greenCell.setValue(cell.getGreen());
        blueCell.setValue(cell.getBlue());

        redBackground.setValue(back.getRed());
        greenBackground.setValue(back.getGreen());
        blueBackground.setValue(back.getBlue());
        
        redGrid.setValue(grid.getRed());
        greenGrid.setValue(grid.getGreen());
        blueGrid.setValue(grid.getBlue());

        sizePrompt.setText(Integer.toString(gridView.getViewportSize()));
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

    public void saveFile(){
        FileChooser fc=new FileChooser();
        fc.setTitle("Save file...");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("GOL files","*.gol"));
        File file=fc.showSaveDialog(speedSlider.getScene().getWindow());
        if(file==null) return;
        if(GameOfLife.getInstance().saveFile(file.getPath())==0){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save file");
            alert.setHeaderText("Success");
            alert.setContentText(file.getName()+" saved successfully");
            alert.showAndWait();
        }
        else {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save file");
            alert.setHeaderText("Failure");
            alert.setContentText(file.getName()+" couldn't be saved");
            alert.showAndWait();
        }
    }

    public void openFile(){
        FileChooser fc=new FileChooser();
        fc.setTitle("Open file...");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("GOL files","*.gol"));
        File file=fc.showOpenDialog(speedSlider.getScene().getWindow());
        if(file==null) return;
        if(GameOfLife.getInstance().openFile(file.getPath())==0){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save file");
            alert.setHeaderText("Success");
            alert.setContentText(file.getName()+" saved successfully");
            alert.showAndWait();
        }
        else {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save file");
            alert.setHeaderText("Failure");
            alert.setContentText(file.getName()+" couldn't be saved");
            alert.showAndWait();
        }
    }

    public void openAbout(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Game of Life");
        alert.setContentText("By Thelias\nVersion 1.2.3");
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
