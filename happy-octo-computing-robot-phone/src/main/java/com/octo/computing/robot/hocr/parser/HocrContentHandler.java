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

import com.octo.computing.robot.hocr.elements.Body;
import com.octo.computing.robot.hocr.elements.Foo;
import com.octo.computing.robot.hocr.elements.Head;
import com.octo.computing.robot.hocr.elements.HocrClass;
import com.octo.computing.robot.hocr.elements.HocrElement;
import com.octo.computing.robot.hocr.elements.Html;
import com.octo.computing.robot.hocr.elements.Meta;
import com.octo.computing.robot.hocr.elements.Root;
import com.octo.computing.robot.hocr.elements.Title;
import com.octo.computing.robot.hocr.elements.TypesettingElement;
import com.octo.computing.robot.hocr.elements.ValueHolder;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author alexander
 */
public class HocrContentHandler extends DefaultHandler {

    private Root root;

    private Html html;

    private Head head;

    private Body body;

    private final LinkedList<HocrElement> hocrElementPath = new LinkedList();

    private ValueHolder currentElement;

    public Root getRoot() {
        return root;
    }

    @Override
    public void startDocument() throws SAXException {
        this.root = new Root();
        this.currentElement = this.root;
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        if (this.currentElement instanceof HocrElement) {
            this.hocrElementPath.addLast((HocrElement) this.currentElement);
        }
        System.out.println("peek is " + hocrElementPath.stream().map(hocrElement -> hocrElement.getClass().getSimpleName()).collect(Collectors.joining("-")));
        switch (qualifiedName) {
            case "html":
                this.currentElement = createHtmlElement(uri, localName, qualifiedName, attributes);
                break;

            case "head":
                this.currentElement = createHeadElement(uri, localName, qualifiedName, attributes);
                break;

            case "title":
                this.currentElement = createTitleElement(uri, localName, qualifiedName, attributes);
                break;

            case "meta":
                this.currentElement = createMetaElement(uri, localName, qualifiedName, attributes);
                break;

            case "body":
                this.currentElement = createBodyElement(uri, localName, qualifiedName, attributes);
                break;

            case "div":
            case "p":
            case "span":
                HocrElement te = createHocrClassElement(uri, localName, qualifiedName, attributes);
                this.currentElement = te;
                break;

            default:
                HocrElement he = createUnknown(uri, localName, qualifiedName, attributes);
                this.currentElement = he;

        }
        System.out.println("start of " + currentElement.getClass().getSimpleName());
        if (this.currentElement == null) {
            throw new IllegalStateException("Work element must not turn to null");
        }
    }

    private Html createHtmlElement(String uri, String localName, String qualifiedName, Attributes attributes) {
        html = new Html();

        root.setHtml(html);
        return html;
    }

    private Head createHeadElement(String uri, String localName, String qualifiedName, Attributes attributes) {
        head = new Head();

        html.setHead(head);
        return head;
    }

    private Title createTitleElement(String uri, String localName, String qualifiedName, Attributes attributes) {
        Title title = new Title();

        head.setTitle(title);
        return title;
    }

    private Meta createMetaElement(String uri, String localName, String qualifiedName, Attributes attributes) {
        Meta meta = new Meta();
        IntStream.range(0, attributes.getLength())
                .forEach(i -> meta.addAttribute(attributes.getQName(i), attributes.getValue(i)));

        head.addMeta(meta);
        return meta;
    }

    private Body createBodyElement(String uri, String localName, String qualifiedName, Attributes attributes) {
        body = new Body();

        html.setBody(body);
        return body;
    }

    private TypesettingElement createHocrClassElement(String uri, String localName, String qualifiedName, Attributes attributes) {
        return HocrClass.forClassName(attributes.getValue("class"))
                .map(hocrClass -> {
                    switch (hocrClass) {
                        case PAGE:
                        case AREA:
                        case PARAGRAPH:
                        case LINE:
                        case WORD:
                            return createTypesettingElement(hocrClass, uri, localName, qualifiedName, attributes);

                    }

                    return null;
                })
                .orElseThrow(() -> new IllegalArgumentException("Unknown class type: " + attributes.getValue("class")));

    }

    private TypesettingElement createTypesettingElement(HocrClass hocrClass, String uri, String localName, String qualifiedName, Attributes attributes) {

        TypesettingElement element = hocrClass.newInstance(attributes);

        appendToParent(element);
        return element;
    }

    private HocrElement createUnknown(String uri, String localName, String qualifiedName, Attributes attributes) {
        Foo foo = new Foo();
        foo.setQualifiedName(qualifiedName);
        IntStream.range(0, attributes.getLength())
                .forEach(i -> foo.addAttribute(attributes.getQName(i), attributes.getValue(i)));

        appendToParent(foo);
        return foo;
    }

    private void appendToParent(HocrElement element) {
        HocrElement parent = hocrElementPath.peekLast();
        if (parent == null) {
            parent = body;
        }
        parent.addChild(element);
    }

    @Override
    public void endElement(String uri, String localName, String qualifiedName) throws SAXException {
        System.out.println("end of " + (currentElement == null ? "null" : currentElement.getClass().getSimpleName()));
        switch (qualifiedName) {
            case "html":
                this.currentElement = root;
                break;

            case "head":
                this.currentElement = html;
                break;

            case "title":
                this.currentElement = head;
                break;

            case "meta":
                this.currentElement = head;
                break;

            case "body":
                this.currentElement = html;
                break;

            case "div":
            case "p":
            case "span":
                this.currentElement = hocrElementPath.removeLast();
                break;

            default:
                this.currentElement = hocrElementPath.removeLast();
        }
        System.out.println("next work element=" + (currentElement == null ? "null" : currentElement.getClass().getSimpleName()));
        if (this.currentElement == null) {
            throw new IllegalStateException("Work element must not turn to null");
        }
    }

    @Override
    public void characters(char[] characters, int start, int length) throws SAXException {
        String value = new String(characters, start, length).trim();
        this.currentElement.setValue(value);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        super.error(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        super.fatalError(e); //To change body of generated methods, choose Tools | Templates.
    }

}
