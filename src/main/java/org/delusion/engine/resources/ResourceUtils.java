package org.delusion.engine.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

// Author andy
// Created 7:33 AM
public class ResourceUtils {


    public static String readString(String path) {
        InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(path);

        System.out.println("inputStream = " + inputStream);

        String text = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
	        .lines()
            .collect(Collectors.joining("\n"));
        return text;

    }
}
