package driver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
    final static DirectoryChooser directoryChooser = new DirectoryChooser();
    final static FileChooser fileChooser = new FileChooser();
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("driver.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("SpamChecker");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
