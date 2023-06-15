package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        final List<String> KEYWORDS = new ArrayList<>();
        KEYWORDS.add("Canada");
        KEYWORDS.add("Halifax");
        KEYWORDS.add("hockey");
        KEYWORDS.add("hurricane");
        KEYWORDS.add("electricity");
        KEYWORDS.add("house");
        KEYWORDS.add("inflation");

        try {
            NewsExtractor newsExtractor = new NewsExtractor(KEYWORDS);
            NewsTransformation newsTransformation = new NewsTransformation(KEYWORDS);
            WordCounter wordCounter = new WordCounter(KEYWORDS);
            newsExtractor.execute();
            newsTransformation.execute();
            wordCounter.execute();
        } catch (IOException e) {
            System.out.println("Something went wrong!");
            System.out.println(e);
        }
    }
}