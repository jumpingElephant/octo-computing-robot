package com.octo.computing.robot.stage;

import com.octo.computing.robot.hocr.elements.Root;
import com.octo.computing.robot.hocr.parser.Bounds;
import com.octo.computing.robot.hocr.parser.HocrParser;
import com.octo.computing.robot.hocr.traversal.WordStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class FXMLController implements Initializable {

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Root hocrRoot = loadHocrData();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawWordBounds(gc, hocrRoot);

    }

    private Root loadHocrData() {
        File file = new File("src/main/resources/data/hocr6050804050300312430.xhtml");
        try {
            return new HocrParser().parse(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void drawWordBounds(GraphicsContext gc, Root hocrRoot) {
        Bounds pageBounds = hocrRoot.getHtml().getBody().getPage(0).getBounds();
        double scaleX = gc.getCanvas().getWidth() / pageBounds.getRight();
        double scaleY = gc.getCanvas().getHeight() / pageBounds.getBottom();
        double scale = Math.max(scaleX, scaleY);
        gc.scale(scale, scale);
        gc.getCanvas().setHeight(pageBounds.getBottom() * scale);
        try {
            gc.drawImage(new Image(new FileInputStream("src/main/resources/data/output.png")), 0, 0);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        gc.setStroke(Color.YELLOW);
        strokeGrid(gc, gc.getCanvas().getWidth() / scaleX, gc.getCanvas().getHeight() / scaleY, 500);

        gc.setFill(Color.GREEN);
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(5);

        WordStream.of(hocrRoot)
                .forEach(word -> {
                    double x, y, width, height;

                    x = word.getBounds().getLeft();
                    width = word.getBounds().getRight() - x;
                    y = word.getBounds().getTop();
                    height = word.getBounds().getBottom() - y;

                    gc.strokeRect(x, y, width, height);
                });

    }

    private void strokeGrid(GraphicsContext gc, double width, double height, int gridSize) {
        int n = Double.valueOf(Math.floorDiv(Double.valueOf(width).intValue(), gridSize)).intValue();
        IntStream.range(1, n).forEach(i -> {
            gc.strokeLine(i * gridSize, 0, i * gridSize, height);
        });
        n = Double.valueOf(Math.floorDiv(Double.valueOf(height).intValue(), gridSize)).intValue();
        IntStream.range(1, n).forEach(i -> {
            gc.strokeLine(0, i * gridSize, width, i * gridSize);
        });
    }

}
