package io.apicurio.registry.systemtests.framework;

import java.nio.file.Paths;

public class RapidastUtils {
    public static String getRapidastFilePath(String filename) {
        return Paths.get(Environment.TESTSUITE_PATH, "configs", "rapidast", filename).toString();
    }
}
