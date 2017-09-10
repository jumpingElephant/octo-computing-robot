/*
 * The MIT License
 *
 * Copyright 2017 alexander.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.octo.computing.robot.hocr.elements;

import com.octo.computing.robot.hocr.parser.Bounds;
import com.octo.computing.robot.hocr.parser.HocrContentHandler;
import com.octo.computing.robot.hocr.parser.Image;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;

/**
 *
 * @author alexander
 */
public enum HocrClass {
    PAGE("ocr_page", Page.class),
    AREA("ocr_carea", Area.class),
    PARAGRAPH("ocr_par", Paragraph.class),
    LINE("ocr_line", Line.class),
    WORD("ocrx_word", Word.class);

    private final String className;
    private final Class<? extends TypesettingElement> type;

    private HocrClass(String className, Class<? extends TypesettingElement> type) {
        this.className = className;
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public Class<? extends TypesettingElement> getType() {
        return type;
    }

    public static Optional<HocrClass> forClassName(String className) {
        return Arrays.asList(HocrClass.values()).stream()
                .filter(ocrClass -> ocrClass.getClassName().equals(className))
                .findAny();
    }

    public TypesettingElement newInstance(Attributes attributes) {
        try {
            TypesettingElement element = type.newInstance();
            element.setId(attributes.getValue("id"));
            element.setTitle(attributes.getValue("title"));

            applyBounds(element);
            if (this == HocrClass.PAGE) {
                applyImage((Page) element);
            }

            return element;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(HocrContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void applyBounds(TypesettingElement element) {
        Bounds bounds = Bounds.fromHocrTitleValue(element.getTitle())
                .orElseThrow(() -> new IllegalStateException("No bounds found in title: " + element.getTitle()));
        element.setBounds(bounds);
    }

    private void applyImage(Page element) {
        Image image = Image.fromHocrTitleValue(element.getTitle())
                .orElseThrow(() -> new IllegalStateException("No image found in title: " + element.getTitle()));
        element.setImage(image);
    }

}
