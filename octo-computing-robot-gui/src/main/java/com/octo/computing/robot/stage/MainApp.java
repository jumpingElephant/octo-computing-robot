package com.octo.computing.robot.stage;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fxmisc.cssfx.CSSFX;
import org.fxmisc.cssfx.impl.log.CSSFXLogger;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("octo-computing-robot");
        stage.setScene(scene);
        stage.show();

        CSSFXLogger.setLoggerFactory((loggerName) -> (level, message, args) -> {
            if (CSSFXLogger.LogLevel.INFO.compareTo(level) < 0) {
                return;
            }
            System.out.println("CSSFX [" + level.name() + "] " + String.format(message, args));
        });
        CSSFX.addConverter((String string) -> new File("src/main/resources/" + string).toPath()).start();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
