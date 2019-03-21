package gameoflife;

import gameoflife.model.GameOfLife;
import gameoflife.model.ThreadManager;
import gameoflife.views.WindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientGameOfLife extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("/fxml/window.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Game of Life");
        primaryStage.show();
        WindowController controller=loader.getController();
        controller.buildHandlers();
        GameOfLife.getInstance().step();
        ClientGameOfLife.primaryStage=primaryStage;
        primaryStage.setOnCloseRequest(event -> {
            ThreadManager.getInstance().stopAll();
            Platform.exit();
            System.exit(0);
        });
    }
}
