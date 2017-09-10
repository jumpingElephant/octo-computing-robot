/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 *
 * @author alexander
 */
public class JavaApplication2 {

    /**
     * export LC_NUMERIC="C"
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        File imageFile = new File("src/main/resources/output.png");
        Tesseract instance = Tesseract.getInstance(); //
        instance.setDatapath("/home/alexander/git/ocr/tessdata/");

        try {

            String result = instance.doOCR(imageFile);
            System.out.println(result);

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

}
