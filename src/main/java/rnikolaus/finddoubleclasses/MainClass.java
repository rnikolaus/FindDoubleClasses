package rnikolaus.finddoubleclasses;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JFileChooser;

/**
 *
 * @author rapnik
 */
public class MainClass {

    public static void main(String[] args) throws IOException {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(null);
        if (returnVal!=JFileChooser.APPROVE_OPTION)System.exit(1);
        
        File dir = fc.getSelectedFile();
        final Summarizer summarizer = new Summarizer();
        Files.walkFileTree(dir.toPath(), new java.nio.file.SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                final String toLowerCase = file.toString().toLowerCase();
                if (attrs.isRegularFile() && 
                        (toLowerCase.endsWith(".jar")
                        ||toLowerCase.endsWith(".war")
                        ||toLowerCase.endsWith(".ear"))) {
                    JarFile jarFile = new JarFile(file.toFile());
                    Enumeration<JarEntry> e = jarFile.entries();
                    while (e.hasMoreElements()) {
                        summarizer.add(e.nextElement().getName(), file.toString());
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
            

        });
        
        //summarizer.printResult();
        TreeGui treeGui= new TreeGui(summarizer);
        treeGui.setVisible(true);
    }

}
