
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Тестирование персонала Диспетчерского Пункта АО ГНЦ НИИАР.");
        primaryStage.setScene(new Scene(root,dimension.width-100,dimension.height-100));
        primaryStage.setMaximized(true);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}