package com.pedroth.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private Pattern pattern;
    private Matcher matcher;

    public Tokenizer(String regex) {
        this.pattern = Pattern.compile(regex, Pattern.MULTILINE);
    }

    public static Tokenizer of(String regex) {
        return new Tokenizer(regex);
    }

    public String[] tokenize(String string) {
        this.matcher = pattern.matcher(string);
        List<String> stringList = new ArrayList<>();
        while (this.matcher.find()) {
            stringList.add(this.matcher.group(0));
        }
        return stringList.toArray(new String[stringList.size()]);
    }
}
