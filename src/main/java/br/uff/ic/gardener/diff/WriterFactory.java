package br.uff.ic.gardener.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriterFactory {

    static String fileOutputName = "diff-" + FormatFactory.getDateHour(new java.util.Date()) + ".txt";

    public static PrintWriter getWriter() throws IOException {
        try {
            FileWriter writer = new FileWriter(new File(fileOutputName), true);
            return new PrintWriter(writer, true);
        } catch (IOException ex) {
            Logger.getLogger(NormalFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
