import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author nikol
 */
public class MainFX extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("TelaEntrada.fxml"));
        Scene scene = new Scene(root);
        
        stage.setResizable(false);
        stage.setWidth(705.0);
        stage.setTitle("Problema da Montanha Russa");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
        Platform.exit();
        System.exit(0);
    }
    
}
