package com.octo.computing.robot.stage;

import com.octo.computing.robot.hocr.elements.Page;
import com.octo.computing.robot.hocr.elements.Root;
import com.octo.computing.robot.hocr.parser.HocrParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;
import javax.xml.parsers.ParserConfigurationException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.xml.sax.SAXException;

public class FXMLController implements Initializable {

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Root hocrRoot = loadHocrData();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Page page = hocrRoot.getHtml().getBody().getPage(0);
        Typesetter.builder()
                .page(page)
                .build()
                .drawWordBounds(gc);

    }

    @FXML
    private void loadHocrFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("HOCR", "xhtml"));
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        System.out.println("did nothing");
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Image", "png", "jpeg"));
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file != null) {
            openFile(file);
        }
    }

    private void openFile(File file) {
        System.out.println("Open file: " + file.getAbsolutePath());

        Tesseract instance = Tesseract.getInstance(); //
        instance.setDatapath("/home/alexander/git/ocr/tessdata/");

        try {
            String result = instance.doOCR(file);
            System.out.println(result);

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        try {
            Root hocr = new HocrParser().parse(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
