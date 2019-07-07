package sample;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;
        Controller controller = new Controller(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        stage.setTitle("Белые против Красных");
        stage.setScene(new Scene(root, 400, 500));
        stage.resizableProperty().set(false);
        stage.show();
    }

    public void loadScene() throws IOException {
        Controller controller = new Controller(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        stage.close();
        stage.setScene(new Scene(root, 400, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
