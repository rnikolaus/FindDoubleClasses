package rnikolaus.finddoubleclasses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author rapnik
 */
public class JarFileVisitor extends SimpleFileVisitor<Path> {
    final public Summarizer summarizer = new Summarizer();

    

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (attrs.isRegularFile()) {
            if (isJar(file.toString())) {
                parseJar(file.toFile(), file.toString());
            }
        }
        return FileVisitResult.CONTINUE;
    }

    private void parseJar(File file, String originatingPath) throws IOException {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            final JarEntry entry = e.nextElement();
            final String name = entry.getName();
            if (isJar(name)) {
                File temp = File.createTempFile("tmp", null);
                temp.deleteOnExit();
                try (FileOutputStream st = new FileOutputStream(temp);
                        InputStream is = jarFile.getInputStream(entry)) {
                    IOUtils.copy(is, st);
                }
                parseJar(temp, name + " @ " + originatingPath);
            } else {
                if (isClass(name)) {
                    summarizer.add(name, originatingPath);
                }
            }
        }
    }

    private boolean isJar(String fileName) {
        final String lowerCase = fileName.toLowerCase();
        return lowerCase.endsWith(".jar")
                || lowerCase.endsWith(".war")
                || lowerCase.endsWith(".ear")
                || lowerCase.endsWith(".sar");
    }

    private boolean isClass(String fileName) {
        return fileName.toLowerCase().endsWith(".class");
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    public Summarizer getSummarizer() {
        return summarizer;
    }

}
