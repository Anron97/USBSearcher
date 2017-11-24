package USBSearcher;

import USBSearcher.controller.UsbSearcherController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UsbSearcher.fxml"));
        Parent root = loader.load();
        UsbSearcherController controller = loader.getController();
        primaryStage.setOnCloseRequest((WindowEvent event) -> controller.shutdown());
        primaryStage.setTitle("USBSearcher");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
