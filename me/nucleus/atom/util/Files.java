// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.util;

import java.io.FileWriter;
import java.util.List;
import java.io.IOException;
import java.nio.file.Paths;

public class Files
{
    public static String[] readFileAllLines(final String path) {
        try {
            final List<String> lines = java.nio.file.Files.readAllLines(Paths.get(path, new String[0]));
            final String[] array = new String[lines.size()];
            lines.toArray(array);
            return array;
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static void writeFile(final String path, final String[] lines) {
        try {
            final FileWriter fw = new FileWriter(path);
            for (final String l : lines) {
                fw.write(l + "\r");
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
