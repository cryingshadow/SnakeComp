package util;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;

import javax.tools.*;

import control.*;

/**
 * Dynamic compiler for snake controls based on https://stackoverflow.com/a/21544850.
 * @author cryingshadow
 */
public class DynamicCompiler {

    /**
     * @param directory The directory holding the snake controls for a competition.
     * @return The compiled and loaded snake controls.
     */
    public static List<SnakeControl> compileAndLoad(final File directory) {
        final List<SnakeControl> res = new LinkedList<SnakeControl>();
        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory not found!");
        }
        final List<File> sourceFiles =
            Arrays
            .asList(directory.listFiles())
            .stream()
            .filter(f -> f.getName().endsWith(".java"))
            .collect(Collectors.toList());
        final List<String> names = new LinkedList<String>();
        for (final File source : sourceFiles) {
            final String fullName = source.getName();
            names.add(fullName.substring(0, fullName.length() - 5));
        }
        try {
            final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
                final List<String> optionList = new LinkedList<String>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path"));
                final Iterable<? extends JavaFileObject> compilationUnit =
                    fileManager.getJavaFileObjectsFromFiles(sourceFiles);
                final JavaCompiler.CompilationTask task =
                    compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnit);
                if (task.call()) {
                    try (
                        final URLClassLoader classLoader =
                            new URLClassLoader(new URL[]{new File("./").toURI().toURL()})
                    ) {
                        for (final String name : names) {
                            final Class<?> loadedClass = classLoader.loadClass("control.samples." + name);
                            res.add((SnakeControl)loadedClass.newInstance());
                        }
                    }
                } else {
                    final StringBuilder errString = new StringBuilder();
                    for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        errString.append("Error on line ");
                        errString.append(diagnostic.getLineNumber());
                        errString.append(" in ");
                        errString.append(diagnostic.getSource().toUri());
                        errString.append("\n");
                    }
                    throw new IllegalArgumentException(errString.toString());
                }
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        return res;
    }

}