package com.pedroth.utils;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TokenizerTest {

    @Test
    public void tokenizerTest() {
        final String regex = "<!--Special-->";
        final String string = "<!--Special-->ahfdghsgfag<!--Special-->dfhaisduyfhiasdf<!--Special-->";
        final String[] strings = Tokenizer.of(regex).tokenize(string);

        assertEquals(3, strings.length);
        assertTrue(Arrays.stream(strings).allMatch(regex::equals));
    }
}
