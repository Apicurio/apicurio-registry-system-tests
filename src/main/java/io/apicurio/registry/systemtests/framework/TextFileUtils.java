package io.apicurio.registry.systemtests.framework;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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
