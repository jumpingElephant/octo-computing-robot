/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.octo.computing.robot.stage;

import com.octo.computing.robot.hocr.elements.Page;
import com.octo.computing.robot.hocr.parser.Bounds;
import com.octo.computing.robot.hocr.traversal.WordStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author alexander
 */
public class Typesetter {

    private final Page page;

    public void drawWordBounds(GraphicsContext gc) {
        Bounds pageBounds = page.getBounds();
        double scaleX = gc.getCanvas().getWidth() / pageBounds.getRight();
        double scaleY = gc.getCanvas().getHeight() / pageBounds.getBottom();
        double scale = Math.max(scaleX, scaleY);
        gc.scale(scale, scale);
        gc.getCanvas().setHeight(pageBounds.getBottom() * scale);
        try {
            gc.drawImage(new Image(new FileInputStream(page.getImage().getPath())), 0, 0);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        gc.setStroke(Color.YELLOW);
        strokeGrid(gc, gc.getCanvas().getWidth() / scaleX, gc.getCanvas().getHeight() / scaleY, 500);

        gc.setFill(Color.GREEN);
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(5);

        WordStream.ofPage(page)
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

    public static class Builder {

        private Page page;

        private Builder() {
        }

        public Builder page(final Page value) {
            this.page = value;
            return this;
        }

        public Typesetter build() {
            return new com.octo.computing.robot.stage.Typesetter(page);
        }
    }

    public static Typesetter.Builder builder() {
        return new Typesetter.Builder();
    }

    private Typesetter(final Page page) {
        this.page = page;
    }

}
