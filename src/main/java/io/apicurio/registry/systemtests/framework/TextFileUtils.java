package io.apicurio.registry.systemtests.framework;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TextFileUtils {
    public static void replaceInFile(String filepath, String oldText, String newText) {
        File textFile = new File(filepath);
        try {
            String data = FileUtils.readFileToString(textFile);
            data = data.replace(oldText, newText);
            FileUtils.writeStringToFile(textFile, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
