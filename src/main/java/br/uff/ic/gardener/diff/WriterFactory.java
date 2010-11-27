package br.uff.ic.gardener.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class WriterFactory {

    private static PrintWriter stWriter = null;
    private static File stFile = null;

    /**
     *
     * @param fileOutputName
     */
    public synchronized static void setWriter(String fileOutputName){
        stWriter = createWriter(fileOutputName);
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public synchronized static PrintWriter getWriter() throws IOException {
        if(stWriter != null){
            return stWriter;
        }

        String fileOutputName = "diff-"+ FormatFactory.getDateHour(new java.util.Date()) + ".txt";
        stWriter = createWriter(fileOutputName);

        return stWriter;
    }

    /**
     *
     * @return
     */
    public static File getFile(){
        return stFile;
    }
    
    private static PrintWriter createWriter(String fileOutputName){
        try {
            stFile = new File(fileOutputName);
            FileWriter writer = new FileWriter(stFile, true);
            return new PrintWriter(writer, true);
        } catch (IOException ex) {
            Logger.getLogger(NormalFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
