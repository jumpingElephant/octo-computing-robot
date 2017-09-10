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
package com.octo.computing.robot.hocr.parser;

import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author alexander
 */
public class ImageTest {

    public ImageTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testFromHocrTitleValue_page_1() {

        // GIVEN
        String ocrx_wordTitleValue = "image \"src/main/resources/output.png\"; bbox 0 0 5114 7171; ppageno 0";

        // WHEN
        Image actual = Image.fromHocrTitleValue(ocrx_wordTitleValue).get();

        // THEN
        assertEquals("src/main/resources/output.png", actual.getPath());

    }

    @Test
    public void testFromHocrTitleValue_line_1_1() {

        // GIVEN
        String ocrx_wordTitleValue = "bbox 4647 59 4969 132; baseline -0.003 -16";

        // WHEN
        Optional<Image> actual = Image.fromHocrTitleValue(ocrx_wordTitleValue);

        // THEN
        assertFalse(actual.isPresent());

    }

}
