package com.tyss.optimize.performance.execution.impl;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicCodeExecutor {

    public void executeCustomLogic(String code) throws Exception {
        // Step 1: Write the custom logic to a temporary .java file
        String className = "CustomPreProcessorImpl";
        String fileName = className + ".java";
        File sourceFile = new File(fileName);
        
        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(code);
        }

        // Step 2: Compile the .java file
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, fileName);

        // Step 3: Load the compiled class
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File("").toURI().toURL()});
        Class<?> clazz = Class.forName(className, true, classLoader);

        // Step 4: Execute the compiled code
        Method method = clazz.getMethod("execute");
        method.invoke(clazz.getDeclaredConstructor().newInstance());

        // Cleanup: Delete the temporary files
        sourceFile.delete();
        new File(className + ".class").delete();
    }
}

