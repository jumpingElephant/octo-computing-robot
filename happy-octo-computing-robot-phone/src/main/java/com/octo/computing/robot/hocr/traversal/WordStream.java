/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.octo.computing.robot.hocr.traversal;

import com.octo.computing.robot.hocr.elements.Page;
import com.octo.computing.robot.hocr.elements.Root;
import com.octo.computing.robot.hocr.elements.Word;
import java.util.stream.Stream;

/**
 *
 * @author alexander
 */
public class WordStream {

    private WordStream() {
    }

    public static Stream<Word> of(Root root) {
        return root
                .getHtml()
                .getBody()
                .getPages().stream()
                .flatMap(page -> page.getAreas().stream())
                .flatMap(area -> area.getParagraphs().stream())
                .flatMap(paragraph -> paragraph.getLines().stream())
                .flatMap(line -> line.getWords().stream());
    }

    public static Stream<Word> ofPage(Page page) {
        return page.getAreas().stream()
                .flatMap(area -> area.getParagraphs().stream())
                .flatMap(paragraph -> paragraph.getLines().stream())
                .flatMap(line -> line.getWords().stream());
    }

}
