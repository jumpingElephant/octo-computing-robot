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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author alexander
 */
public class Image {

    private final String path;

    public static Optional<Image> fromHocrTitleValue(String hocrTitleValue) {

        Pattern imagePattern = Pattern.compile("image \"(\\S+)\";");

        final Matcher imageMatcher = imagePattern.matcher(hocrTitleValue);

        if (imageMatcher.find()) {
            String imagePath = imageMatcher.group(1);

            return Optional.of(Image.builder()
                    .imagePath(imagePath)
                    .build());

        }
        return Optional.empty();
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Image{" + "imagePath=" + path + '}';
    }

    public static class Builder {

        private String imagePath;

        private Builder() {
        }

        public Builder imagePath(final String value) {
            this.imagePath = value;
            return this;
        }

        public Image build() {
            return new Image(imagePath);
        }
    }

    public static Image.Builder builder() {
        return new Image.Builder();
    }

    private Image(final String imagePath) {
        this.path = imagePath;
    }

}
