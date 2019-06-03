package com.pedroth.builder;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TemplateBuilderTest {

    @Test
    public void testBuildTemplateFromString() throws IOException {
        String expected = "<html><div>Pedro</div></html>";
        String src = "src/test/java/com/pedroth/resources/Template.html";
        String actual = TemplateBuilder.of(src).render("main", "Pedro").apply();
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildTemplateFromFile() throws IOException {
        String expected = "<html><div><div><p>Test</p></div></div></html>";
        String src = "src/test/java/com/pedroth/resources/Template.html";
        String fillSrc = "src/test/java/com/pedroth/resources/FillTemplate.html";
        String actual = TemplateBuilder.of(src).render("main", new File(fillSrc)).apply();
        assertEquals(expected, actual);
    }
}
