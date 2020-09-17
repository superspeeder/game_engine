package org.delusion.engine.resources.loaders;

import org.delusion.engine.resources.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// Author andy
// Created 10:15 AM
public class CSVLoader<T> {


    private final Function<String, T> parser;

    public CSVLoader(Function<String,T> parser) {
        this.parser = parser;
    }

    public List<List<T>> readData(String path) {
        String textContent = ResourceUtils.readString(path);
        String[] lines = textContent.split("\n");
        List<List<T>> content = new ArrayList<>();
        int ln = 0;
        for (String line : lines) {
            content.add(new ArrayList<>());
            String[] ids = line.split(",");
            for (String id : ids) {
                content.get(ln).add(parser.apply(id.replaceFirst(" ", "")));
            }
            ln++;
        }

        return content;

    }
}
